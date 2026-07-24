# TOPIC 31: Session Handling (in JSP)

## CONCEPT

### Why this concept exists

Topic 11 covered `HttpSession` exhaustively from the **Servlet** side. Topic 23 introduced the `session` implicit object; Topic 24 introduced `sessionScope` in EL. This topic **consolidates** these into the specific, practical patterns you'll actually write in **View-layer JSPs** — displaying login state, conditionally showing content based on session data, and the precise division of labor between Servlet (which should manage session **writes**) and JSP (which should only perform session **reads**, for display).

### The Architectural Principle, Stated Precisely

Following Topic 1's MVC principle, applied specifically to sessions:

```
Servlet (Controller): CREATES sessions, WRITES session attributes
       (session.setAttribute(...), session.invalidate())
                              │
                              ▼
JSP (View): READS session attributes for display only
       (${sessionScope.xxx}, session.getAttribute(...) in a scriptlet — discouraged)
```

**A JSP calling `session.setAttribute(...)` to store login state is a structural anti-pattern** — exactly parallel to Topic 25's `<jsp:forward>` warning: it works, but it blends Controller responsibility into the View. Real login/logout **logic** (Topic 11's `LoginServlet`/`LogoutServlet`) belongs in Servlets; JSPs should only **display** what's already there.

---

## Reading Session Data — Three Ways, Ranked by Preference

### 1. EL `sessionScope` (Preferred — Topic 24)

```jsp
<p>Welcome, ${sessionScope.loggedInUser}!</p>
```

### 2. Plain EL (also works, since EL searches all scopes automatically — Topic 24)

```jsp
<p>Welcome, ${loggedInUser}!</p>
```

**Why prefer explicit `sessionScope` over plain `${loggedInUser}` here:** plain EL searches **Page → Request → Session → Application** in order (Topic 24) — if some other page-or-request-scoped attribute happened to share the exact name `loggedInUser`, plain EL could silently pick up the **wrong** one. `sessionScope.loggedInUser` removes all ambiguity, guaranteeing you read specifically from session scope — a good habit for anything security/identity-related, where ambiguity could have real consequences.

### 3. Scriptlet (Discouraged, shown for completeness/exam recognition)

```jsp
<% String user = (String) session.getAttribute("loggedInUser"); %>
Welcome, <%= user %>!
```

Functionally identical, but reintroduces exactly the scriptlet verbosity/fragility JSTL and EL exist to eliminate (Topics 21, 24, 26) — **avoid this in new code**; recognize it in legacy code.

---

## Conditional Display Based on Login State — Combining Topic 26's JSTL

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
    <c:when test="${not empty sessionScope.loggedInUser}">
        <p>Welcome, <c:out value="${sessionScope.loggedInUser}" />! <a href="logout">Logout</a></p>
    </c:when>
    <c:otherwise>
        <p><a href="login.html">Login</a></p>
    </c:otherwise>
</c:choose>
```

This is the **exact** pattern you'll place at the top of virtually every "logged-in area" page across your Module 4 projects — a clean, scriptlet-free navigation bar reacting to session state.

---

## Complete Example — Dashboard JSP, Properly Separated From Its Controller

**Servlet (Controller — owns all session logic, per Topic 11):**
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

        // Servlet may ALSO prepare additional data for display, e.g.:
        request.setAttribute("lastLogin", session.getAttribute("lastLoginTime"));

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
```

**JSP (View — pure display, zero session-writing logic):**
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<body>
    <h2>Welcome, <c:out value="${sessionScope.loggedInUser}" />!</h2>

    <c:if test="${not empty lastLogin}">
        <p>Last login: <fmt:formatDate value="${lastLogin}" pattern="dd-MMM-yyyy HH:mm" /></p>
    </c:if>

    <a href="logout">Logout</a>
</body>
</html>
```

**Notice the JSP never once calls `session.setAttribute()` or `session.invalidate()`** — it purely **reads**, via `sessionScope` (session data) and plain request-scoped `${lastLogin}` (Servlet-prepared display data, Topic 7) — exactly the disciplined separation this topic exists to reinforce.

---

## A Genuinely Useful Pattern — Session Timeout Warning Display

Combining Topic 11's `getMaxInactiveInterval()`/`getLastAccessedTime()` with EL:

```jsp
<%
    long lastAccessed = session.getLastAccessedTime();
    long maxInactive = session.getMaxInactiveInterval() * 1000L; // seconds → ms
    long remainingMs = maxInactive - (System.currentTimeMillis() - lastAccessed);
    request.setAttribute("minutesRemaining", remainingMs / 60000);
%>
<p>Your session will expire in approximately ${minutesRemaining} minute(s) of inactivity.</p>
```

**Why this small piece still uses a scriptlet, rather than pure EL/JSTL:** this calculation involves **arithmetic across multiple session properties** that don't map to a simple JavaBean property or JSTL tag — a legitimate, narrow case where a small scriptlet remains reasonable, **provided it contains no mutable shared state** (Topic 21's thread-safety warning) — this is purely local computation using local variables, safe by Topic 1/18's rules. This illustrates the **real-world balance**: prefer JSTL/EL overwhelmingly, but don't contort yourself avoiding a three-line, thread-safe scriptlet when it's genuinely the clearest tool for a specific, narrow calculation.

---

## EXECUTION FLOW

```
Request: GET /myapp/dashboard  (browser sends JSESSIONID cookie, Topic 10)
        │
        ▼
DashboardServlet: request.getSession(false) → finds existing session
   → session.getAttribute("loggedInUser") → "Rahul" (not null, proceed)
   → request.setAttribute("lastLogin", ...)
   → forward() to dashboard.jsp
        │
        ▼
dashboard.jsp executes (Topic 20's translation → _jspService()):
   → ${sessionScope.loggedInUser} → EL reads DIRECTLY from the session
     object (same object the Servlet used, Topic 7's scope-sharing)
   → ${lastLogin} → EL reads from REQUEST scope (Servlet-prepared value)
        │
        ▼
Rendered HTML sent to browser, reflecting both session identity
and Servlet-prepared supplementary data, cleanly combined
```

---

## COMMON ERRORS

**Error: `NullPointerException`-equivalent silent blank output from `sessionScope`**
- **Cause:** Same as Topic 24's general EL silent-failure behavior — if `loggedInUser` was never set (user genuinely not logged in, or a Servlet forgot to set it), `${sessionScope.loggedInUser}` simply prints nothing — no crash, easy to miss during testing.
- **Fix:** Always guard session-dependent display with `<c:if test="${not empty sessionScope.xxx}">`, exactly as shown, rather than assuming the value is always present.

**Error: JSP directly writing to the session (architectural smell, not a runtime error)**
```jsp
<% session.setAttribute("visitedDashboard", true); %>
```
- **Cause:** Placing session-mutation logic in the View layer instead of the Controller.
- **Fix:** Move this kind of logic into the Servlet — recall this topic's core architectural principle: JSPs read, Servlets write.

**Error: Assuming `session` is always non-null inside a JSP**
- **Cause:** Forgetting that `<%@ page session="false" %>` (Topic 22/23) would make the `session` implicit object unavailable entirely — a rare but real configuration to double-check if `session`-related code mysteriously fails to compile on a specific page.
- **Fix:** Confirm the page directive doesn't disable session participation before debugging further.

---

That completes **Session Handling in JSP** — largely a disciplined application of Topic 11's `HttpSession` mechanics and Topic 24's EL scope-access, organized around one clear architectural rule: **Servlets write session state, JSPs only read it for display.**

**Next up per your module order:** **Include Mechanisms** — a focused, final consolidation comparing all the "include" approaches encountered so far (Include Directive, `<jsp:include>`, `RequestDispatcher.include()`) side-by-side with decision guidance, before moving into **MVC with JSP** as a dedicated capstone topic for the module.

Say **"Next"** to continue.