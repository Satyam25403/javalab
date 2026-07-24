# TOPIC 19: Servlet Best Practices — Module 1 Consolidated Checklist

## Why This Topic Exists

Across 18 topics, we've flagged individual best practices in context — this final topic of Module 1 pulls **every single one of them** together into one definitive, categorized reference. Treat this as the checklist you mentally run through whenever writing or reviewing Servlet code, and as your primary revision sheet before university exams/viva for this module.

---

## 1. Architecture & Design

| Practice | Why (with topic reference) |
|---|---|
| Follow MVC — Servlet as Controller only, JSP as View, Service/DAO as Model | Separation of concerns; testability; team collaboration (Topic 1) |
| Never write raw HTML inside a Servlet in real projects | Unmaintainable, mixes concerns (Topic 1, 7) |
| Keep Model classes free of any `jakarta.servlet.*` imports | Keeps business logic reusable outside a web context (Topic 1) |
| Use Filters for cross-cutting concerns (auth, logging, encoding) instead of repeating logic per-Servlet | DRY principle, single point of change (Topic 14) |
| Use `ServletContextListener` for genuine app-wide, one-time startup logic (e.g., connection pools) | Guaranteed to run once, regardless of which Servlets are ever hit (Topic 15) |

## 2. Thread Safety

| Practice | Why |
|---|---|
| Store request-specific data in **local variables**, never instance variables | Instance variables are shared across all concurrent threads (Topic 1, 18) |
| Only instance variables that are read-only after `init()` are safe | Mutation is the actual danger, not "instance variable-ness" itself (Topic 2, 18) |
| Use `Atomic*` classes for simple shared counters/flags | Faster than locks, still fully thread-safe (Topic 15, 18) |
| Use `synchronized` only around the minimal critical section | Avoid unnecessary performance loss from over-locking (Topic 18) |

## 3. Configuration

| Practice | Why |
|---|---|
| Match Servlet API version to Tomcat version (`jakarta.*` for Tomcat 10+) | Namespace mismatch causes deployment failure (Topic 1 Step 4) |
| Keep `web.xml` elements in strict spec-mandated order | Schema validation failure otherwise (Topic 5) |
| Use `<load-on-startup>` for Servlets with expensive `init()` logic you want ready immediately | Avoids first-user latency penalty (Topic 2) |
| Prefer named constants (`HttpServletResponse.SC_OK`) over raw status numbers | Readability, fewer typos (Topic 3, 8) |

## 4. Request/Response Handling

| Practice | Why |
|---|---|
| Always null-check `request.getParameter()`, `getAttribute()`, `getCookies()` | These return `null`, not exceptions, when absent (Topic 1, 6, 10) |
| Use `getParameterValues()` for any field that can submit multiple values (checkboxes) | `getParameter()` silently returns only the first value (Topic 8) |
| Set all headers/content-type/status **before** writing any response body | Headers can't change after the response is committed (Topic 8) |
| Use `CONSTANT.equals(variable)` instead of `variable.equals(CONSTANT)` when one side may be null | Avoids NullPointerException defensively (Topic 1) |

## 5. Scope & State Management

| Practice | Why |
|---|---|
| Choose the narrowest scope that satisfies the requirement (Request → Session → Application) | Prevents data leaking between users, avoids memory bloat (Topic 7) |
| Use `getSession(false)` when merely checking login state | Avoids accidentally creating empty sessions (Topic 7, 11) |
| Use `sendRedirect()` (not `forward()`) after a successful POST | Prevents duplicate-submission on refresh — Post-Redirect-Get pattern (Topic 9) |
| Never trust hidden form field values for sensitive/business-critical decisions | Fully client-editable, easily tampered with (Topic 13) |
| Set `HttpOnly` and `Secure` flags on sensitive cookies | Defends against XSS and network interception (Topic 10) |

## 6. Error Handling

| Practice | Why |
|---|---|
| Catch specific exceptions before general ones | Compiler-enforced for direct subclasses; correct behavior for others (Topic 16) |
| Never leave an empty `catch` block | Silently swallows failures with zero diagnostic trace (Topic 16) |
| Log full exception detail server-side; show only friendly messages to users | Security (no leaked internals) + genuine debuggability (Topic 16) |
| Always configure a catch-all `<error-page>` for `java.lang.Exception` | Prevents raw stack traces reaching end users (Topic 5, 16) |
| Use `sendError()` (not `setStatus()`) when you want your configured `<error-page>` to trigger | `setStatus()` alone doesn't activate error-page forwarding (Topic 8, 16) |

## 7. File Handling

| Practice | Why |
|---|---|
| Store uploaded files outside the deployable `webapp/` structure | Survives redeployment; avoids direct-execution security risk (Topic 17) |
| Validate file type/size both client-side (UX) and server-side (security) | Client-side checks are trivially bypassable (Topic 17) |
| Generate unique server-side filenames rather than trusting the original | Avoids collisions and reduces reliance on user-controlled input (Topic 17) |

## 8. Resource Management

| Practice | Why |
|---|---|
| Use try-with-resources for any stream (`InputStream`, `OutputStream`, `Reader`) | Guarantees closure even on exceptions, no manual `finally` needed (Topic 17) |
| Release resources acquired in `init()` inside `destroy()` | Symmetric setup/teardown prevents resource leaks (Topic 2) |

---

## The Single Most Important Idea From All of Module 1

If you remember **one** unifying principle from everything covered, it's this:

> **A Servlet is one shared object handling many concurrent users via many threads. Everything else — MVC, scope selection, thread safety, filters, listeners — exists either to correctly exploit that model's performance benefits, or to safely manage the risks that same model introduces.**

Every topic in this module — from Topic 1's CGI-vs-Servlet comparison through Topic 18's race conditions — is ultimately elaborating on consequences of that one architectural fact.

---

## MODULE 1 COMPLETE

You've now covered every topic listed under **Module 1: Servlet Fundamentals** — 19 topics, from "What is a Servlet" through Best Practices, each with full concept, internal mechanics, syntax, working code, execution tracing, and common errors.

**Per your module order, Module 2 begins next: JSP**, starting with **Introduction, JSP Architecture, and JSP Lifecycle** — and importantly, this is where several things we deliberately deferred throughout Module 1 (raw HTML-in-Servlet being a bad practice, the `${...}` EL syntax used without explanation since Topic 1's MVC example, the exact mechanics of "JSPs are secretly compiled into Servlets" first mentioned in Topic 1 Step 8) will finally be explained in full depth.

Say **"Next"** to begin Module 2.