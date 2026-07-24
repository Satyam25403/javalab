# TOPIC 14: Filters:(equivalent to middlewares)

## CONCEPT

### Why this concept exists

Consider `DashboardServlet` from Topic 11 — it starts with an authentication check (`getSession(false)`, null-check, redirect if not logged in). Now imagine you have **20 different Servlets** in your application, all of which need this **exact same** authentication check. Copy-pasting that check into every single Servlet is a maintenance nightmare — if the login logic ever changes, you must remember to update it in 20 places, and inevitably you'll miss one, creating a security hole.

**Filters** solve this by letting you write **cross-cutting logic once** and apply it to **many Servlets** (or the entire application) declaratively, via URL pattern matching — **without modifying any Servlet's code at all.**

### What problem it solves

"Cross-cutting concerns" — functionality that logically applies **across** many otherwise-unrelated parts of your application (authentication, logging, input sanitization, compression, character encoding setup) — don't belong inside individual business-logic Servlets. Filters extract this logic into a **separate, reusable, pluggable layer** that intercepts requests **before** they reach a Servlet, and can also intercept the **response** on its way back out.

### Real-world analogy

Think of **airport security checkpoints**. Every passenger (request), regardless of which specific airline/gate (Servlet) they're ultimately headed to, passes through the **same** security checkpoint (Filter) first. The checkpoint doesn't know or care about your final destination — it just performs its one job (screening) on everyone, then lets you continue to your actual gate. If security find a problem, they can **stop you right there** — you never even reach your gate (the Servlet never gets invoked at all).

### Architecture — The Filter Chain (Chain of Responsibility Pattern)

```
Browser Request
        │
        ▼
┌─────────────────┐
│   Filter 1        │  (e.g., LoggingFilter)
│   - pre-processing │
└────────┬──────────┘
         │ chain.doFilter()
         ▼
┌─────────────────┐
│   Filter 2        │  (e.g., AuthFilter)
│   - pre-processing │
└────────┬──────────┘
         │ chain.doFilter()
         ▼
┌─────────────────┐
│     Servlet        │  (e.g., DashboardServlet)
│  (actual business   │
│      logic)          │
└────────┬──────────┘
         │ (response begins forming)
         ▼
┌─────────────────┐
│   Filter 2        │
│  - post-processing  │  (code AFTER chain.doFilter() call)
└────────┬──────────┘
         ▼
┌─────────────────┐
│   Filter 1        │
│  - post-processing  │
└────────┬──────────┘
         ▼
Browser Response
```

**Critical insight from this diagram:** Filters execute in **declared order** on the way **in** (Filter 1 → Filter 2 → Servlet), but in **reverse order** on the way **out** (Servlet → Filter 2 → Filter 1) — this is the classic **Chain of Responsibility** design pattern, and it's structurally identical to how middleware works in virtually every modern web framework.

---

## SYNTAX — The `Filter` Interface

```java
public interface Filter {
    default void init(FilterConfig filterConfig) throws ServletException {}
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException;
    default void destroy() {}
}
```

**Notice the parallel structure to `Servlet`'s lifecycle (Topic 2) — this is deliberate:**

| Filter method | Servlet equivalent | Called when |
|---|---|---|
| `init(FilterConfig)` | `init(ServletConfig)` | Once, when the Filter is first loaded |
| `doFilter(...)` | `service(...)` | Every time a matching request passes through |
| `destroy()` | `destroy()` | Once, on shutdown/undeploy |

**Key parameter type difference — `ServletRequest`/`ServletResponse`, NOT `HttpServletRequest`/`HttpServletResponse`:** The `Filter` interface is deliberately **protocol-agnostic** (recall Topic 1 Step 6.1's `GenericServlet` discussion — same design philosophy). Since we're always working with HTTP, you'll almost always **cast** these to their Http-specific subtypes inside your `doFilter()` implementation:

```java
HttpServletRequest httpRequest = (HttpServletRequest) request;
HttpServletResponse httpResponse = (HttpServletResponse) response;
```

---

## Code Example 1 — `AuthFilter`: Centralizing the Authentication Check

This directly replaces the repeated authentication logic that would otherwise live inside every protected Servlet:

```java
package com.company.myapp.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/dashboard/*")   // protects everything under /dashboard/
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("loggedInUser") != null);

        if (loggedIn) {
            // Allow the request to CONTINUE to the next Filter, or the Servlet
            chain.doFilter(request, response);
        } else {
            // STOP here — the Servlet is NEVER reached
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }
    }
}
```

### Line-by-line explanation of new concepts

**`@WebFilter("/dashboard/*")`**
- Uses the **path mapping** wildcard pattern (Topic 4) — this Filter intercepts **every** request whose path starts with `/dashboard/`, regardless of which specific Servlet ultimately handles it. This is exactly the power that eliminates repeated per-Servlet checks: protect an entire **section** of your application with one declaration.

**`FilterChain chain` (third parameter)**
- Represents the **rest of the filter chain**, plus the target Servlet at the very end. This object is your **only way** to pass control forward.

**`chain.doFilter(request, response)`**
- This is the single most important line in any Filter. **Calling this passes control to the next component in the chain** (the next Filter, or the final Servlet if this is the last Filter). 
- **If you never call this**, the chain **stops here** — the Servlet (and any subsequent Filters) **never execute at all**. This is precisely how `AuthFilter` **blocks** unauthorized access: in the `else` branch, we simply don't call `chain.doFilter()` — we redirect instead, and the request's journey ends right there, at the Filter, never reaching `DashboardServlet`.

---

## Code Example 2 — `LoggingFilter`: Demonstrating Pre- AND Post-Processing

```java
package com.company.myapp.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*")   // applies to EVERY request in the entire application
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        long startTime = System.currentTimeMillis();

        // ===== PRE-PROCESSING (before the Servlet runs) =====
        System.out.println("Incoming request: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

        chain.doFilter(request, response);  // Servlet (and any further filters) execute HERE

        // ===== POST-PROCESSING (after the Servlet has finished) =====
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Completed: " + httpRequest.getRequestURI() + " in " + duration + "ms");
    }
}
```

**Why `"/*"` here, but `"/dashboard/*"` for `AuthFilter`?** Logging is a concern relevant to **every** request in the app, so it uses the broadest possible pattern. Authentication is only relevant to **protected** sections, so it's scoped more narrowly. This demonstrates how **different Filters can have entirely different scopes**, each independently declared — a single application typically has **several** Filters simultaneously active, each handling one specific concern.

**Notice the code structure: everything before `chain.doFilter()` is pre-processing; everything after is post-processing.** This single method, split by that one call, is what enables the "wrap around" behavior shown in the architecture diagram above.

---

## `web.xml` Equivalent (Filter + Filter-Mapping, Referencing Topic 5)

```xml
<filter>
    <filter-name>AuthFilter</filter-name>
    <filter-class>com.company.myapp.filter.AuthFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>AuthFilter</filter-name>
    <url-pattern>/dashboard/*</url-pattern>
</filter-mapping>

<filter>
    <filter-name>LoggingFilter</filter-name>
    <filter-class>com.company.myapp.filter.LoggingFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>LoggingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

**Ordering rule (flagged in Topic 5, now made concrete):** if both `AuthFilter` and `LoggingFilter` match a given request (e.g., `/dashboard/home` matches both `/dashboard/*` and `/*`), they execute in the **order their `<filter-mapping>` elements appear**, then the Servlet. **When using annotations (`@WebFilter`) instead**, the ordering is determined by the container's own class-scanning order, which is **not guaranteed or predictable** — this is precisely why, if execution order genuinely matters between multiple Filters, **`web.xml`-based `<filter-mapping>` ordering is the reliable, spec-compliant way to control it**, even in an otherwise annotation-heavy project. This is a real, practical reason to fall back to `web.xml` for specific declarations, directly connecting back to Topic 5's "why web.xml still matters" discussion.

---

## Practical Use Cases (Beyond the Two Examples Shown)

1. **Character Encoding Filter** — setting `request.setCharacterEncoding("UTF-8")` centrally for every request, rather than repeating it in every Servlet (a genuinely common real-world Filter you'll see in almost every serious Java web project).
2. **Input Sanitization** — directly addressing Topic 13's XSS concern: a Filter could wrap the request object and strip/escape dangerous characters from all parameters centrally, before any Servlet even sees them.
3. **Compression** — wrapping the response to gzip-compress output before sending it to the browser.
4. **CORS handling** — adding necessary cross-origin headers for API requests (highly relevant once you build REST APIs in Spring Boot).

---

## EXECUTION FLOW — Complete Trace with Both Filters Active

```
Request: GET /myapp/dashboard/home  (user IS logged in)
        │
        ▼
Tomcat's Mapper determines which Filters AND which Servlet match this URL
   → LoggingFilter ("/*") matches
   → AuthFilter ("/dashboard/*") matches
   → DashboardServlet ("/dashboard/home" or similar) matches
        │
        ▼
Filter chain constructed in web.xml declaration order: [LoggingFilter, AuthFilter, Servlet]
        │
        ▼
LoggingFilter.doFilter() begins
   → prints "Incoming request: GET /myapp/dashboard/home"
   → calls chain.doFilter() ─────────────┐
                                           ▼
                              AuthFilter.doFilter() begins
                                 → checks session → user IS logged in
                                 → calls chain.doFilter() ─────────┐
                                                                     ▼
                                                        DashboardServlet.doGet() runs
                                                           → generates response
                                                     ┌───────────────┘
                                 → (back in AuthFilter, nothing after chain.doFilter() here)
                              AuthFilter.doFilter() ends
                     ┌────────────────────────────────┘
   → (back in LoggingFilter, POST-processing runs now)
   → prints "Completed: /myapp/dashboard/home in 12ms"
LoggingFilter.doFilter() ends
        │
        ▼
Response sent to Browser
```

**Contrast trace — same request, but user is NOT logged in:**
```
LoggingFilter pre-processing runs → chain.doFilter()
        │
        ▼
AuthFilter checks session → NOT logged in
        │
        ▼
AuthFilter calls httpResponse.sendRedirect(...) 
   -- does NOT call chain.doFilter() --
        │
        ▼
Chain STOPS here. DashboardServlet.doGet() NEVER RUNS.
        │
        ▼
Control returns back up to LoggingFilter's post-processing
   (this STILL runs, since LoggingFilter's chain.doFilter() call already returned control)
   → prints "Completed: ... " (logs the redirect as the outcome)
        │
        ▼
Redirect response sent to Browser
```

This second trace is important to internalize: **even when a downstream Filter blocks the chain, control still unwinds back through every filter that already called `chain.doFilter()`** — `LoggingFilter`'s post-processing code still runs, because from `LoggingFilter`'s perspective, its `chain.doFilter()` call simply returned (it doesn't know or care *why* — whether the Servlet ran normally or a downstream Filter blocked it).

---

## COMMON ERRORS

**Error: Forgetting to call `chain.doFilter()` entirely (not even in an else branch)**
- **Symptom:** Every request matching that Filter's pattern silently hangs or returns a blank response — the Servlet never runs, and if you also forgot to write any response content or redirect, the browser gets an empty response.
- **Fix:** Every code path through `doFilter()` must **either** call `chain.doFilter()` **or** explicitly write a complete response (like a redirect or error) — never leave a path that does neither.

**Error: Calling `chain.doFilter()` AND also trying to write response content afterward incorrectly**
```java
chain.doFilter(request, response);
response.getWriter().println("Extra content"); // may fail if Servlet already committed the response
```
- **Cause:** If the downstream Servlet already fully wrote and flushed its response, attempting to write more content afterward in the Filter can cause `IllegalStateException` (same committed-response rule from Topic 8/9).
- **Fix:** Post-processing in Filters is safe for things like logging, timing, or modifying headers **before** the response is committed — but assume you generally **cannot** meaningfully alter the response body after `chain.doFilter()` returns, unless you deliberately wrapped the response object to buffer it (an advanced technique beyond this course's current scope).

**Error: `ClassCastException` when casting `ServletRequest` to `HttpServletRequest`**
- **Cause:** Extremely rare in practice (since virtually all real deployments are HTTP-based), but technically possible if a Filter is used in a non-HTTP context. Not a practical concern for your coursework — just be aware the cast exists because the interface is generically typed, as explained above.

**Error: Two filters unexpectedly running in the "wrong" order**
- **Cause:** Relying on `@WebFilter` annotation ordering when execution order actually matters.
- **Fix:** Switch to explicit `<filter-mapping>` ordering in `web.xml` when order matters (as discussed above) — remember Topic 5's precedence rule: `web.xml` declarations can coexist with/override annotation-based ones.