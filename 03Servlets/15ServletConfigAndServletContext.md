# TOPIC 6: ServletConfig and ServletContext

## CONCEPT

### Why this concept exists

The **most frequently confused pairs of concepts** in Servlet programming — and one of the most reliably asked viva/interview questions: *"What's the difference between ServletConfig and ServletContext?"*

The short answer, which we'll now fully justify: **`ServletConfig` is per-Servlet, private configuration. `ServletContext` is per-application, shared configuration and shared storage.**

### Why two separate objects instead of one?

Because they solve **fundamentally different scoping problems**:
- Sometimes you need configuration that's **specific to one Servlet** (e.g., `LoginServlet` needs a `maxLoginAttempts` value that `RegisterServlet` doesn't care about at all) → `ServletConfig`.
- Sometimes you need configuration or shared data that **every Servlet, Filter, and JSP in the entire application** should be able to access (e.g., the application's name, a shared database connection pool reference, a global feature flag) → `ServletContext`.

### Real-world analogy

Think of a large company:
- **`ServletConfig`** = An **individual employee's personal work badge and desk settings** — specific to that one employee (Servlet), configured for them alone, not visible or relevant to other employees.
- **`ServletContext`** = The **company-wide intranet/bulletin board** — a single shared resource that **every** employee (every Servlet, Filter, Listener, JSP) in the entire building (application) can read from and, in some cases, post to. There's exactly **one** bulletin board per company (one `ServletContext` per web application), but potentially **many** employees with their own individual badges (`ServletConfig`, one per Servlet instance).

---

## ServletConfig — Deep Dive

### What it represents

An object created by the container **once per Servlet**, holding:
1. That Servlet's `<init-param>` values (from `web.xml`) or `initParams` (from `@WebServlet`).
2. That Servlet's `<servlet-name>`.
3. A reference to the shared `ServletContext` (yes — `ServletConfig` itself provides a bridge to `ServletContext`, via `getServletContext()`).

### Key methods

| Method | Returns | Purpose |
|---|---|---|
| `getInitParameter(String name)` | `String` | Retrieves one specific init parameter's value, or `null` if not found |
| `getInitParameterNames()` | `Enumeration<String>` | Retrieves all init parameter names, for iteration |
| `getServletName()` | `String` | Returns the `<servlet-name>` value |
| `getServletContext()` | `ServletContext` | Returns the shared, application-wide context object |

### Code Example

```java
package com.company.myapp.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = "/login",
    initParams = {
        @WebInitParam(name = "maxLoginAttempts", value = "3")
    }
)
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private int maxLoginAttempts;

    @Override
    public void init() throws ServletException {
        // Read this Servlet's own private init-param, set once at startup
        String value = getServletConfig().getInitParameter("maxLoginAttempts");
        maxLoginAttempts = Integer.parseInt(value);
        System.out.println("LoginServlet configured with maxLoginAttempts = " + maxLoginAttempts);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("Max login attempts allowed: " + maxLoginAttempts);
    }
}
```

**Explanation:**
- `getServletConfig()` — inherited from `GenericServlet` (Topic 1, Step 6.1's hierarchy) — returns the `ServletConfig` object the container populated during `init(ServletConfig config)`.
- **This `maxLoginAttempts` value is invisible to any other Servlet.** If you had a `RegisterServlet` in the same application, it has no way to read `LoginServlet`'s init-param — this is the essence of "private, per-Servlet scope."

---

## ServletContext — Deep Dive

### What it represents

A **single object, shared across the entire web application** — one instance per deployed app, created when the application starts (before any Servlet's `init()` runs, actually — the context exists before individual servlets do, which is why Listeners, can use it during `contextInitialized()`).

It serves **two distinct purposes**, both important:

**Purpose 1: Application-wide configuration** (read-only from your code's perspective, set via `<context-param>`)
**Purpose 2: Application-wide shared storage** (read/write, via `setAttribute()`/`getAttribute()` — a genuine shared data store, unlike `<context-param>` which is fixed at deployment time)

### Key methods

| Method | Returns | Purpose |
|---|---|---|
| `getInitParameter(String name)` | `String` | Reads a `<context-param>` value (app-wide config, read-only at runtime) |
| `getAttribute(String name)` | `Object` | Reads a shared attribute (app-wide storage, read/write at runtime) |
| `setAttribute(String name, Object value)` | `void` | Writes/overwrites a shared attribute |
| `removeAttribute(String name)` | `void` | Removes a shared attribute |
| `getContextPath()` | `String` | Returns the app's context path (e.g., `/myapp`) |
| `getRealPath(String path)` | `String` | Converts a web-relative path (e.g., `/WEB-INF/data.txt`) into an actual absolute filesystem path on the server |
| `log(String msg)` | `void` | Writes a message to the container's log file — a container-aware alternative to `System.out.println()` |

### Code Example — Demonstrating Both Purposes

**Purpose 1 — reading app-wide config (`<context-param>`):**

```xml
<!-- in web.xml -->
<context-param>
    <param-name>appName</param-name>
    <param-value>Student Management System</param-value>
</context-param>
```

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String appName = getServletContext().getInitParameter("appName");
    response.getWriter().println("Welcome to " + appName);
}
```

**Purpose 2 — shared storage across requests AND across different Servlets (the genuinely powerful part):**

```java
@WebServlet("/visit-counter")
public class VisitCounterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();

        // Read the current count (may be null on very first visit)
        Integer count = (Integer) context.getAttribute("visitCount");
        if (count == null) {
            count = 0;
        }

        count = count + 1;

        // Write it back — visible to EVERY user and EVERY Servlet in this app
        context.setAttribute("visitCount", count);

        response.setContentType("text/plain");
        response.getWriter().println("Total visits across all users: " + count);
    }
}
```

**Critical distinction to internalize:** This `visitCount` is shared across **every single user** hitting this application — unlike `HttpSession` (upcoming topic), which is per-*individual-user*. If User A visits and sees "Total visits: 5," and User B visits a second later, they'll see "Total visits: 6" — the **same counter**, shared globally.

**This also demonstrates something important connecting back to Thread Safety (upcoming dedicated topic):** Since `ServletContext` is a **single shared object accessed by potentially many concurrent threads across many different Servlets**, the read-modify-write pattern above (`getAttribute` → increment → `setAttribute`) is **NOT thread-safe** — two simultaneous requests could both read `count = 5`, both increment to `6`, and both write back `6`, **losing one increment**. We'll formally fix this with `synchronized` blocks in the dedicated Thread Safety/Synchronization topic.

---

## ServletConfig vs. ServletContext — Definitive Comparison Table

| Aspect | ServletConfig | ServletContext |
|---|---|---|
| Scope | One per Servlet | One per entire web application |
| Created when | Just before that Servlet's `init()` is called | When the application starts (before any Servlet initializes) |
| Holds | That Servlet's own `<init-param>` values | App-wide `<context-param>` values + shared attributes |
| Visible to | Only that one Servlet | Every Servlet, Filter, Listener, and JSP in the app |
| Read/write attributes? | No — `ServletConfig` has no `setAttribute()`/`getAttribute()` at all | Yes — full read/write shared storage |
| How to obtain | `getServletConfig()` (inside a Servlet) | `getServletContext()` (inside a Servlet), or `config.getServletContext()` (from a `ServletConfig`), or `session.getServletContext()` (from a session) |
| Real-world analogy | Personal work badge | Company bulletin board |

**A subtlety worth flagging precisely:** `ServletConfig` does **not** have `getAttribute()`/`setAttribute()` methods at all — only `getInitParameter()`. If you need Servlet-specific *mutable* shared state, you'd use an **instance variable** on that Servlet (with the thread-safety caveats from Topic 2), not `ServletConfig`. `ServletConfig` is purely for reading fixed, deployment-time configuration.

---

## EXECUTION FLOW — Where These Objects Fit in the Bigger Picture

```
Application Startup
        │
        ▼
Single ServletContext object created for the entire app
        │  (populated from all <context-param> entries in web.xml)
        ▼
[Listeners' contextInitialized() fires here — next topic]
        │
        ▼
For EACH Servlet with load-on-startup (or on first request, if lazy):
        │
        ▼
   A dedicated ServletConfig object created for THIS Servlet
   (populated from THIS Servlet's own <init-param> entries,
    plus a reference to the single shared ServletContext)
        │
        ▼
   init(ServletConfig) called → your init() runs
        │
        ▼
[Servlet now handles requests — can call getServletConfig()
 for its own params, or getServletContext() to reach the
 shared, application-wide object]
```

**Key mental model:** There is exactly **ONE `ServletContext`** for your whole app, but potentially **MANY `ServletConfig`** objects — one per Servlet. Every `ServletConfig` object, however, holds a reference back to that **same single** `ServletContext` — this is how a Servlet can access both its own private config and the shared app-wide context through one unified starting point.

---

## COMMON ERRORS

**Error: `NullPointerException` when reading a context-param**
```java
String appName = getServletContext().getInitParameter("appName");
appName.length(); // NPE if "appName" wasn't actually declared in web.xml
```
- **Cause:** Same root cause as `request.getParameter()` (Topic 1, Step 7.8) — `getInitParameter()` returns `null` if the key doesn't exist, it never throws an exception for a missing key.
- **Fix:** Always null-check, and double-check the `<param-name>` string matches exactly (case-sensitive) between `web.xml` and your `getInitParameter()` call.

**Error: `ClassCastException` when reading a ServletContext attribute**
```java
Integer count = (Integer) context.getAttribute("visitCount"); 
// throws ClassCastException if something else stored a String under that same key
```
- **Cause:** `ServletContext.getAttribute()` returns type `Object` (since it's a generic shared storage mechanism that can hold anything) — you must cast it to the expected type, and if some other part of the codebase mistakenly stored a different type under the same attribute name, you get a runtime `ClassCastException`.
- **Fix:** Use consistent, well-documented attribute-naming conventions across your team/codebase (a common real convention: prefix attribute keys with their purpose/type, e.g., `"app.visitCount"`), and be disciplined about which class "owns" writing to which attribute key.

**Error: Confusing ServletConfig's init-param with ServletContext's context-param and reading from the wrong one**
```java
// In web.xml, "timeout" is declared as a <context-param>, not a per-servlet <init-param>
String timeout = getServletConfig().getInitParameter("timeout"); // returns null!
```
- **Cause:** Beginners frequently mix up which object to query — `<context-param>` values are read via `getServletContext().getInitParameter(...)`, **not** `getServletConfig().getInitParameter(...)`, even though both methods share the identical name `getInitParameter()`.