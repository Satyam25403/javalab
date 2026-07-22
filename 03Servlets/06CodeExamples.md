# TOPIC 1 (continued): Code Examples

## STEP 7 — CODE EXAMPLES

We start with project setup (since you're using Eclipse + Maven), then write the **smallest possible working Servlet**, explained line by line.

---

### 7.1 — Creating the Eclipse Maven Web Project (One-Time Setup)

**Step-by-step Eclipse menu path:**

1. **File → New → Maven Project**
2. Check **"Create a simple project (skip archetype selection)"** — this gives us a clean, minimal project so we build the structure ourselves (better for learning than an archetype dumping unfamiliar boilerplate on you).
3. Click **Next**. Fill in:
   - **Group Id:** `com.company`
   - **Artifact Id:** `myapp`
   - **Packaging:** change dropdown from `jar` to **`war`** ← critical, don't miss this
4. Click **Finish**.

Eclipse generates this initial structure:
```
myapp/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       └── resources/
```

Notice: **no `webapp` folder yet**, and no `WEB-INF`. Because we skipped the archetype, Eclipse doesn't auto-generate these. We create them manually — good, because now you know *exactly* why each folder exists (from Step 3), rather than trusting a wizard blindly.

**5. Manually create the missing folders:**
- Right-click project → **New → Folder** → create path `src/main/webapp`
- Inside `webapp`, create folder `WEB-INF`

**6. Fix Eclipse's "Deployment Descriptor" recognition (important):**
- Right-click project → **Properties → Project Facets**
- Check **"Dynamic Web Module"**, set version to **6.0** (matches Servlet 6.0 / Jakarta EE 10)
- Eclipse will prompt to set the **"Content directory"** — set it to `src/main/webapp`
- Apply and Close.

This step tells Eclipse: *"Treat this Maven project as a proper web application, and treat `src/main/webapp` as its web root"* — without this, Eclipse won't let you deploy the project to a Tomcat server.

**7. Replace the generated `pom.xml` with our full version from Step 4** (Servlet API, JSP API, JSTL, MySQL Connector, `maven-war-plugin`, Java 17 compiler settings). Save the file — Eclipse's m2e plugin automatically detects changes and downloads dependencies from Maven Central in the background. You'll see a small progress indicator in the bottom-right corner of Eclipse.

**8. Register a Tomcat Server in Eclipse:**
- **Window → Show View → Servers**
- In the Servers panel, right-click → **New → Server**
- Choose **Apache → Tomcat v10.1 Server** (must match the Tomcat version you downloaded)
- Point it to your Tomcat installation directory when prompted
- Finish.

**9. Add your project to that server:**
- Right-click the Tomcat server in the Servers panel → **Add and Remove...**
- Move `myapp` from the left ("Available") to the right ("Configured") panel
- Finish.

Your project is now fully wired: Maven manages dependencies, Eclipse's Dynamic Web Module facet recognizes it as deployable, and Tomcat is registered as your run target. From here onward, running your app is a single right-click → **Run on Server**.

---

### 7.2 — Smallest Possible Servlet: `HelloServlet`

**File location:** `src/main/java/com/company/myapp/servlet/HelloServlet.java`

**Eclipse path to create it:** Right-click `src/main/java` → **New → Class** → Package: `com.company.myapp.servlet`, Name: `HelloServlet`.

```java
package com.company.myapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Hello from HelloServlet!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
```

---

### 7.3 — Line-by-Line Explanation (Nothing Skipped)

**`package com.company.myapp.servlet;`**
- Declares which package this class belongs to. Must match the folder structure exactly (`com/company/myapp/servlet/HelloServlet.java`) — Java enforces this correspondence between package declaration and physical folder path. This is also the package we designed back in Step 3's project structure.

**`import java.io.IOException;`**
- Needed because `doGet()`'s signature declares `throws IOException`. Without this import, the compiler wouldn't recognize the `IOException` type.

**`import java.io.PrintWriter;`**
- `PrintWriter` is the class returned by `response.getWriter()`, which we use to write text output to the HTTP response body.

**`import jakarta.servlet.ServletException;`**
- Note the **`jakarta.*`** namespace (not `javax.*`) — as established in Step 4, we're using Jakarta EE 10 / Servlet 6.0 with Tomcat 10+. This exception type is needed for the `throws` clause.

**`import jakarta.servlet.annotation.WebServlet;`**
- Imports the `@WebServlet` annotation itself, which we use below instead of a `web.xml` `<servlet>`/`<servlet-mapping>` pair (Step 5, Section 5.3).

**`import jakarta.servlet.http.HttpServlet;`**
- The abstract base class we must extend, as established in Step 6.

**`import jakarta.servlet.http.HttpServletRequest;`** and **`HttpServletResponse;`**
- The two objects representing the incoming request and outgoing response, passed as parameters into `doGet()`.

**`@WebServlet("/hello")`**
- This is shorthand syntax for `@WebServlet(urlPatterns = "/hello")` — when you're only setting the URL pattern and nothing else (no `name`, `initParams`, etc.), Java annotations allow this abbreviated single-value syntax. This single line **replaces** the entire `<servlet>` + `<servlet-mapping>` block we wrote by hand in `web.xml` back in Step 5. The container scans all classes at deployment time, finds this annotation, and automatically registers the mapping — no `web.xml` entry needed at all for this Servlet.

**`public class HelloServlet extends HttpServlet {`**
- `public` — the container must be able to access this class from outside its package (via reflection), so it cannot be `private` or package-private in any restrictive sense that would block that access... actually to be precise: Servlet classes are conventionally `public` because the container instantiates them via reflection and needs unrestricted access; a non-public class *could* technically work due to Java reflection's `setAccessible()` capabilities in some container implementations, but `public` is the correct, spec-compliant, universally safe convention. Always declare Servlets `public`.
- `extends HttpServlet` — inherits the entire lifecycle machinery and the `service()` dispatch logic explained in Step 6.

**`private static final long serialVersionUID = 1L;`**
- `HttpServlet` implements `java.io.Serializable` (inherited transitively through the `Servlet` interface hierarchy in some implementations, and practically Tomcat *can* serialize Servlet instances to disk during server restarts in some session-persistence scenarios, though this is edge-case behavior). Declaring an explicit `serialVersionUID` is a **best practice** (Java compiler warning otherwise) to avoid `InvalidClassException` issues if the class definition changes between serialization and deserialization. For now, treat this as **required boilerplate** — we'll explain Java serialization mechanics in depth when we reach Session Handling / `HttpSession`, where serialization becomes practically relevant.

**`@Override`**
- A compiler-checked annotation confirming that `doGet()` genuinely overrides a method from the superclass (`HttpServlet`). If you misspell the method name (e.g., `dogets`), the compiler throws an **error** immediately, rather than silently creating a new, unrelated method that the container would never call — this has saved countless developers from a very confusing bug where "my Servlet code never runs" turns out to be a typo in the method name.

**`protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {`**
- Exact signature match required (Step 6.3) — `protected` access, `void` return, these two exact parameter types in this exact order, these two exact checked exceptions declared.

**`response.setContentType("text/html");`**
- Sets the HTTP response header `Content-Type: text/html`, telling the browser: *"interpret the body I'm about to send as HTML, not plain text or JSON."* **This must be called BEFORE `getWriter()`** — once you start writing to the output stream, headers are typically already being flushed/committed, and changing `Content-Type` afterward has no effect (a very common beginner mistake and debugging question).

**`PrintWriter out = response.getWriter();`**
- Retrieves the character-output stream tied to this response's body. Internally, this stream is connected to the underlying **TCP socket's output stream** (through several buffering/wrapper layers within Tomcat) — anything written here eventually gets transmitted to the browser as the HTTP response body.

**`out.println("<html>");` (and subsequent lines)**
- Directly writes raw HTML strings into the response body. This is the **"Java code generating HTML by string concatenation"** approach explicitly flagged as a **disadvantage** back in Step 1 — messy, hard to maintain, no separation of concerns. We're doing it here **only** because it's the simplest possible complete example; starting in Module 2 (JSP), you'll learn the proper way to separate presentation from logic, and by the MVC project in Module 3, Servlets will **never** contain raw HTML like this — they'll only forward to JSP views.

**No explicit `out.close()`**
- The container automatically closes/flushes the response's writer when the `service()` method (and therefore `doGet()`) completes — you don't need to manually close it (and in fact, manually closing it prematurely can cause issues if the container expects to do additional processing after your method returns).

---

### 7.4 — Deploying and Running It

1. Right-click project → **Run As → Run on Server**
2. Select your registered Tomcat v10.1 server → Finish.
3. Eclipse compiles your code, packages it (using Maven's WAR packaging under the hood), and Tomcat starts up, auto-deploying the app.
4. Eclipse opens an embedded browser (or you manually open a browser) to:
   ```
   http://localhost:8080/myapp/hello
   ```
5. You should see: **"Hello from HelloServlet!"** rendered as an HTML heading.

**Trace this against Step 2 (Internal Working):** When you hit that URL, Tomcat's Mapper matches `/hello` against your `@WebServlet("/hello")` annotation, finds no existing `HelloServlet` instance yet, loads the class, instantiates it via reflection, calls `init()` (inherited empty implementation — does nothing since you didn't override it), then a thread from the pool calls `service()`, which detects `GET` and delegates to your `doGet()`. Everything we studied theoretically in Step 2 just happened for real, right there.

