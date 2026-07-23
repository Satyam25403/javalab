# TOPIC 12: URL Rewriting

## CONCEPT

### Why this concept exists

Topic 11 ended by flagging the problem: **if a client has cookies disabled, `JSESSIONID` never gets stored or sent back**, and every request looks like a brand-new session to the server — session-based login, carts, and any stateful feature silently breaks. URL Rewriting exists as the Servlet API's **built-in fallback mechanism** for exactly this scenario — encoding the session ID directly into the URL itself, rather than relying on the browser's cookie storage.

### What problem it solves

Cookies are stored **client-side**, entirely at the browser's/user's discretion — a user can disable them, a corporate firewall/proxy can strip them, or privacy-focused browser extensions can block them. URL Rewriting solves the "what if cookies aren't available?" problem by embedding the session identifier as **part of the URL path itself**, which travels with the request regardless of cookie support, since it's just... part of the URL.

### Real-world analogy

Continuing the coat-check analogy: if the numbered ticket (cookie) gets lost or the attendant refuses to accept tickets today, URL Rewriting is like the attendant instead **writing your ticket number directly onto every single door and signpost you'll walk past** in the building — so wherever you go, the number travels *with your path*, rather than in your pocket.

---

## How It Looks in Practice

A normal URL:
```
http://localhost:8080/myapp/dashboard
```

The **same** URL, with the session ID rewritten into it:
```
http://localhost:8080/myapp/dashboard;jsessionid=A1B2C3D4E5F6G7H8
```

Notice the `;jsessionid=...` segment — this is a **path parameter** (technically called a "matrix parameter" in URL terminology), appended after the path but before any query string. When a request arrives with this segment present, Tomcat extracts the session ID from it, exactly as it would from the `JSESSIONID` cookie, and resolves the correct `HttpSession` object.

---

## SYNTAX — `encodeURL()` and `encodeRedirectURL()`

The Servlet API does **not** expect you to manually string-concatenate `;jsessionid=...` yourself. Instead, `HttpServletResponse` provides two methods that do this **conditionally and automatically**:

```java
String url = response.encodeURL("dashboard");
```

| Method | Use for |
|---|---|
| `response.encodeURL(String url)` | Wrapping URLs used in **regular links** (e.g., inside `<a href="...">`, or any URL your Servlet/JSP writes into the HTML body) |
| `response.encodeRedirectURL(String url)` | Wrapping URLs specifically passed into `response.sendRedirect(...)` |

**Why two separate methods?** Because `sendRedirect()` (Topic 9) involves a distinct HTTP mechanism (a `Location` header triggering a new browser request) compared to a link embedded in HTML body content — the Servlet spec deliberately provides a dedicated method matched to each output context, even though both do conceptually the same underlying job.

**Critical behavior — these methods are "smart," not blind string concatenation:**

```java
String link = response.encodeURL("dashboard");
```

Internally, this method checks: *"Does this client's session tracking currently rely on cookies, or not?"*
- **If cookies are working normally** (the common case — client already has a valid `JSESSIONID` cookie, or cookie support is functioning): `encodeURL()` returns the URL **completely unchanged** — no `;jsessionid=...` is appended, since it's unnecessary.
- **If the container detects cookies are NOT being used** (e.g., this is the very first response in a session and it's unknown yet whether the client accepts cookies, or cookies have been explicitly disabled): `encodeURL()` appends the `;jsessionid=...` segment automatically.

**This is why you should *always* wrap URLs with `encodeURL()`/`encodeRedirectURL()` as a defensive best practice**, even if you assume cookies will work — it costs nothing when cookies work fine (URL passes through unchanged), and it transparently provides the fallback when they don't.

---

## Code Example — Applying URL Rewriting Correctly

```java
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            // Encoding a redirect URL
            String loginUrl = response.encodeRedirectURL("login.html");
            response.sendRedirect(loginUrl);
            return;
        }

        String username = (String) session.getAttribute("loggedInUser");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Welcome, " + username + "!</h2>");

        // Encoding a normal link written into the HTML body
        String logoutLink = response.encodeURL("logout");
        out.println("<a href='" + logoutLink + "'>Logout</a>");
    }
}
```

**Trace through both scenarios:**

*Scenario A — cookies work normally (the vast majority of real-world traffic):*
```java
response.encodeURL("logout") → returns "logout" (unchanged)
```
Output HTML: `<a href='logout'>Logout</a>` — completely normal.

*Scenario B — cookies disabled/unavailable:*
```java
response.encodeURL("logout") → returns "logout;jsessionid=A1B2C3D4E5F6G7H8"
```
Output HTML: `<a href='logout;jsessionid=A1B2C3D4E5F6G7H8'>Logout</a>` — clicking this link sends the session ID along via the URL itself, and the container's session lookup succeeds even without any cookie.

---

## Important Limitations and Practical Reality

1. **Only affects dynamically-generated links** — URL Rewriting only works for URLs your Servlet/JSP code actually passes through `encodeURL()`/`encodeRedirectURL()`. A **static** `login.html` file with a hardcoded `<a href="dashboard">` link (no Java/JSP processing) **cannot** have the session ID injected into it — this is a genuine, structural limitation, and another concrete reason why purely static HTML files have limits in a session-dependent application.

2. **Modern relevance is limited but non-zero:** In 2026, the vast majority of browsers accept cookies by default, and most modern web applications don't heavily rely on URL Rewriting in practice but understanding it solidifies your grasp of *how* sessions actually work under the hood.

3. **Security consideration:** Session IDs embedded directly in URLs are more exposed than cookies — URLs get logged in server access logs, browser history, and can leak via the `Referer` header (Topic 8) if a user clicks an external link from a page containing a `;jsessionid=...` URL. This is a legitimate reason modern applications prefer cookie-based session tracking and treat URL Rewriting purely as a fallback.

---

## EXECUTION FLOW

```
Client sends first request (unknown cookie support)
        │
        ▼
Servlet calls request.getSession() → new session created, ID = XYZ789
        │
        ▼
Container doesn't yet know if THIS client will honor the
Set-Cookie header it's about to send
        │
        ▼
Servlet writes a link using response.encodeURL("nextPage")
        │
        ▼
Container checks internal session-tracking mode for this response:
   - Response WILL include Set-Cookie: JSESSIONID=XYZ789 (attempt cookie-based tracking)
   - AND, defensively, encodeURL() ALSO appends ;jsessionid=XYZ789 to the returned URL
   (this dual approach covers both possibilities until the container is certain
    which mechanism the client will actually honor)
        │
        ▼
Client's NEXT request:
   - If cookies worked: Cookie: JSESSIONID=XYZ789 header present
   - If cookies did NOT work: the ;jsessionid=XYZ789 embedded in the URL the user clicked
     is present in the path instead
        │
        ▼
Either way, container successfully resolves the correct HttpSession
```

---

## COMMON ERRORS

**Error: Session appears to "reset" on every single request, despite session code being correct**
- **Cause:** Testing in a browser/environment with cookies fully disabled, **while** all links in the application are static HTML (not passed through `encodeURL()`), so no fallback mechanism is active at all.
- **Fix:** Ensure all dynamically-generated links pass through `encodeURL()`, and recognize that purely static HTML pages cannot participate in this fallback — they'd need to become JSP-served content to carry the session ID forward.

**Error: Forgetting to use `encodeRedirectURL()` specifically for redirects**
```java
response.sendRedirect(response.encodeURL(loginUrl)); // WRONG method used
```
- **Cause:** Using `encodeURL()` instead of `encodeRedirectURL()` when the destination is passed into `sendRedirect()`. While in many container implementations this may still work due to overlapping internal logic, it's **not spec-correct**.
- **Fix:** Always match `encodeRedirectURL()` with `sendRedirect()`, and `encodeURL()` with regular HTML links — this pairing is worth memorizing precisely as shown.