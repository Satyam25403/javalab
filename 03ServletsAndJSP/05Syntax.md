# TOPIC 1 (continued): Syntax

## STEP 6 — SYNTAX

This is the last purely theoretical step before we write actual code in Step 7. Here we dissect the **`HttpServlet` class itself** — every method, every parameter, every return type, every exception — so that when you see code in Step 7, nothing is unfamiliar.

---

### 6.1 — The Servlet Interface Hierarchy

Before looking at `HttpServlet`, you must understand the **inheritance chain** it comes from, because this explains *why* certain methods exist where they do:

```
        jakarta.servlet.Servlet                  (interface)
                    │
                    │ implements
                    ▼
        jakarta.servlet.GenericServlet            (abstract class)
                    │
                    │ extends
                    ▼
        jakarta.servlet.http.HttpServlet          (abstract class)
                    │
                    │ extends
                    ▼
              YOUR SERVLET CLASS
        (e.g., HelloServlet, LoginServlet)
```

**Why this three-layer design?**

- **`Servlet` (interface)** — Defines the absolute minimum contract every Servlet must fulfill: `init()`, `service()`, `destroy()`, `getServletConfig()`, `getServletInfo()`. This interface is **protocol-agnostic** — theoretically, Servlets could handle protocols other than HTTP (this was a real design goal, though HTTP dominated in practice).

- **`GenericServlet` (abstract class)** — Provides a **default, protocol-independent implementation** of the `Servlet` interface. It implements `init(ServletConfig)` for you (storing the config object) and implements `getServletConfig()`, `getServletInfo()`, `log()` as convenience methods. It leaves `service()` abstract — you'd have to implement it yourself if extending `GenericServlet` directly.

- **`HttpServlet` (abstract class)** — Extends `GenericServlet` and adds **HTTP-specific behavior**. It provides a **concrete implementation of `service()`** that automatically inspects the incoming HTTP method (GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE) and **delegates** to the corresponding `doXxx()` method. This is the class **you will always extend** in real Servlet development — you almost never extend `GenericServlet` directly in HTTP-based web applications.

**Why does this matter?** Understanding this hierarchy answers a very common interview question: *"Why do we override `doGet()`/`doPost()` instead of `service()` directly?"* — Answer: because `HttpServlet.service()` is already implemented for you, correctly handling method dispatch, `HEAD` requests specially (auto-generating a `GET` response without body), and `OPTIONS`/`TRACE` handling. Overriding `service()` yourself would mean **losing** all that built-in correct behavior and reimplementing HTTP semantics manually — a bad practice.

---

### 6.2 — Complete Method Signatures of `HttpServlet`

Let's go through **every method** you'll actually override or call, with full explanation.

```java
public abstract class HttpServlet extends GenericServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { ... }

    // Inherited from GenericServlet:
    public void init(ServletConfig config) throws ServletException { ... }
    public void init() throws ServletException { ... }  // convenience overload
    public void destroy() { ... }
    public ServletConfig getServletConfig() { ... }
    public String getServletInfo() { ... }
}
```

---

### 6.3 — Keyword-by-Keyword and Parameter-by-Parameter Explanation

**`protected` (access modifier on doGet/doPost/etc.)**
- Why `protected`, not `public`? Because these methods are meant to be called **only by the container** (via `service()`, which itself is called by the container's request-handling machinery) or by subclasses — **not** to be invoked directly by arbitrary external code. Making them `protected` enforces the architectural rule: *"You don't call these; the container calls these."* This is a textbook example of the **Template Method design pattern** — `HttpServlet` defines the skeleton algorithm (`service()` deciding which `doXxx()` to call), while subclasses fill in the specific steps.

**`void` (return type)**
- These methods don't *return* the response as a value — instead, they **write** the response directly onto the `HttpServletResponse` object passed in as a parameter (via its output stream/writer). This is a **side-effect-based** design rather than a return-value-based design, which makes sense because HTTP responses involve headers, status codes, AND body content — too much to elegantly bundle into a single return value.

**`HttpServletRequest req` (first parameter)**
- An object representing the **entire incoming HTTP request** — method type, URL, headers, query parameters, form data, cookies, session, request body, client IP address, and more. Created **fresh for every request** by the container (not shared/reused across requests) — this is why it's always safe to use as a local reference inside your method without thread-safety concerns.
- We'll cover its full method list (`getParameter()`, `getHeader()`, `getSession()`, etc.) in depth in the dedicated **HttpServletRequest** topic.

**`HttpServletResponse resp` (second parameter)**
- An object representing the **outgoing HTTP response** you're building — you use it to set status codes, headers, content type, and write the actual response body (HTML/JSON/etc.) via `resp.getWriter()` or `resp.getOutputStream()`.
- Also created fresh per request by the container.

**`throws ServletException, IOException` (checked exceptions)**
- **`ServletException`** — A general-purpose exception indicating something went wrong **within Servlet processing logic** — e.g., a downstream component failure, a configuration problem, or you explicitly throw it to signal an application-level error to the container (which then typically triggers your configured `<error-page>` for exception types, from Step 5).
- **`IOException`** — Thrown when reading from the request stream or writing to the response stream fails at the I/O level (e.g., client disconnected mid-request, network failure). This comes from the fact that request/response bodies are fundamentally **streams**, and all Java stream I/O operations can throw `IOException`.
- Both are **checked exceptions**, meaning the compiler forces you to either handle them (`try-catch`) or declare them in your method signature (`throws`). Since the container itself knows how to handle these (typically by triggering error pages or logging), it's standard practice to simply declare `throws ServletException, IOException` on your overridden methods and let the container deal with the failure, rather than swallowing them silently.

---

### 6.4 — `service()` Method — The Dispatcher

```java
protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String method = req.getMethod();  // e.g., "GET", "POST"

    if (method.equals("GET")) {
        doGet(req, resp);
    } else if (method.equals("POST")) {
        doPost(req, resp);
    } else if (method.equals("PUT")) {
        doPut(req, resp);
    } else if (method.equals("DELETE")) {
        doDelete(req, resp);
    }
    // ... and so on for HEAD, OPTIONS, TRACE
}
```

*(This is conceptually how `HttpServlet`'s internal `service()` works — the actual Apache Tomcat source is more detailed, handling `HEAD` specially by wrapping the response to discard the body while still setting `Content-Length`, and auto-generating `OPTIONS` responses listing supported methods — but this simplified version captures the essential dispatching logic you need to understand.)*

**Key insight:** This is *exactly* why, if a client sends a `GET` request but your Servlet **only overrides `doPost()`** (leaving `doGet()` un-overridden), you get an **HTTP 405 Method Not Allowed** error. The default `doGet()` implementation inherited from `HttpServlet` (which you didn't override) simply responds with a 405 status and a message like "HTTP method GET is not supported by this URL." This is one of the most common beginner errors and a favorite exam/interview trick question.

---

### 6.5 — `init()` — Two Overloaded Versions (Important Detail)

```java
public void init(ServletConfig config) throws ServletException {
    this.config = config;   // stores config internally (done by GenericServlet)
    init();                 // calls the no-arg version
}

public void init() throws ServletException {
    // empty by default — YOU override THIS one typically
}
```

**Why two versions?** This is a subtle but important design choice (asked in advanced viva questions):
- The **container** always calls `init(ServletConfig config)` (the one-argument version).
- `GenericServlet`'s implementation of that method **stores** the `ServletConfig` object into an internal field (so `getServletConfig()` works later) and then **calls the no-argument `init()`**.
- **You, the developer,** are expected to override the **no-argument `init()`** for your custom initialization logic (like opening a DB connection pool), **not** the one-argument version — because if you override the one-argument version and forget to call `super.init(config)`, the `ServletConfig` object never gets stored, and `getServletConfig()` will return `null`, causing `NullPointerException`s later. Overriding the no-arg version avoids this pitfall entirely, since the config-storing step happens safely in the parent class before your code even runs.

**`ServletConfig` parameter** — represents configuration **specific to this one Servlet** (its `<init-param>` values, its `<servlet-name>`, and a reference to the shared `ServletContext`). Contrast this with `ServletContext` (application-wide), covered in depth in its own dedicated topic later in Module 1.

**`throws ServletException`** — if initialization fails critically (e.g., a required config value is missing), you can throw `ServletException` from `init()`, which signals to the container that this Servlet failed to initialize — the container will then typically refuse to route any requests to it and may log/report the startup failure.

---

### 6.6 — `destroy()`

```java
public void destroy() {
    // no exception declared, no parameters
}
```

- Called **exactly once**, when the container is shutting down the Servlet (application undeployment, server shutdown, or redeployment during development).
- **No `throws` clause** — by design, cleanup code shouldn't be throwing checked exceptions that the container would need to handle mid-shutdown; you're expected to handle any cleanup errors internally (try-catch) rather than propagate them.
- Typical use: closing database connection pools, releasing file handles, stopping background threads that the Servlet may have started in `init()`.

---

### 6.7 — Full Method Reference Table

| Method | Called By | Called When | Overridden By You? |
|---|---|---|---|
| `init(ServletConfig)` | Container | Once, before first use | Rarely (only if you skip calling super) |
| `init()` | `init(ServletConfig)` internally | Same time as above | **Yes**, for custom startup logic |
| `service(req, resp)` | Container | Every request | **No** — let HttpServlet's dispatch logic work |
| `doGet(req, resp)` | `service()` | Every GET request | **Yes**, for GET logic |
| `doPost(req, resp)` | `service()` | Every POST request | **Yes**, for POST logic |
| `doPut/doDelete(req, resp)` | `service()` | PUT/DELETE requests | Yes, if building REST-style endpoints |
| `destroy()` | Container | Once, during shutdown | **Yes**, for cleanup logic |
| `getServletConfig()` | You (or container) | Anytime after init | No — inherited, just call it |
| `getServletContext()` | You | Anytime | No — inherited, just call it |

