# TOPIC 10: Cookies

## CONCEPT

### Why this concept exists

HTTP is stateless (Topic 7). Cookies are the **original, foundational mechanism** browsers and servers use to simulate state across otherwise-independent requests. Now we formalize them fully — both as the low-level building block behind sessions, **and** as a standalone tool for storing small pieces of data directly on the client's browser.

### What problem it solves

Without cookies, every single request would be a "first contact" — the server would have no way to recognize *"this is the same browser that sent me a request 3 seconds ago."* Cookies solve this by having the **server** send a small piece of data to the browser (`Set-Cookie` header), which the browser **automatically stores** and **automatically re-sends** with every subsequent request to that same domain (`Cookie` header) — no extra code needed on the browser side; this behavior is built into the HTTP protocol itself.

### Real-world analogy

Think of a **coat check at a theater**. When you hand over your coat, the attendant gives you a small numbered ticket (the cookie). You keep that ticket in your pocket (browser storage). Every time you interact with the coat check counter again that evening (subsequent requests), you show your ticket, and the attendant instantly knows which coat is yours (server recognizes your session/identity) — without you having to re-describe your coat every single time.

---

## Cookies vs. Sessions — Clarifying the Relationship

This is a frequently confused pairing, so let's be precise: **a cookie is not the same thing as a session — a cookie is the *mechanism* that typically carries a session's identifier.**

```
HttpSession object          ←── lives entirely on the SERVER, in memory
        │
        │ identified by a unique session ID
        ▼
JSESSIONID cookie            ←── lives on the CLIENT (browser), just a small string
```

When you call `request.getSession()` (Topic 7), the container:
1. Creates the `HttpSession` object server-side.
2. Generates a unique ID (e.g., `A1B2C3D4E5F6...`).
3. Sends a `Set-Cookie: JSESSIONID=A1B2C3D4E5F6...` header in the response.
4. The browser stores this cookie and sends it back (`Cookie: JSESSIONID=A1B2C3D4E5F6...`) on every future request to your app.
5. The container reads this incoming cookie, extracts the ID, and looks up the matching `HttpSession` object in its server-side memory.

**This is why session management "just works" without you ever manually touching cookies** — the container handles the `JSESSIONID` cookie's creation and reading entirely automatically. But you can **also** create and manage your **own custom cookies** directly, for purposes unrelated to session tracking (e.g., "remember my username for next time," "remember the user's theme preference") — that's what this topic teaches you to do explicitly.

---

## The `Cookie` Class — Syntax and Methods

```java
import jakarta.servlet.http.Cookie;
```

### Creating and Sending a Cookie

```java
Cookie cookie = new Cookie("username", "rahul123");
cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days, in seconds
cookie.setPath("/myapp");            // available across the whole app
response.addCookie(cookie);
```

| Method | Purpose |
|---|---|
| `new Cookie(String name, String value)` | Constructor — creates a cookie with a name and value |
| `setMaxAge(int seconds)` | How long (in seconds) the browser should retain this cookie. `0` = delete immediately. **Not calling this at all** = a **session cookie** (deleted when the browser closes — see below). Negative value = same as not setting it. |
| `setPath(String path)` | Restricts which URL paths on your domain will have this cookie sent back to them |
| `setDomain(String domain)` | Restricts which domain(s) receive this cookie |
| `setHttpOnly(boolean flag)` | If `true`, JavaScript **cannot** access this cookie (`document.cookie` won't see it) — a critical **security** setting, discussed below |
| `setSecure(boolean flag)` | If `true`, cookie is only sent over **HTTPS** connections, never plain HTTP |
| `getValue()` / `getName()` | Read the cookie's current value/name |

### Reading Cookies from an Incoming Request

```java
Cookie[] cookies = request.getCookies(); // may return null if NO cookies sent at all

if (cookies != null) {
    for (Cookie c : cookies) {
        if (c.getName().equals("username")) {
            String username = c.getValue();
            // use it
        }
    }
}
```

**Important:** `request.getCookies()` returns `null` (not an empty array) if the client sent **zero** cookies — always null-check before iterating, exactly the same defensive pattern you learned for `getParameter()` (Topic 1) and `getAttribute()` (Topic 6).

**There's also no `getCookie(String name)` convenience method** in the standard API — you must **loop and compare names manually**, as shown above.

---

## Session Cookies vs. Persistent Cookies

| Type | How created | Lifetime |
|---|---|---|
| **Session Cookie** | `setMaxAge()` never called (or called with a negative value) | Deleted automatically when the **browser is closed** — this is exactly how `JSESSIONID` behaves by default |
| **Persistent Cookie** | `setMaxAge(positiveSeconds)` called | Survives browser restarts, stored on disk, until the specified expiry time passes |

**Practical example — "Remember Me" checkbox on a login form**, a classic real-world use case tying this directly to your Module 4 Login System project:

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String rememberMe = request.getParameter("rememberMe"); // checkbox value, e.g., "on" or null

    boolean isValid = LoginService.validateCredentials(username, password);

    if (isValid) {
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", username);

        if ("on".equals(rememberMe)) {
            Cookie rememberCookie = new Cookie("rememberedUsername", username);
            rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            rememberCookie.setPath("/myapp");
            response.addCookie(rememberCookie);
        }

        response.sendRedirect("dashboard");
    } else {
        request.setAttribute("errorMessage", "Invalid credentials.");
        request.getRequestDispatcher("/WEB-INF/views/loginError.jsp").forward(request, response);
    }
}
```

Then, on the login **page itself** (`login.html` would need to become a JSP or be served by a Servlet to do this dynamically — a small foreshadowing of why static HTML has limits and JSP becomes necessary, a theme for Module 2):

```java
// In a Servlet serving the login page:
Cookie[] cookies = request.getCookies();
String rememberedUsername = null;

if (cookies != null) {
    for (Cookie c : cookies) {
        if (c.getName().equals("rememberedUsername")) {
            rememberedUsername = c.getValue();
        }
    }
}
request.setAttribute("rememberedUsername", rememberedUsername);
// forward to a JSP that pre-fills the username field using this attribute
```

---

## Security Considerations (Genuinely Important, Not Optional Reading)

**`HttpOnly` flag:**
```java
cookie.setHttpOnly(true);
```
Prevents client-side JavaScript from reading the cookie via `document.cookie`. This is a critical defense against **XSS (Cross-Site Scripting)** attacks — if an attacker manages to inject malicious JavaScript into your page, `HttpOnly` cookies (like session IDs) remain inaccessible to that script, limiting the damage. **Best practice: always set `HttpOnly` on session-related or sensitive cookies.**.

**`Secure` flag:**
```java
cookie.setSecure(true);
```
Ensures the cookie is only ever transmitted over HTTPS, never plain HTTP — preventing it from a **man-in-the-middle** risk. In production systems handling any sensitive data, this should always be `true`.

**Never store passwords or sensitive data directly in a cookie**, even encoded — cookies are stored in plain text on the user's machine and can be inspected/modified by the user themselves (via browser dev tools) or intercepted. In our "Remember Me" example above, we stored only the **username** (not the password) as a convenience/UX feature — actual re-authentication still requires the session mechanism, not the cookie alone.

---

## EXECUTION FLOW — Complete Cookie Round-Trip

```
Request 1: User logs in with "Remember Me" checked
        │
        ▼
LoginServlet creates Cookie("rememberedUsername", "rahul123"), setMaxAge(30 days)
        │
        ▼
response.addCookie(cookie) → adds "Set-Cookie: rememberedUsername=rahul123; Max-Age=2592000; Path=/myapp"
   header to the HTTP response
        │
        ▼
Browser receives response, sees Set-Cookie header, stores it on disk
        │
        ▼
[User closes browser, comes back the next day]
        │
        ▼
Request 2: Browser loads login page again
        │
        ▼
Browser automatically attaches: "Cookie: rememberedUsername=rahul123" header
   to this new request (no code needed on browser side — built into HTTP)
        │
        ▼
Servlet calls request.getCookies(), finds "rememberedUsername", 
   pre-fills the username field on the login form
```

---

## COMMON ERRORS

**Error: `NullPointerException` when iterating `request.getCookies()`**
```java
for (Cookie c : request.getCookies()) { ... } // NPE if client sent zero cookies
```
- **Fix:** Always null-check `request.getCookies()` before looping, as shown above.

**Error: Cookie not appearing on subsequent requests**
- **Common cause 1:** `setPath()` was set too narrowly (e.g., `/myapp/admin`), so the cookie isn't sent back for requests to `/myapp/login` (a different path). 
- **Common cause 2:** `addCookie()` was called **after** the response was already committed (same rule as headers in Topic 8) — cookies are sent via a header, so they're subject to the identical "must set before writing body" ordering rule.
- **Fix:** Set `setPath("/")` or your app's root context path if you want the cookie available app-wide, and always call `addCookie()` before any `getWriter()` output.

**Error: Assuming cookies are secure/tamper-proof**
- **Cause:** A common beginner misconception — storing something like `isAdmin=true` in a plain cookie, trusting it on the next request.
- **Symptom:** Any user can open browser dev tools, manually edit that cookie's value, and grant themselves elevated privileges — a severe security vulnerability.
- **Fix:** Never trust cookie values for authorization decisions without server-side verification (e.g., checking against the actual session-stored, server-controlled user role, not a client-editable cookie value).