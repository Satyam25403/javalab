# TOPIC 1 (continued): What is a Servlet — Internal Working

## STEP 2 — INTERNAL WORKING

This is where we go under the hood and understand *exactly* what Tomcat does, in what order, and why.

### 1. Class Loading

When Tomcat starts up (or on first request, depending on `load-on-startup` configuration — covered in detail in `web.xml` topic later), it needs to load your Servlet class into memory.

Here's the precise sequence:

1. Tomcat's internal **ClassLoader hierarchy** locates your compiled `.class` file. Every web application deployed on Tomcat has its own **WebappClassLoader**, which is a child of Tomcat's **Common ClassLoader**, which is a child of the JVM's **System/Application ClassLoader**, which is a child of the **Bootstrap ClassLoader**.

```
Bootstrap ClassLoader (loads core java.* classes)
        │
        ▼
Extension/Platform ClassLoader
        │
        ▼
System/Application ClassLoader (loads Tomcat's own classes)
        │
        ▼
Common ClassLoader (shared libraries across all web apps)
        │
        ▼
WebappClassLoader (unique per deployed web application)
        │
        ▼
   Your Servlet Class
```

2. **Why does each webapp get its own ClassLoader?** This is a critical architectural decision. It provides **isolation** — if you deploy two different web applications (WAR files) on the same Tomcat instance, each can have different versions of the same library (e.g., App A uses Gson 2.8, App B uses Gson 2.10) without conflict, because each app's classes are loaded by a separate WebappClassLoader instance. This also means a class loaded for one webapp is **not visible** to another webapp — true isolation.

3. Once located, the ClassLoader reads the `.class` file's bytecode, verifies it (bytecode verification — ensures no corrupted or malicious bytecode), and defines the `Class` object in the JVM's method area (part of JVM memory, sometimes called Metaspace in modern JVMs).

### 2. Object Creation (Instantiation)

- Tomcat uses **Java Reflection** internally (`Class.forName()` followed by `newInstance()` or, in modern implementations, `getDeclaredConstructor().newInstance()`) to create the Servlet object.
- **Critically: this happens only ONCE per Servlet class**, not once per request. This is the single most important internal fact about Servlets.
- The Servlet must have a **public no-argument constructor** (either explicitly defined or the default one provided by Java) because the container instantiates it via reflection without knowing any custom constructor arguments.

### 3. Container Behavior

The **Servlet Container** (Tomcat, in our case) is responsible for a well-defined contract, based on the Servlet interface (`jakarta.servlet.Servlet`). The container guarantees:

- It will call `init(ServletConfig config)` exactly once, before the servlet handles any requests.
- It will call `service(ServletRequest req, ServletResponse res)` for every incoming request, only after `init()` has completed successfully.
- It will call `destroy()` exactly once, when the servlet is being taken out of service (e.g., app shutdown, redeployment).
- It will manage **thread allocation** — assigning a thread from its internal thread pool to handle each incoming request's `service()` call.

This is called the **Servlet Lifecycle**, and it is entirely container-managed. You, the developer, **never call `init()`, `service()`, or `destroy()` directly** — that would violate the container's contract and break the architecture. This is a concrete example of the **Inversion of Control (IoC)** principle — the same principle that later becomes the philosophical foundation of the Spring Framework. (Important connection for your future Spring learning — file this away.)

### 4. Memory Usage

- **Single Servlet object** lives in the **Heap memory** of the JVM, in an area shared across all threads.
- Because only **one instance** exists (by default — this is called the **Singleton-like model**, though technically not a Java "Singleton pattern" implementation, just a single-instance-per-container-per-webapp behavior), memory footprint per Servlet is minimal.
- However, every **incoming request/response pair** creates *new* `HttpServletRequest` and `HttpServletResponse` objects — these are **not shared**; they are created freshly for each request and are thread-local in the sense that each thread gets its own request/response object pair.
- **Local variables** declared inside `doGet()`/`doPost()` live on the **stack** of the specific thread executing that method call — this is why local variables are inherently thread-safe (each thread has its own stack), while **instance variables** live on the **heap**, shared by all threads calling methods on that object — this is why instance variables are **not** thread-safe by default.

### 5. Lifecycle (Summary — detailed breakdown comes in the dedicated "Servlet Lifecycle" topic)

```
   Class Loading
        │
        ▼
   Object Instantiation (via reflection) — ONCE
        │
        ▼
   init(ServletConfig) — ONCE
        │
        ▼
   ┌─────────────────────────────┐
   │   service() — MANY TIMES     │  <── one call per HTTP request
   │   (routes to doGet/doPost)   │
   └─────────────────────────────┘
        │
        ▼
   destroy() — ONCE (on shutdown/undeploy)
        │
        ▼
   Object eligible for Garbage Collection
```

### 6. Execution Flow (Request → Response, step by step)

Let's trace **exactly** what happens when a browser sends a request to a URL mapped to a Servlet:

1. Browser sends an HTTP request, e.g., `GET /myapp/hello`.
2. Request reaches Tomcat's **Connector** component (listens on a port, e.g., 8080, handles raw socket/TCP communication, parses raw HTTP into a request object).
3. Tomcat's **Engine → Host → Context → Wrapper** pipeline (Tomcat's internal component hierarchy) determines which **web application (Context)** this URL belongs to, based on the context path (`/myapp`).
4. Within that Context, Tomcat's internal **Mapper** component matches the remaining URL path (`/hello`) against the registered **URL patterns** (from `@WebServlet` annotations or `web.xml` `<servlet-mapping>`) to determine **which specific Servlet** should handle this request.
5. Tomcat checks: is this Servlet instance already created? If not: class loading → instantiation → `init()` (as described above).
6. Tomcat's thread pool (by default, the "Executor" — a pool of worker threads) grabs an **available thread** and assigns this request to it.
7. That thread creates a new `HttpServletRequest` object (wrapping all data: headers, parameters, body, cookies, session info) and a new `HttpServletResponse` object (initially empty, to be filled in).
8. The thread calls `servlet.service(request, response)` on the **shared Servlet instance**.
9. The default `service()` implementation (inherited from `HttpServlet`) internally checks the HTTP method (GET, POST, PUT, DELETE, etc.) and delegates to the corresponding method: `doGet()`, `doPost()`, etc.
10. Your overridden `doGet()`/`doPost()` method executes your business logic — reads request parameters, talks to a database (JDBC), computes a result.
11. You write output either directly via `response.getWriter().println(...)` or (more commonly, and better practice) forward the request to a JSP for rendering via `RequestDispatcher`.
12. Once your method finishes execution, control returns to the container.
13. The container flushes the response buffer, sends the complete HTTP response (status line, headers, body) back through the Connector, over the TCP socket, to the browser.
14. The thread is **released back to the thread pool** — it does NOT die; it becomes available to handle the next incoming request (possibly for a completely different Servlet).
15. Browser receives the response and renders it.

### 7. Thread Behavior & Multi-user Handling

This is one of the most commonly misunderstood — and most commonly *asked in interviews* — concepts, so pay close attention:

- **One Servlet object. Many threads. Concurrent execution.**
- When 100 users hit the same Servlet URL simultaneously, Tomcat does **NOT** create 100 Servlet objects. It creates (or reuses from the pool) **100 threads**, and all 100 threads call `service()` on the **exact same Servlet instance**, concurrently.
- This is why the Servlet specification explicitly warns: **Servlets must be thread-safe**, because multiple threads execute the *same* object's methods at the *same* time.
- **Local variables are safe** — each thread has its own stack frame for the method call, so local variables inside `doGet()` don't clash between threads.
- **Instance variables are dangerous** — if you declare `private int counter;` as an instance variable and increment it inside `doGet()` without synchronization, you get a **race condition**, because multiple threads read/modify/write that same shared variable concurrently, and updates can be lost or corrupted.
- **Static variables are even more dangerous** in a sense — they're shared not just across threads but potentially across multiple Servlets in the same JVM (though scoped per ClassLoader, so per-webapp usually).

This directly maps to the **"Thread Safety" and "Synchronization"** sub-topics listed later in your Module 1 outline — we'll go very deep there with real race-condition code demos.

### 8. Compilation Process

- Your `.java` Servlet source file is compiled by `javac` (or your IDE/Maven build) into a `.class` bytecode file **before deployment** — unlike JSP, which is compiled **on the fly by Tomcat** (we'll cover this distinction in depth in Module 2).
- The compiled `.class` file must be placed inside `WEB-INF/classes/<package-structure>/` (or bundled inside a JAR under `WEB-INF/lib/` if it's part of a library) — this is part of the mandatory Servlet directory structure we'll cover in Step 3.

### 9. Container Responsibilities vs Developer Responsibilities

| Container's Responsibility (Tomcat) | Developer's Responsibility (You) |
|---|---|
| Load the Servlet class | Write correct business logic |
| Create the Servlet instance | Ensure thread safety (no unsafe instance variables) |
| Call init(), service(), destroy() at the right time | Override doGet()/doPost()/etc. correctly |
| Manage thread pooling and concurrency | Handle exceptions gracefully |
| Parse raw HTTP into Request/Response objects | Validate input from request parameters |
| Route requests to correct Servlet (URL mapping) | Configure correct URL patterns |
| Manage session objects (HttpSession) | Use session appropriately (don't overload it) |
| Handle connection/socket level networking | Close resources (DB connections, streams) properly |

