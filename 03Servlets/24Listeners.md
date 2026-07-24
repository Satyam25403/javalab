# TOPIC 15: Listeners

## CONCEPT

### Why this concept exists

Filters (Topic 14) intercept **requests** as they flow through the application. But some things you need to react to **aren't requests at all** — the application **starting up**, the application **shutting down**, a session being **created**, a session **expiring**, an attribute being **added** to the context. **Listeners** exist to let you hook into these **lifecycle events**, which happen independently of any specific HTTP request.

### What problem it solves

Recall Topic 2's `init()` — it runs once per Servlet, but **only when that specific Servlet is loaded** (which, if lazy-loaded, could be arbitrarily late, or never, if that URL is never hit). If you need something to happen **the instant the entire application starts** — regardless of which Servlet a user happens to visit first — `init()` on an individual Servlet is the **wrong** tool. `ServletContextListener` is the **right** tool: it fires **exactly once, guaranteed, at application startup**, before any Servlet, before any request is even possible.

### Real-world analogy

If Filters are airport security checkpoints (Topic 14), Listeners are the **airport's opening and closing procedures** — turning on the lights, activating the runway systems, opening the gates each morning (application startup) — and the corresponding shutdown procedures each night (application shutdown). These happen **once**, on a schedule tied to the airport itself being open or closed, completely independent of any individual passenger (request) — in fact, they happen **before the first passenger can even arrive**, and **after the last one has left**.

---

## The Listener Interfaces — Categorized by What They Observe

The Servlet API defines several listener interfaces, grouped by which scope's events they observe:

| Interface | Observes events on | Key methods |
|---|---|---|
| `ServletContextListener` | Application scope (Topic 6, 7) | `contextInitialized()`, `contextDestroyed()` |
| `ServletContextAttributeListener` | Attribute changes in application scope | `attributeAdded()`, `attributeRemoved()`, `attributeReplaced()` |
| `HttpSessionListener` | Session scope (Topic 7, 11) | `sessionCreated()`, `sessionDestroyed()` |
| `HttpSessionAttributeListener` | Attribute changes in session scope | `attributeAdded()`, `attributeRemoved()`, `attributeReplaced()` |
| `ServletRequestListener` | Request scope (Topic 7) | `requestInitialized()`, `requestDestroyed()` |

**For this course, we focus on the two most practically important ones: `ServletContextListener` and `HttpSessionListener`** — these cover the vast majority of real-world listener use cases.

---

## `ServletContextListener` — Deep Dive

### Syntax

```java
public interface ServletContextListener extends EventListener {
    default void contextInitialized(ServletContextEvent sce) {}
    default void contextDestroyed(ServletContextEvent sce) {}
}
```

- `contextInitialized(ServletContextEvent sce)` — fires **exactly once**, when the application starts up, **before any Servlet's `init()` runs**.
- `contextDestroyed(ServletContextEvent sce)` — fires **exactly once**, when the application shuts down, **after** all Servlets' `destroy()` methods have completed.
- `ServletContextEvent` — a wrapper object whose `getServletContext()` method gives you access to the actual `ServletContext`, letting you store shared setup data (like a connection pool reference) as a context attribute for every Servlet to later retrieve.

### Code Example — The Connection-Pool-Initialization Pattern

```java
package com.company.myapp.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application starting up... initializing shared resources.");

        // Conceptual placeholder — actual DataSource/connection pool object
        // creation is covered fully in Module 3 (JDBC / Connection Pooling)
        String dbUrl = "jdbc:mysql://localhost:3306/mydb";

        // Store it in Application scope so EVERY Servlet can retrieve it later
        sce.getServletContext().setAttribute("dbUrl", dbUrl);

        System.out.println("Shared resources initialized. Application ready.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application shutting down... releasing shared resources.");
        // Real cleanup (closing a connection pool, etc.) happens here,
        // fully demonstrated once we reach Connection Pooling
    }
}
```

**Why `@WebListener` needs no path/URL pattern (unlike `@WebServlet`/`@WebFilter`):** Listeners don't respond to requests at specific URLs — they respond to **lifecycle events**, which are inherently application-wide, not URL-scoped. The annotation simply registers the class as a listener; the container calls its methods automatically at the appropriate lifecycle moments.

**Why this is the correct place for expensive one-time setup (rather than a Servlet's `init()`):** A Servlet's `init()` only runs when **that Servlet** is first loaded, or might never happen if a user never visits that specific URL. A `ServletContextListener`, by contrast, is **guaranteed** to run at application startup, **regardless of which Servlets ever get used** — making it the correct, reliable place for genuinely global setup like establishing a database connection pool that **every** part of your application will depend on.

---

## `HttpSessionListener` — Deep Dive

### Syntax

```java
public interface HttpSessionListener extends EventListener {
    default void sessionCreated(HttpSessionEvent se) {}
    default void sessionDestroyed(HttpSessionEvent se) {}
}
```

- `sessionCreated(HttpSessionEvent se)` — fires every time `request.getSession()` creates a **brand-new** session (i.e., whenever `session.isNew()` would be `true`).
- `sessionDestroyed(HttpSessionEvent se)` — fires when a session is invalidated **either** explicitly (`session.invalidate()`, Topic 11) **or** automatically due to timeout.

### Code Example — Tracking Active Users in Real Time

This is a genuinely useful, realistic pattern: maintaining a live count of currently active sessions, using `ServletContext` (application scope) as shared storage, updated by session lifecycle events:

```java
package com.company.myapp.listener;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounterListener implements HttpSessionListener {

    private static final AtomicInteger activeSessionCount = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int current = activeSessionCount.incrementAndGet();
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("activeSessionCount", current);
        System.out.println("Session created: " + se.getSession().getId() + ". Active sessions: " + current);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int current = activeSessionCount.decrementAndGet();
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("activeSessionCount", current);
        System.out.println("Session destroyed: " + se.getSession().getId() + ". Active sessions: " + current);
    }
}
```

**Why `AtomicInteger` instead of a plain `int` — a direct, concrete tie-in to the upcoming Thread Safety topic:**

 `sessionCreated()`/`sessionDestroyed()` can be called **concurrently**, from **different threads**, for different users' sessions being created/destroyed at the same moment. If we used a plain:
```java
private static int activeSessionCount = 0; // DANGEROUS
activeSessionCount++; // NOT thread-safe — read-modify-write race condition
```
...we'd have the **exact same race condition** flagged in Topic 6's visit counter example. `AtomicInteger` (from `java.util.concurrent.atomic`) provides `incrementAndGet()`/`decrementAndGet()` as genuinely **atomic operations** — the read-modify-write happens as one indivisible operation at the hardware/JVM level, completely eliminating the race condition **without** needing an explicit `synchronized` block. This is a preview of a proper, modern solution to the exact thread-safety problem we'll formalize fully in the dedicated Thread Safety/Synchronization topic .

**Any Servlet can now read this live count:**
```java
Integer activeSessions = (Integer) getServletContext().getAttribute("activeSessionCount");
```

---

## Ordering Guarantees — Listeners vs. Filters vs. Servlets at Startup

Consolidating what's been referenced across Topics 5, 6, and 14 into one clear, definitive sequence:

```
Application Deployment / Startup
        │
        ▼
1. ServletContext object created
        │
        ▼
2. ALL registered ServletContextListeners' contextInitialized() called
   (in the order they're declared/discovered — annotation scan order
    is not strictly guaranteed; web.xml <listener> order IS guaranteed
    and reliable if precise ordering matters)
        │
        ▼
3. Filters are initialized (init() called on each, once)
        │
        ▼
4. Servlets with <load-on-startup> are instantiated, init() called
   (lazy-loaded Servlets wait until their first matching request)
        │
        ▼
Application is now ready to accept requests
        │
        ▼
[For each request: Filters' doFilter() → Servlet's service() → doGet()/doPost()]
        │
        ▼
[Whenever a NEW session is created during any request: HttpSessionListener.sessionCreated() fires]
        │
        ▼
[Whenever a session times out or is invalidated: HttpSessionListener.sessionDestroyed() fires]
        │
        ▼
Application Shutdown / Undeploy
        │
        ▼
5. Servlets' destroy() called
        │
        ▼
6. Filters' destroy() called
        │
        ▼
7. ALL ServletContextListeners' contextDestroyed() called (reverse order of initialization)
```

---

## EXECUTION FLOW — Combining Everything From This Topic

```
[Tomcat starts, deploys myapp]
        │
        ▼
AppContextListener.contextInitialized() fires
   → prints "Application starting up..."
   → sets "dbUrl" as a ServletContext attribute
        │
        ▼
[App fully deployed, ready for requests]
        │
        ▼
User A visits /login, submits credentials successfully
   → LoginServlet calls request.getSession() → NEW session created
        │
        ▼
SessionCounterListener.sessionCreated() fires automatically
   → activeSessionCount becomes 1
   → prints "Session created: ABC123. Active sessions: 1"
        │
        ▼
User B visits /login, also logs in successfully → another new session
        │
        ▼
SessionCounterListener.sessionCreated() fires again
   → activeSessionCount becomes 2
        │
        ▼
[30 minutes pass, User A's session times out due to inactivity (Topic 11)]
        │
        ▼
SessionCounterListener.sessionDestroyed() fires AUTOMATICALLY
   (container calls this — no request needed to trigger it; a background
    reaper thread detects the timeout and invalidates the session itself)
   → activeSessionCount becomes 1
```

**Important detail worth emphasizing:** `sessionDestroyed()` firing due to **timeout** happens **without any incoming request at all** — it's triggered by the container's own internal background timer/reaper mechanism, not by any code you write reacting to a request. This is a genuinely important distinction from Filters, which **only** ever run in response to an actual incoming request.

---

## COMMON ERRORS

**Error: Listener logic assumed to run per-request, but it doesn't**
- **Cause:** Confusing `ServletContextListener`'s one-time startup behavior with something that re-runs per request (that's what Filters are for, Topic 14).
- **Fix:** Use `ServletContextListener` only for genuine one-time application-lifetime setup/teardown; use Filters for anything that needs to run on every matching request.

**Error: Race condition despite "seeming" simple (the exact bug `AtomicInteger` avoids)**
```java
private static int count = 0;
count++; // if written this way instead of using AtomicInteger, under concurrent
         // session creation/destruction, this WILL eventually produce incorrect counts
```
- **Fix:** Use `AtomicInteger` (or proper `synchronized` blocks, covered in the dedicated Thread Safety topic next) for any counter/shared mutable state touched by listener callbacks, since these genuinely do fire concurrently in a real multi-user application.

**Error: Forgetting `@WebListener` annotation (or the `web.xml` `<listener>` entry) entirely**
- **Symptom:** Your listener class exists, compiles fine, but its methods never actually get called — because the container has no way of knowing this class exists as a listener without explicit registration.
- **Fix:** Every listener class **must** be registered — either via `@WebListener` or a `<listener>` entry in `web.xml` — simply implementing the interface is not enough on its own, unlike Servlets/Filters where the annotation itself carries the URL mapping info that also serves as the registration.