# TOPIC 1 (continued): Code Examples — Medium Complexity

## STEP 7 (continued) — Medium Example: Login-Check Servlet using `doPost()` and `getParameter()`

We'll now build a Servlet that receives form data via **POST**, reads request parameters, performs simple validation logic, and responds differently based on the outcome — a realistic pattern you'll use constantly in real applications.

---

### 7.5 — The HTML Form (Front-End Entry Point)

**File location:** `src/main/webapp/login.html`

```html
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
    <h2>Login</h2>
    <form action="login" method="post">
        <label>Username:</label>
        <input type="text" name="username" required><br><br>

        <label>Password:</label>
        <input type="password" name="password" required><br><br>

        <input type="submit" value="Login">
    </form>
</body>
</html>
```

**Explanation of key attributes:**

- **`action="login"`** — This tells the browser: *"when this form is submitted, send the request to the URL `login` (relative to the current page's path)."* Since this file lives at `webapp/login.html`, and our app's context path is `/myapp`, the browser will send the request to `http://localhost:8080/myapp/login`. This **must exactly match** the `@WebServlet` URL pattern of the Servlet we're about to write.
- **`method="post"`** — Tells the browser to send the form data using the **HTTP POST method**, not GET. This is a critical, deliberate choice we'll explain fully in the next sub-section.
- **`name="username"` / `name="password"`** — These `name` attributes become the **parameter keys** that our Servlet will retrieve via `request.getParameter("username")` and `request.getParameter("password")`. The `name` attribute — **not** the `id` attribute — is what gets sent in the HTTP request body; this is a very common point of confusion for beginners coming from a pure front-end background.

---

### 7.6 — Why POST Instead of GET? (Important Conceptual Detail)

This distinction is frequently tested in vivas and interviews, so let's be precise:

| Aspect | GET | POST |
|---|---|---|
| Data location | Appended to URL as query string (`?username=x&password=y`) | Sent inside the **request body**, invisible in the URL |
| Visibility | Visible in browser history, server logs, bookmarks | Not visible in URL/history |
| Data length limit | Limited by URL length restrictions (browser/server dependent, often ~2000 chars) | No such practical limit (body can be large) |
| Idempotency | Should be idempotent (safe to repeat, e.g., refresh) | Not idempotent (resubmitting can cause duplicate actions, e.g., double form submission) |
| Caching | Can be cached by browsers/proxies | Not cached by default |
| Use case | Fetching/reading data (search, viewing a page) | Submitting sensitive or state-changing data (login, form submission, payments) |

---

### 7.7 — The Servlet: `LoginServlet.java`

**File location:** `src/main/java/com/company/myapp/servlet/LoginServlet.java`

```java
package com.company.myapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Hardcoded credentials for now — real validation against DB comes in Module 3 (JDBC)
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Read parameters submitted from the HTML form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Step 2: Set response content type BEFORE writing anything
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Step 3: Basic null/empty validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            out.println("<html><body>");
            out.println("<h2 style='color:red;'>Error: Username and password are required.</h2>");
            out.println("<a href='login.html'>Go back</a>");
            out.println("</body></html>");
            return; // stop further execution
        }

        // Step 4: Business logic — credential check
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            out.println("<html><body>");
            out.println("<h2 style='color:green;'>Login successful! Welcome, " + username + ".</h2>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<h2 style='color:red;'>Invalid username or password.</h2>");
            out.println("<a href='login.html'>Try again</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h3>This endpoint only supports POST requests via the login form.</h3>");
        out.println("</body></html>");
    }
}
```

---

### 7.8 — Line-by-Line Explanation (New Concepts Only — repeated concepts from HelloServlet skipped)

**`private static final String VALID_USERNAME = "admin";`**
- A `static final` field — this is a **class-level constant**, shared across all instances (though remember: there's only ever one instance anyway, per Step 2). Marked `private` since it's an internal implementation detail, not part of any external contract.
- **Thread-safety note (ties back to Step 2, Section 7):** This is a **read-only constant**, never modified after class loading — so even though it's technically an instance-accessible field on a shared Servlet object, it's **completely thread-safe**, because no thread ever writes to it. Contrast this with a *mutable* instance variable (like a counter that increments per request), which would **not** be thread-safe. This distinction — "is the shared state read-only or mutable?" — is exactly what determines thread-safety, not merely "is it an instance variable or not."

**`request.getParameter("username")`**
- Retrieves the value submitted under the form field `name="username"`. Returns type `String`.
- **Important behavior to memorize:** If the parameter doesn't exist at all in the request (e.g., a malformed request missing that field), `getParameter()` returns **`null`** — it does **not** throw an exception. This is precisely why Step 3's validation (`username == null`) is necessary — skipping null-checks here is one of the most common sources of `NullPointerException` in real Servlet code, and a frequently-asked debugging question in interviews.
- If the parameter exists but is submitted as an **empty string** (e.g., user submitted the form without typing anything, but the field itself was present), `getParameter()` returns `""` (empty string), **not** `null` — which is why we **also** check `.trim().isEmpty()`, not just `== null`. This dual-check pattern (`null` OR `empty`) is standard, defensive real-world validation.

**`response.setContentType("text/html")` called again**
- Notice this Servlet handles **two** methods (`doPost` and `doGet`), and each independently sets content type and gets its own writer — because, as established in Step 2, **each request gets a fresh `HttpServletRequest`/`HttpServletResponse` pair**; nothing is shared or reused between the `doGet()` call for one request and the `doPost()` call for a different request. There's no "leftover state" between them.

**`return;` after the validation error block**
- A plain `return` statement (not `return null` or `return someValue`, since the method's return type is `void`) — this **exits the method immediately**, preventing the code from falling through into the credential-check logic below with `null`/empty values, which would otherwise cause a `NullPointerException` at `username.equals(...)` (calling `.equals()` on a `null` reference). This is a standard **guard clause** pattern — validate early, exit early, keep the "happy path" logic unindented and readable below.

**`username.equals(VALID_USERNAME)` — NOT `VALID_USERNAME.equals(username)`... actually let's flag this properly**
- Actually, notice I wrote `username.equals(VALID_USERNAME)`. This works fine **here** because we already guaranteed `username` is non-null via our Step 3 validation above. But as a **best practice** to build into your habits: when comparing a potentially-null variable against a known-non-null constant, prefer `VALID_USERNAME.equals(username)` (constant first) — this way, even if `username` were somehow null, you'd get `false` instead of a `NullPointerException`, because calling `.equals()` on the non-null constant is always safe. I'm flagging this explicitly because **exam/interview code reviewers actively look for this pattern** — it's a well-known Java defensive-coding idiom.

**Overriding `doGet()` to show a message instead of leaving it unoverridden**
- Recall from Step 6.4: if we simply **didn't** override `doGet()` at all, a GET request to `/login` would hit `HttpServlet`'s default `doGet()` implementation and return **HTTP 405 Method Not Allowed**. Here, we deliberately override it to show a friendlier custom message instead — this is a **design choice**, not a requirement. Both approaches are valid depending on whether you want strict method enforcement (let it 405) or a friendlier explicit message (override and explain). I'm showing you the override approach here specifically so you see it in action; we'll discuss the trade-off further in the "Common Errors" step.

---

### 7.9 — What Happens Internally When You Submit This Form (Preview of Step 8: Execution Flow)

Briefly, before we do the full deep-dive in Step 8:

1. Browser loads `login.html` (static file, served directly by Tomcat's default servlet — no Java code involved at all for this step).
2. User fills form, clicks Submit.
3. Browser constructs an HTTP POST request to `/myapp/login`, with body: `username=admin&password=admin123` (URL-encoded form data, `Content-Type: application/x-www-form-urlencoded`).
4. Tomcat routes this to `LoginServlet` (matched via `@WebServlet("/login")`).
5. `service()` detects POST, calls `doPost()`.
6. `getParameter()` internally **parses the request body** (Tomcat does this lazily — parsing happens the first time you call `getParameter()`, not automatically on request arrival) to extract `username` and `password` values.
7. Your validation and business logic run.
8. Response HTML is written and sent back.

