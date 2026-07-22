# TOPIC 2: Servlet Lifecycle (init, service, destroy — Formal Deep Dive)

## CONCEPT

### Why this concept exists

You've already seen the lifecycle informally scattered across Steps 1, 2, and 6. Now we formalize it as its own topic because **the Servlet Lifecycle is the single most-tested concept in every Java backend interview** — it's the mechanism that makes Servlets fundamentally different from a plain Java class you instantiate yourself.

The core idea: **you never call `new LoginServlet()` anywhere in your code.** The container does it, exactly once, and manages three well-defined phases afterward. This is a concrete instance of **Inversion of Control (IoC)** — control over object creation and method invocation is inverted from "you call it" to "the framework/container calls it." This term — it becomes the **entire foundation of the Spring Framework** later.

### The Three Phases

```
┌─────────────────────────────────────────────────────────────┐
│                                                                │
│   PHASE 1: INSTANTIATION + INITIALIZATION (happens ONCE)     │
│   ─────────────────────────────────────────────────────      │
│   1. Class loaded by WebappClassLoader                        │
│   2. Object created via reflection (no-arg constructor)       │
│   3. init(ServletConfig) called → stores config → calls init()│
│                                                                │
├─────────────────────────────────────────────────────────────┤
│                                                                │
│   PHASE 2: REQUEST HANDLING (happens MANY TIMES)              │
│   ─────────────────────────────────────────────────          │
│   For EVERY incoming request:                                 │
│   4. New thread assigned from pool                            │
│   5. New HttpServletRequest/Response objects created           │
│   6. service() called → dispatches to doGet()/doPost()/etc.  │
│   7. Thread returned to pool after response sent               │
│                                                                │
├─────────────────────────────────────────────────────────────┤
│                                                                │
│   PHASE 3: DESTRUCTION (happens ONCE)                          │
│   ─────────────────────────────────                           │
│   8. Container decides to remove Servlet (shutdown/redeploy)   │
│   9. destroy() called                                          │
│   10. Object becomes eligible for garbage collection            │
│                                                                │
└─────────────────────────────────────────────────────────────┘
```

### Real-world analogy (extending the restaurant chef analogy from Topic 1)

- **Phase 1 (Instantiation)** = Hiring the chef and letting them set up their station — sharpening knives, prepping their workspace **once**, before any customer orders arrive.
- **Phase 2 (Request Handling)** = The chef cooking dish after dish after dish, for hundreds of different customers, **without re-setting-up their station each time**.
- **Phase 3 (Destruction)** = The restaurant closing down permanently (or the chef being let go) — cleaning the station, turning off the stove, **once**, at the very end.

### When exactly does Phase 1 trigger? — `load-on-startup` revisited

This connects directly back to Step 5's `<load-on-startup>` tag:

| Configuration | When Phase 1 (instantiation + init) happens |
|---|---|
| No `load-on-startup` specified (default) | **Lazily** — on the **first** request that matches this Servlet's URL pattern |
| `load-on-startup` present (e.g., `1`) | **Eagerly** — immediately when the web application starts, before any request arrives |

This is a genuinely important **design decision** you'll make in real projects: eager loading means slightly slower app startup but faster first-request response (no user experiences the one-time `init()` delay); lazy loading means faster startup but the *unlucky first user* to hit that Servlet experiences the initialization delay.

### Advantages of this lifecycle model

1. **Performance** — object creation cost paid once, not per request (core motivation from Topic 1).
2. **Resource efficiency** — expensive setup (DB connection pools, configuration loading, caching static data) happens once in `init()`, reused across thousands of requests.
3. **Predictability** — the container guarantees strict ordering (`init()` always fully completes before any `service()` call is made on that instance), so you never have to worry about a request arriving before your Servlet is ready.

### Disadvantages / things to watch out for

1. **Shared mutable state danger** — since Phase 2 involves *many threads* calling `service()` concurrently on the *same* object created in Phase 1, any instance variable you set up carelessly becomes a thread-safety hazard (Topic covered formally later as "Thread Safety").
2. **`init()` failure handling** — if `init()` throws a `ServletException`, the Servlet is marked **unavailable**, and the container will not route requests to it (may return 404 or 500 depending on container behavior) — you must handle initialization failures carefully, especially for things like failed DB connections during startup.

### Best Practices

1. Use `init()` for read-only, expensive, one-time setup (e.g., loading a properties file, initializing a connection pool reference — actual pool creation is usually better delegated to `ServletContextListener`, covered in the Listeners topic later).
2. Never use `init()` to set up request-specific data — it runs once, long before any specific request exists.
3. Always release resources acquired in `init()` inside `destroy()` — this is the symmetric cleanup pairing.

---

## INTERNAL WORKING

### `init(ServletConfig config)` — precise internal sequence

```java
// This is (conceptually) what GenericServlet does internally:
public void init(ServletConfig config) throws ServletException {
    this.servletConfig = config;   // stores config as instance field
    this.init();                    // calls YOUR overridden no-arg version
}
```

As established in Topic 1 Step 6.5: **you override the no-arg `init()`**, not this one, to avoid accidentally losing the `ServletConfig` reference.

**Example — real use case using `init()`:**

```java
package com.company.myapp.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = "/greet",
    initParams = @jakarta.servlet.annotation.WebInitParam(name = "greetingPrefix", value = "Hello")
)
public class GreetingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Instance variable — safe here because it's set ONCE in init()
    // and NEVER modified afterward (read-only during Phase 2)
    private String greetingPrefix;

    @Override
    public void init() throws ServletException {
        // Runs ONCE, before any request is handled
        greetingPrefix = getServletConfig().getInitParameter("greetingPrefix");
        System.out.println("GreetingServlet initialized with prefix: " + greetingPrefix);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h2>" + greetingPrefix + ", World!</h2>");
    }

    @Override
    public void destroy() {
        // Runs ONCE, on shutdown
        System.out.println("GreetingServlet is being destroyed. Cleaning up...");
    }
}
```

**Why is `greetingPrefix` safe as an instance variable here, but a request counter wouldn't be?**

This is the critical distinction, and a favorite viva/interview question:

| Instance variable usage | Thread-safe? | Why |
|---|---|---|
| `greetingPrefix` — set once in `init()`, never modified after | **Yes** | All threads only **read** it during Phase 2; no thread ever writes to it after initialization — no race condition possible |
| `private int requestCount = 0;` incremented inside `doGet()` | **No** | Multiple threads **read AND write** this same field concurrently during Phase 2 → classic race condition, lost updates |

**Rule of thumb:** An instance variable is safe **only if it becomes effectively immutable/read-only after `init()` completes.** Anything mutated per-request must be a **local variable** inside the method, never an instance field.

### `getServletConfig().getInitParameter(...)` — internal mechanics

- `ServletConfig` is created by the container **specifically for this one Servlet**, populated from that Servlet's `<init-param>` (web.xml) or `initParams` (annotation) entries — established in Topic 1 Step 5.2/5.3.
- Internally, it's backed by a simple key-value structure (conceptually a `Map<String,String>`), read-only from your code's perspective — you can only *read* init parameters, never set new ones at runtime.

### `service()` internal dispatch — already covered in Topic 1 Step 6.4, not repeating here.

### `destroy()` — precise timing

The container calls `destroy()` under these circumstances:
1. **Application undeployment** — you stop/remove the app from Tomcat.
2. **Server shutdown** — Tomcat itself is stopping.
3. **Redeployment during development** — Eclipse republishing your app after a code change (this is why you'll see `init()`/`destroy()` console messages repeatedly while developing — each republish destroys the old instance and creates a fresh one).

**Critical guarantee:** The container will **not** call `destroy()` while any thread is still inside `service()` for that Servlet — it waits for in-flight requests to complete first (a **graceful shutdown** guarantee), then calls `destroy()` once, then discards the instance.

---

## EXECUTION FLOW — Lifecycle Timeline for `GreetingServlet`

```
[App Startup or First Request to /greet]
        │
        ▼
Class loading (WebappClassLoader loads GreetingServlet.class)
        │
        ▼
Object instantiation (reflection, no-arg constructor)
        │
        ▼
init(ServletConfig) called
        │  → stores config
        │  → calls init() [your override]
        │  → greetingPrefix = "Hello"
        │  → console prints "GreetingServlet initialized..."
        ▼
[Servlet now READY — Phase 1 complete]
        │
        ├──► Request 1 (Thread A) → service() → doGet() → reads greetingPrefix → writes response
        ├──► Request 2 (Thread B) → service() → doGet() → reads greetingPrefix → writes response
        ├──► Request 3 (Thread A, reused) → service() → doGet() → ... 
        │      (this repeats for the entire app lifetime — SAME object, MANY threads/requests)
        │
        ▼
[App Shutdown / Undeploy]
        │
        ▼
destroy() called
        │  → console prints "GreetingServlet is being destroyed..."
        ▼
Object eligible for garbage collection
```

---

## COMMON ERRORS (Lifecycle-Specific)

**Error: `init()` silently not running your setup logic**
- **Cause:** You overrode `init(ServletConfig config)` (the one-argument version) instead of `init()`, and **forgot to call `super.init(config)`** inside it.
- **Symptom:** `getServletConfig()` returns `null` later, causing `NullPointerException` when you try to read init parameters.
- **Fix:** Always override the **no-argument** `init()` unless you have a specific advanced reason not to; if you must override the one-arg version, always call `super.init(config)` as the very first line.

**Error: Stale data after code changes during development**
- **Cause:** You changed an `init()`-loaded value (like a hardcoded config), but Eclipse didn't trigger a full republish, so the old Servlet instance (with old `init()`-loaded state) is still running.
- **Fix:** Right-click server → **Clean...**, or **Restart** the server explicitly in Eclipse's Servers view, rather than relying on hot-redeployment for lifecycle-sensitive changes.

**Error: Exception thrown inside `init()`**
```
SEVERE: Servlet [GreetingServlet] threw exception during init
```
- **Cause:** Something in your `init()` logic failed (e.g., null init-param used before checking, or a resource genuinely unavailable at startup).
- **Effect:** This Servlet becomes **permanently unavailable** for the rest of the application's running lifetime (the container will not retry `init()` automatically) — requests to its URL typically result in a 404 or 500, depending on container version.
- **Fix:** Wrap risky `init()` logic in try-catch, log meaningfully, and consider whether the failure should truly prevent the whole Servlet from working, or whether you should degrade gracefully instead.

