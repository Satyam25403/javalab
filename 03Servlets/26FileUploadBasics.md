# TOPIC 17: File Upload Basics

## CONCEPT

### Why this concept exists

Every form we've built so far used `application/x-www-form-urlencoded` encoding — fine for text fields, but **fundamentally incapable of transmitting binary data** like images, PDFs, or documents efficiently. HTTP defines a **different encoding format**, `multipart/form-data`, specifically designed to transmit files alongside regular form fields in a single request. This topic covers how Servlets handle that format.

### What problem it solves

A **profile picture upload**, a **document submission**, an **attachment field** — all require sending raw binary file content from the browser to the server. `multipart/form-data` solves this by splitting the request body into distinct **"parts"** — each part can be a regular text field OR a file's raw binary content, separated by unique boundary markers. The Servlet API's `Part` interface gives you structured access to each piece.

### Real-world analogy

Think of mailing a **package with multiple compartments** — one compartment holds a handwritten note (a text form field), another compartment holds a physical photograph (a file), each clearly separated and labeled inside the same box (the single HTTP request), rather than trying to somehow squeeze the photograph into the same slot as the note.

---

## The HTML Form — Required Attribute

```html
<form action="upload" method="post" enctype="multipart/form-data">
    <label>Your Name:</label>
    <input type="text" name="username"><br>

    <label>Choose a file:</label>
    <input type="file" name="profilePicture"><br>

    <input type="submit" value="Upload">
</form>
```

**`enctype="multipart/form-data"` — the single most commonly forgotten, critical attribute:** Without this, even with `<input type="file">` present, the browser will **not** actually transmit the file's binary content correctly. **This is the single most common file-upload bug**.

---

## SYNTAX — `@MultipartConfig` and the `Part` Interface

```java
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
```

**`@MultipartConfig`** — a **mandatory** annotation on any Servlet that needs to process file uploads. Without it, calling `request.getPart()`/`getParts()` throws an exception, even if the form correctly used `multipart/form-data`.

```java
@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1MB — files larger than this are written to disk temporarily during processing, rather than held fully in memory
    maxFileSize = 1024 * 1024 * 10,        // 10MB — maximum size for a SINGLE uploaded file
    maxRequestSize = 1024 * 1024 * 50      // 50MB — maximum size for the ENTIRE multipart request (all files + fields combined)
)
public class UploadServlet extends HttpServlet {
    // ...
}
```

**Why these three separate limits exist, precisely:**
- `fileSizeThreshold` — a **performance/memory** tuning knob: small files can be held entirely in server memory during processing (fast), while larger files get buffered to a temporary disk location instead (avoiding memory exhaustion under many concurrent large uploads).
- `maxFileSize` — a **per-file** cap, protecting against a single absurdly large file (e.g., someone trying to upload a 4GB video to a "profile picture" field).
- `maxRequestSize` — an **overall request** cap, protecting against many moderately-sized files combined exceeding reasonable limits, or general denial-of-service-style abuse via oversized requests.

**If these limits are exceeded, the container throws `IllegalStateException`** — you should catch this and respond gracefully rather than letting it become an uncaught 500 error.

---

## The `Part` Interface — Reading Uploaded File Data

| Method | Returns | Purpose |
|---|---|---|
| `getName()` | `String` | The form field's `name` attribute |
| `getSubmittedFileName()` | `String` | The **original filename** as it existed on the client's machine (e.g., `"vacation.jpg"`) |
| `getSize()` | `long` | Size of this part, in bytes |
| `getContentType()` | `String` | MIME type (e.g., `"image/jpeg"`) |
| `getInputStream()` | `InputStream` | Raw byte stream of the file's content — this is how you actually read/save the file |
| `write(String fileName)` | `void` | A convenience method that writes this part's content directly to a file, relative to a configured location |

---

## Code Example — Complete File Upload Servlet

```java
package com.company.myapp.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // A directory OUTSIDE the web application's deployable structure —
    // explained precisely below why this matters
    private static final String UPLOAD_DIR = "C:/myapp-uploads/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        // Regular text field — read exactly as always (Topic 1)
        String username = request.getParameter("username");

        // File field — read via getPart(), NOT getParameter()
        Part filePart = request.getPart("profilePicture");

        if (filePart == null || filePart.getSize() == 0) {
            response.getWriter().println("<h2>No file was selected.</h2>");
            return;
        }

        String originalFileName = filePart.getSubmittedFileName();

        // Ensure the upload directory exists
        File uploadDirFile = new File(UPLOAD_DIR);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Save the file
        try (InputStream fileContent = filePart.getInputStream()) {
            File targetFile = new File(uploadDirFile, originalFileName);
            Files.copy(fileContent, targetFile.toPath());
        }

        response.getWriter().println("<h2>File '" + originalFileName + "' uploaded successfully by " + username + "!</h2>");
        response.getWriter().println("<p>Size: " + filePart.getSize() + " bytes, Type: " + filePart.getContentType() + "</p>");
    }
}
```

### Explanation of key decisions

**Mixing `getParameter()` and `getPart()` in the same method:** Notice `username` is still read via the familiar `request.getParameter("username")` — the Servlet API transparently handles regular text fields within a `multipart/form-data` request the same way as before; you only need `getPart()` specifically for the **file** field itself.

**`try (InputStream fileContent = filePart.getInputStream())`**
- This is **try-with-resources** (a Java language feature you'll see formalized fully as its own topic in Module 3's JDBC section, but it applies generally to any `AutoCloseable` resource, including file streams) — it **guarantees** the stream is closed automatically when the block exits, even if an exception occurs partway through, without needing an explicit `finally` block. This is the modern, correct way to handle any stream/resource that must be closed.

**`Files.copy(fileContent, targetFile.toPath())`**
- A modern (Java NIO) convenience method that efficiently copies the entire input stream's content into the target file — simpler and less error-prone than manually reading byte arrays in a loop (the older, pre-NIO approach you may see in legacy tutorials).

---

## Critical Design Decision — Why Store Uploads OUTSIDE the Web Application Directory

**Why NOT save uploaded files inside `webapp/` (e.g., `webapp/uploads/`)?**

1. **Redeployment wipes it out** — recall lifecycle discussion: redeploying your application (a routine part of development, and even production updates) typically **replaces the entire deployed directory structure**. Any files saved inside `webapp/` would be **permanently lost** on the next redeploy.
2. **Security risk** — if uploads are saved somewhere directly browser-accessible (anywhere under `webapp/`, outside `WEB-INF/`), a malicious user could potentially upload a disguised executable script (if your validation is weak) and then **directly request and execute it** via its URL — a serious, real-world attack vector called **unrestricted file upload**.
3. **Scalability** — in real production environments (especially cloud-based ones), the application server's own filesystem is often ephemeral or not meant for persistent user data at all — files are typically stored in dedicated storage services (like AWS S3) instead. Using an external directory path (like shown, `C:/myapp-uploads/`) is a small step in decoupling "where the app runs" from "where uploaded data persists."

---

## Basic Validation Considerations (Preview — Full Security Practices Beyond This Course's Current Scope)

Even at this "basics" level, a few validation habits are worth adopting immediately:

```java
String contentType = filePart.getContentType();
if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
    response.getWriter().println("<h2>Only JPEG and PNG images are allowed.</h2>");
    return;
}

if (filePart.getSize() > 5 * 1024 * 1024) { // 5MB, application-level check beyond the @MultipartConfig limit
    response.getWriter().println("<h2>File too large. Maximum 5MB allowed.</h2>");
    return;
}
```

**Important nuance:** validating `getContentType()` alone is **not** fully secure — a malicious user can trivially fake this header's value from their browser/tool. Genuinely robust file-type validation (inspecting actual file bytes/magic numbers, sanitizing filenames to prevent path traversal attacks like `../../etc/passwd` as a filename) is a deeper security topic beyond "basics" — there's more rigor needed for a genuinely production-hardened upload feature.

---

## EXECUTION FLOW

```
Browser: User selects a file, submits form (enctype="multipart/form-data")
        │
        ▼
Browser encodes request body with distinct "parts", separated by
a unique boundary string (e.g., "----WebKitFormBoundary7MA4YWx...")
   Part 1: name="username", plain text value
   Part 2: name="profilePicture", filename="vacation.jpg", binary content
        │
        ▼
Tomcat receives request, recognizes Content-Type: multipart/form-data
        │
        ▼
UploadServlet.doPost() called
   → request.getParameter("username") reads Part 1's value normally
   → request.getPart("profilePicture") returns a Part object for Part 2
        │
        ▼
filePart.getInputStream() gives access to Part 2's raw binary bytes
        │
        ▼
Files.copy(...) writes those bytes to C:/myapp-uploads/vacation.jpg
        │
        ▼
Response confirms success back to the browser
```

---

## COMMON ERRORS

**Error: `getPart()` throws an exception — "not a multipart request"**
- **Cause:** Forgetting `enctype="multipart/form-data"` on the HTML form (the single most common mistake, flagged above), OR forgetting the `@MultipartConfig` annotation on the Servlet.
- **Fix:** Verify both are present — they're both required, independently, for file upload to work at all.

**Error: `IllegalStateException` — file exceeds configured size limits**
- **Cause:** Uploaded file/request exceeds `maxFileSize`/`maxRequestSize` from `@MultipartConfig`.
- **Fix:** Wrap the relevant logic in a try-catch, and show the user a friendly "file too large" message rather than letting this become an uncaught 500 error (Topic 16 principles applied here).

**Error: `NullPointerException` on `filePart`**
- **Cause:** User submitted the form without selecting any file at all — `getPart()` may return a `Part` object with **zero size** rather than `null` in some cases, or `null` in others, depending on container specifics — **always check both** `filePart == null` **and** `filePart.getSize() == 0`, exactly as shown in the example above.

**Error: `FileAlreadyExistsException` from `Files.copy()`**
- **Cause:** Two different users (or the same user twice) upload files with the **identical filename**, and `Files.copy()` refuses to silently overwrite by default.
- **Fix:** Generate unique filenames server-side (e.g., prefixing with a timestamp or a UUID) rather than trusting the original filename to be unique — this also incidentally improves security by not relying on user-controlled filenames directly.