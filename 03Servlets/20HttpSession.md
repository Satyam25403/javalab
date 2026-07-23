# TOPIC 11: HttpSession — Complete Deep Dive

## CONCEPT

### Why this concept exists

We've used `HttpSession` functionally already — `getSession()`, `setAttribute()`, `getAttribute()`. This topic formalizes its **complete lifecycle**, **full method reference**, **timeout mechanics**, and **invalidation** — the operational details you need for real applications.

### Real-world analogy (extending Topic 10's coat-check analogy)

If the `JSESSIONID` cookie is the numbered ticket, `HttpSession` is the **actual coat hanging on the rack**, with a labeled hook containing whatever else you've stored there (session attributes). The ticket alone is meaningless without the coat rack (server memory) still holding the item — if the coat check closes for the night (session times out, or the server restarts without persistence), your ticket becomes useless even if you still physically have it.

---

## HttpSession — Complete Method Reference

```java
import jakarta.servlet.http.HttpSession;
```

| Method | Returns | Purpose |
|---|---|---|
| `setAttribute(String name, Object value)` | `void` | Store data in this session |
| `getAttribute(String name)` | `Object` | Retrieve stored data (returns `null` if absent) |
| `removeAttribute(String name)` | `void` | Remove one specific attribute |
| `getAttributeNames()` | `Enumeration<String>` | All attribute names currently stored |
| `getId()` | `String` | The unique session ID (same value carried by the `JSESSIONID` cookie) |
| `isNew()` | `boolean` | `true` if this session was just created on this exact request, `false` if it already existed from a previous request |
| `getCreationTime()` | `long` | Timestamp (ms since epoch) when this session was created |
| `getLastAccessedTime()` | `long` | Timestamp of the most recent request that used this session |
| `setMaxInactiveInterval(int seconds)` | `void` | Overrides the timeout for **this specific session** (independent of the app-wide `<session-timeout>` from `web.xml`, Topic 5) |
| `getMaxInactiveInterval()` | `int` | Current timeout setting, in seconds |
| `invalidate()` | `void` | **Destroys** this session immediately — all attributes discarded, session ID becomes invalid |

---

## Session Timeout — Precise Mechanics

Recall from Topic 5:
```xml
<session-config>
    <session-timeout>30</session-timeout> <!-- minutes -->
</session-config>
```

**How the timeout is actually measured:** it's **not** a fixed 30 minutes from creation — it's 30 minutes of **inactivity**. Every time a request arrives carrying a valid `JSESSIONID` that matches an existing session, the container **resets** that session's internal "last accessed" timer. Only if **no request** uses that session for the full timeout duration does the container mark it expired and eligible for cleanup.

**Overriding per-session, in code:**
```java
HttpSession session = request.getSession();
session.setMaxInactiveInterval(60 * 15); // 15 minutes, in SECONDS (note: web.xml uses minutes, this method uses seconds — a genuinely common source of confusion)
```

**Critical unit mismatch to memorize (frequently tested):** `web.xml`'s `<session-timeout>` is in **minutes**. `setMaxInactiveInterval()` in Java code is in **seconds**. Mixing these up is an extremely common, silent bug — setting `session.setMaxInactiveInterval(30)` intending "30 minutes" actually sets a **30-second** timeout.

---

## Session Invalidation — Explicit Logout

```java
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // don't create a new one just to destroy it

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("login.html");
    }
}
```

**Why `getSession(false)` here, precisely:** if a user who was never logged in somehow hits `/logout` directly, calling plain `getSession()` would **create** a brand-new session just so you could immediately invalidate it — wasteful and conceptually backwards. `getSession(false)` correctly returns `null` if there's nothing to log out of, and we simply skip invalidation in that case.

**What `invalidate()` actually does internally:**
1. All attributes stored in that session are discarded (eligible for garbage collection).
2. The session object itself is removed from the container's internal session-tracking map.
3. The `JSESSIONID` cookie **value itself is not directly deleted from the browser** by `invalidate()` alone — but since the server no longer recognizes that ID as valid, the **next** request using that stale cookie will trigger the container to treat it as if no valid session exists, and `getSession()` on that next request will create a **brand new** session with a **new** ID. Practically, this means invalidation is fully effective from the server's perspective immediately.

**Calling any session method on an already-invalidated session object throws `IllegalStateException`:**
```java
session.invalidate();
session.setAttribute("x", "y"); // throws IllegalStateException: session already invalidated
```
- **Rule:** Once you call `invalidate()`, treat that `session` variable as dead — never use it again in that method.

---

## Complete Login/Logout Flow Combining Everything (Topics 7, 10, 11)

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
            HttpSession session = request.getSession(); // creates new session
            session.setAttribute("loggedInUser", username);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes, in seconds

            System.out.println("New session created: " + session.getId() + ", isNew: " + session.isNew());

            response.sendRedirect("dashboard");
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

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("loggedInUser");
        long lastAccessed = session.getLastAccessedTime();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Welcome, " + username + "!</h2>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<a href='logout'>Logout</a>");
    }
}
```

This three-Servlet pattern (`LoginServlet` / `DashboardServlet` / `LogoutServlet`) is **exactly** the authentication skeleton you'll reuse, with a real database instead of hardcoded credentials, for **every project in Module 4** — Student Management, Employee Management, Library Management, and the Banking mini-project all follow this identical session-based authentication pattern.

---

## Session Tracking Without Cookies — URL Rewriting (Preview, Full Coverage Next Topic)

**Important edge case worth knowing now:** what happens if the client has **cookies disabled** in their browser? Without the `JSESSIONID` cookie, the container has no way to recognize returning requests as belonging to the same session — **by default, every request would appear to be a brand-new session.**

The Servlet API provides a fallback mechanism for exactly this scenario: **URL Rewriting**, which encodes the session ID directly into URLs instead of relying on cookies.

---

## EXECUTION FLOW — Session Timeout Scenario

```
10:00 AM — User logs in → session created (ID: XYZ123), timeout = 30 min
10:05 AM — User navigates to dashboard → session XYZ123 found, timer RESET, 
           lastAccessedTime updated to 10:05
10:10 AM — User views another page → timer RESET again to 10:10
           [user then closes laptop, walks away]
10:40 AM — 30 minutes of INACTIVITY have now passed since 10:10
           → Container's background reaper thread marks session XYZ123 as expired
           → Session object destroyed, memory freed
11:00 AM — User returns, opens laptop, clicks a link
           → Browser sends OLD JSESSIONID=XYZ123 cookie (browser doesn't know it expired)
           → Container checks: no session exists with ID XYZ123 anymore
           → request.getSession(false) returns null
           → Application logic (DashboardServlet above) detects null session,
             redirects to login.html — user must log in again
```

This trace demonstrates precisely why the `getSession(false)` + `null`-check pattern in `DashboardServlet` is essential.

---

## COMMON ERRORS

**Error: `IllegalStateException` after invalidating a session**
- Already explained above — using a session object after calling `invalidate()` on it. **Fix:** never touch that reference again post-invalidation.

**Error: Confusing minutes (`web.xml`) with seconds (`setMaxInactiveInterval`)**
- Already flagged above as a critical, easy-to-make unit mismatch. **Fix:** always double check — if setting programmatically, multiply your intended minutes by 60.

**Error: Session data unexpectedly "disappearing" during development**
- **Cause:** Redeploying/restarting the app in Eclipse (Topic 2's lifecycle discussion) destroys **all** in-memory sessions — since `HttpSession` objects live in server memory by default, a server restart wipes them entirely (unless session persistence across restarts is explicitly configured, which is beyond this course's current scope).
- **Fix:** This is expected/normal during development — just log in again after any server restart; don't mistake this for a bug in your code.

**Error: Believing session data is shared between two different browsers/incognito windows for "the same user"**
- **Cause:** Misunderstanding that sessions are tied to the **browser's cookie**, not the "person" — opening an incognito window, or a completely different browser, means **no `JSESSIONID` cookie is shared**, so the container treats it as an entirely separate, brand-new session, even if it's genuinely the same human being logging in again.
- **Fix:** This is actually correct, expected behavior — worth understanding conceptually rather than "fixing."