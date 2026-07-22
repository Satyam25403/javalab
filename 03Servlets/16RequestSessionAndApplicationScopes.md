# TOPIC 7: Request Scope, Session Scope, Application Scope

## CONCEPT

### Why this concept exists

Throughout this course so far, you've seen `request.setAttribute()` (Topic 1's MVC example) and `context.setAttribute()` (Topic 6's visit counter). There's a **third** scope you haven't formally used yet — **session scope**, via `HttpSession`. Together, these three form the **complete attribute-scoping system** in Java web applications — storing and sharing data across different **lifespans** and **visibility boundaries**.

**The fundamental question each scope answers:** *"How long should this piece of data live, and who should be able to see it?"* Choosing the wrong scope is one of the most common **design mistakes** beginners make — storing something in application scope that should be per-user (causing data leakage between users) or storing something in request scope that needs to persist across multiple pages (causing data loss).

### Real-world analogy

Think of a **hotel**:
- **Request scope** = A **single conversation** with the front desk during check-in — information exchanged (your ID, your form) is relevant only for that one interaction, then forgotten immediately after.
- **Session scope** = **Your hotel room key card**, valid for the duration of *your* stay — it identifies *you specifically*, works across multiple visits to different parts of the hotel (restaurant, gym, room) during *your* stay, but means nothing to a different guest, and stops working after checkout.
- **Application scope** = The **hotel's shared lobby announcement board** — visible to *every* guest, persists for as long as the hotel (application) is open, completely independent of any single guest's stay.

---

## THE THREE SCOPES — Complete Comparison

| Aspect | Request Scope | Session Scope | Application Scope |
|---|---|---|---|
| Object | `HttpServletRequest` | `HttpSession` | `ServletContext` |
| Lifetime | One single request (and anything it's forwarded to) | From session creation until timeout/invalidation (spans many requests, one user) | Entire application lifetime (spans many requests, ALL users) |
| Visibility | Only the current request-handling chain (Servlet → forwarded JSP) | Only the one specific user this session belongs to | Every user, every Servlet, every JSP in the whole app |
| Method to store | `request.setAttribute(k, v)` | `session.setAttribute(k, v)` | `context.setAttribute(k, v)` (a.k.a. `getServletContext().setAttribute(...)`) |
| Method to read | `request.getAttribute(k)` | `session.getAttribute(k)` | `context.getAttribute(k)` |
| Thread-safety concern? | No — each request has its own object, never shared across threads | Partially — same user's concurrent requests (e.g., two browser tabs) could race | Yes — heavily shared across all users/threads simultaneously |
| Typical use case | Passing validation results/data from Servlet to JSP (Topic 1's MVC example) | Logged-in user's identity, shopping cart contents | Global counters, shared configuration, app-wide caches |

---

## 1. Request Scope — Recap and Formalization

We've already used this extensively (Topic 1, Step 7.11-7.14). Formally:

- **Created:** fresh, by the container, for every single incoming HTTP request.
- **Destroyed:** immediately after the response for that request is sent back (garbage collected once no references remain).
- **Extends across `forward()`:** Because `forward()` passes the *same* `request`/`response` objects (Topic 1, Step 8, Phase 7), an attribute set before forwarding remains readable in the forwarded-to resource. **This does NOT apply across `sendRedirect()`** — a redirect creates an entirely **new** request from the browser, so request-scoped attributes are lost.

```java
request.setAttribute("username", username);   // set in Servlet
request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
// welcome.jsp can read ${username} via EL — SAME request object
```

---

## 2. Session Scope — New Formal Introduction

### What a "session" conceptually solves

HTTP is fundamentally a **stateless protocol** — each request is, by design, independent, with no built-in memory of previous requests from the same client. But real applications need to remember: *"Is this user logged in? What's in their cart?"* across multiple page loads. `HttpSession` is the Servlet API's mechanism for **simulating statefulness on top of a stateless protocol**.

### How it works internally

1. When you first call `request.getSession()`, the container creates a new `HttpSession` object and generates a **unique session ID**.
2. That session ID is sent back to the browser (typically via a **cookie** named `JSESSIONID`).
3. On every subsequent request, the browser automatically sends that cookie back.
4. The container reads the `JSESSIONID` cookie, looks up the matching `HttpSession` object (stored server-side, in memory), and gives your code access to **that same session** — now you can retrieve data you stored on a previous request.

### Code Example — Session Scope in Action

```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean isValid = LoginService.validateCredentials(username, password);

        if (isValid) {
            HttpSession session = request.getSession(); // creates session if none exists
            session.setAttribute("loggedInUser", username);
            response.sendRedirect("dashboard"); // note: redirect, not forward — new request
        } else {
            request.setAttribute("errorMessage", "Invalid credentials.");
            request.getRequestDispatcher("/WEB-INF/views/loginError.jsp").forward(request, response);
        }
    }
}
```

```java
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // do NOT create if none exists

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.html"); // not logged in, kick back to login
            return;
        }

        String username = (String) session.getAttribute("loggedInUser");
        response.setContentType("text/html");
        response.getWriter().println("<h2>Welcome back, " + username + "!</h2>");
    }
}
```

**Critical new detail: `getSession()` vs `getSession(false)`**

| Call | Behavior |
|---|---|
| `request.getSession()` | Returns existing session **or creates a new one** if none exists — never returns `null` |
| `request.getSession(true)` | Identical to above — `true` is the default/implicit argument |
| `request.getSession(false)` | Returns existing session **or `null`** — never creates a new one |

**Why does `getSession(false)` matter?** This is a crucial security/logic pattern: in `DashboardServlet`, you want to check *"is this user already logged in?"* — you do **not** want to accidentally create a brand-new, empty session just by checking. Using plain `getSession()` here would be a subtle bug: it would silently create a session even for a user who was never logged in, potentially masking the fact that they should be redirected to login. **Always use `getSession(false)` when you're checking for an existing login state**, and only use `getSession()` (or `getSession(true)`) when you genuinely intend to create one (like right after successful login).

**Why `sendRedirect()` here instead of `forward()`?** Since `session.setAttribute()` was used (not `request.setAttribute()`), the data **persists across the new request** created by the redirect — session data isn't tied to a single request-response cycle the way request-scoped data is. This lets us safely use a redirect (which, as a bonus, also gives the user a clean URL change and prevents "resubmit form" issues on refresh — a UX best practice for post-login flows).

---

## 3. Application Scope 

- **Created:** once, when the application starts.
- **Destroyed:** once, when the application shuts down/undeploys.
- **Visible to:** literally every Servlet, Filter, Listener, and JSP in the entire deployed application, regardless of which user or which request.
- **Danger:** because it's shared across **all concurrent users**, mutable application-scoped data is a **prime thread-safety risk**.

---

## Choosing the Right Scope — Decision Framework

Ask these questions in order:

```
Does this data need to be visible to EVERY user of the application?
  │
  YES → Application Scope
  │
  NO
  │
  ▼
Does this data need to persist across MULTIPLE requests
for the SAME user (e.g., across page navigations)?
  │
  YES → Session Scope
  │
  NO
  │
  ▼
Is this data only needed within the current
request-forward chain (Servlet → JSP)?
  │
  YES → Request Scope
```

**Common real mistakes (things to actively avoid):**
1. **Storing per-user data in Application scope** — e.g., storing `loggedInUser` in `ServletContext` instead of `HttpSession`. This would mean **every user shares the same "logged in user"** — a severe bug where User B could see User A's session data.
2. **Storing large or sensitive data in Session scope unnecessarily** — sessions consume server memory for as long as they're alive (up to the timeout); storing huge objects per-user, per-session, for thousands of concurrent users can exhaust server memory. Store only what's needed (e.g., a user ID, not the user's entire database row).
3. **Expecting Request scope data to survive a redirect** — a very common bug: setting `request.setAttribute()` then calling `sendRedirect()` instead of `forward()`, and being confused why the data is "gone" on the next page (because a redirect is a **brand new request** — the old request, and its attributes, are discarded entirely).

---

## EXECUTION FLOW — Visualizing All Three Scopes Together

```
                    ┌───────────────────────────────────────┐
                    │      APPLICATION SCOPE (ServletContext) │
                    │      Lives: entire app lifetime          │
                    │      Visible to: ALL users                │
                    │                                            │
                    │   ┌───────────────────────────────────┐  │
                    │   │   SESSION SCOPE (HttpSession)       │  │
                    │   │   User A's session                   │  │
                    │   │   Lives: until timeout/logout         │  │
                    │   │                                        │  │
                    │   │  ┌──────────────┐ ┌──────────────┐  │  │
                    │   │  │REQUEST SCOPE │ │REQUEST SCOPE │  │  │
                    │   │  │ (Request #1) │ │ (Request #2) │  │  │
                    │   │  │ Lives: 1 req │ │ Lives: 1 req │  │  │
                    │   │  └──────────────┘ └──────────────┘  │  │
                    │   └───────────────────────────────────┘  │
                    │                                            │
                    │   ┌───────────────────────────────────┐  │
                    │   │   SESSION SCOPE (HttpSession)       │  │
                    │   │   User B's session (SEPARATE!)       │  │
                    │   │  ┌──────────────┐                    │  │
                    │   │  │REQUEST SCOPE │                    │  │
                    │   │  └──────────────┘                    │  │
                    │   └───────────────────────────────────┘  │
                    └───────────────────────────────────────┘
```

Request scope is the narrowest (nested inside a session), Session scope is per-user (nested inside the application), and Application scope is the widest, containing everything.

---

## COMMON ERRORS

**Error: `NullPointerException` after redirect, expecting request-scoped data to survive**
```java
request.setAttribute("message", "Success!");
response.sendRedirect("result.jsp"); // NEW request — attribute is lost
```
```jsp
${message} <!-- renders nothing, attribute doesn't exist in this new request -->
```
- **Fix:** Either use `forward()` instead of `redirect()` if you don't need the URL to change, or use session scope temporarily (with manual cleanup) if a redirect is required — this exact "flash message across redirect" problem is a well-known pattern, formally solved in Spring with "flash attributes," but in raw Servlets you'd handle it manually via a short-lived session attribute you remove immediately after reading.

**Error: Data "bleeding" between different users (severe bug, not just an exception)**
- **Cause:** Using `ServletContext.setAttribute()` for something that should be session-scoped (like storing "current logged in user" at application level).
- **Symptom:** User A logs in, then User B (different browser/machine) sees User A's dashboard/data.
- **Fix:** Audit every `context.setAttribute()` call and ask "could this ever need to differ between two simultaneous users?" — if yes, it belongs in session scope, not application scope.

**Error: `ClassCastException` on session attribute retrieval**
- `session.getAttribute()` also returns `Object`, requiring a cast, with identical risk if inconsistent types are stored under the same key.