# TOPIC 8: HttpServletRequest and HttpServletResponse — Complete Method Reference

## CONCEPT

### Why this concept exists

We've used pieces of both objects throughout (`getParameter()`, `setAttribute()`, `getWriter()`, `setContentType()`), but never catalogued them systematically. These two objects are the **primary interface** between your Java code and the raw HTTP protocol — every single method on them corresponds to a specific piece of the HTTP request or response format.

### Real-world analogy

If a Servlet is a chef, `HttpServletRequest` is the **complete, detailed order slip** the waiter hands the chef — not just "what dish," but also who's asking, what time, any allergies, table number, previous orders from this table tonight. `HttpServletResponse` is the **blank plate and everything the chef controls about presentation** — what goes on it, what temperature it's served at (status), and any special instructions attached to the tray (headers) before it goes out.

---

## HttpServletRequest — Complete Method Catalogue

### Category 1: Request Line Information

| Method | Returns | Example Output | Purpose |
|---|---|---|---|
| `getMethod()` | `String` | `"POST"` | The HTTP method used |
| `getRequestURI()` | `String` | `/myapp/login` | Path portion of the URL, excluding domain/protocol |
| `getRequestURL()` | `StringBuffer` | `http://localhost:8080/myapp/login` | Full URL including protocol and domain |
| `getContextPath()` | `String` | `/myapp` | Your application's deployed context path |
| `getServletPath()` | `String` | `/login` | The path matched by the Servlet's URL pattern |
| `getQueryString()` | `String` | `id=5&sort=asc` | Raw query string (everything after `?`), unparsed |
| `getProtocol()` | `String` | `HTTP/1.1` | Protocol version used |

**Code example demonstrating the URL-component methods:**

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();

    out.println("Method: " + request.getMethod());
    out.println("Request URI: " + request.getRequestURI());
    out.println("Request URL: " + request.getRequestURL());
    out.println("Context Path: " + request.getContextPath());
    out.println("Servlet Path: " + request.getServletPath());
    out.println("Query String: " + request.getQueryString());
}
```

If accessed via `http://localhost:8080/myapp/hello?name=Rahul`, this prints:
```
Method: GET
Request URI: /myapp/hello
Request URL: http://localhost:8080/myapp/hello
Context Path: /myapp
Servlet Path: /hello
Query String: name=Rahul
```

### Category 2: Parameters (Client-Submitted Data)

| Method | Returns | Purpose |
|---|---|---|
| `getParameter(String name)` | `String` | Single value for a given parameter name (returns `null` if absent — Topic 1, Step 7.8) |
| `getParameterValues(String name)` | `String[]` | **All** values for a parameter that can have multiple (e.g., checkboxes with the same `name`) |
| `getParameterNames()` | `Enumeration<String>` | All parameter names present in this request |
| `getParameterMap()` | `Map<String, String[]>` | All parameters as a complete map — useful for iterating all form data generically |

**Code example — handling multi-value parameters (checkboxes):**

```html
<form action="hobbies" method="post">
    <input type="checkbox" name="hobby" value="Reading"> Reading
    <input type="checkbox" name="hobby" value="Cricket"> Cricket
    <input type="checkbox" name="hobby" value="Coding"> Coding
    <input type="submit">
</form>
```

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String[] selectedHobbies = request.getParameterValues("hobby");

    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();

    if (selectedHobbies == null) {
        out.println("No hobbies selected.");
    } else {
        out.println("Selected hobbies:");
        for (String hobby : selectedHobbies) {
            out.println("- " + hobby);
        }
    }
}
```

**Why `getParameter("hobby")` would be WRONG here:** if multiple checkboxes share `name="hobby"`, calling `getParameter("hobby")` (singular) only returns the **first** matched value, silently discarding the rest — a very common, silent (no exception!) beginner bug. Whenever a form field can legitimately submit **multiple** values under the same name (checkboxes, multi-select dropdowns), you **must** use `getParameterValues()`, not `getParameter()`.

---

### Category 3: Headers

| Method | Returns | Purpose |
|---|---|---|
| `getHeader(String name)` | `String` | Value of a specific HTTP header |
| `getHeaderNames()` | `Enumeration<String>` | All header names present |
| `getHeaders(String name)` | `Enumeration<String>` | All values for headers that can repeat |
| `getIntHeader(String name)` | `int` | Header value parsed directly as an int |
| `getDateHeader(String name)` | `long` | Header value parsed as a date (milliseconds since epoch) |

**Practical example — detecting client browser/device:**

```java
String userAgent = request.getHeader("User-Agent");
out.println("Client browser info: " + userAgent);
```

Common headers you'll actually use: `User-Agent` (browser/client identification), `Referer` (which page linked here — note the header name's famous historical **misspelling**, permanently baked into the HTTP spec), `Accept-Language` (client's preferred language), `Authorization` (credentials for authenticated APIs — relevant once you reach Spring Security).

---

### Category 4: Attributes (Server-Side Storage, Request Scope — Topic 7)

Already covered thoroughly: `setAttribute()`, `getAttribute()`, `removeAttribute()`, `getAttributeNames()`.

---

### Category 5: Session and Cookies (Full depth in dedicated upcoming topics)

| Method | Returns | Purpose |
|---|---|---|
| `getSession()` | `HttpSession` | Get-or-create session (Topic 7) |
| `getSession(boolean create)` | `HttpSession` or `null` | Conditional get-or-null |
| `getCookies()` | `Cookie[]` | All cookies sent by the client |

---

### Category 6: Client and Server Information

| Method | Returns | Purpose |
|---|---|---|
| `getRemoteAddr()` | `String` | Client's IP address |
| `getRemoteHost()` | `String` | Client's hostname (may require reverse DNS lookup — can be slow) |
| `getServerName()` | `String` | Server's hostname |
| `getServerPort()` | `int` | Port the request arrived on |
| `getLocale()` | `Locale` | Client's preferred locale, derived from `Accept-Language` header |

---

### Category 7: Body/Stream Access (Important for JSON APIs, relevant to your future REST/Spring work)

| Method | Returns | Purpose |
|---|---|---|
| `getReader()` | `BufferedReader` | Reads the request body as **character** data (text, JSON) |
| `getInputStream()` | `ServletInputStream` | Reads the request body as **raw bytes** (file uploads, binary data) |

**Important rule:** You can call **either** `getReader()` **or** `getInputStream()` on a given request — **never both** — calling the second after the first throws `IllegalStateException`, because the underlying stream can only be consumed once. This becomes directly relevant when you build JSON-consuming endpoints later (Spring Boot `@RequestBody` does this internally, wrapping exactly this mechanism).

**Preview example (reading a raw JSON body — foreshadowing REST APIs):**

```java
BufferedReader reader = request.getReader();
StringBuilder jsonBody = new StringBuilder();
String line;
while ((line = reader.readLine()) != null) {
    jsonBody.append(line);
}
System.out.println("Received JSON: " + jsonBody.toString());
```

---

## HttpServletResponse — Complete Method Catalogue

### Category 1: Status Codes

| Method | Purpose |
|---|---|
| `setStatus(int sc)` | Sets the HTTP status code (e.g., `200`, `201`) — for **success** responses |
| `sendError(int sc)` | Sets status code AND triggers the container's error-handling mechanism (your `<error-page>` mappings from Topic 5!) |
| `sendError(int sc, String msg)` | Same, with a custom message |

**Named constants (best practice, Topic 3):** `SC_OK` (200), `SC_CREATED` (201), `SC_NO_CONTENT` (204), `SC_BAD_REQUEST` (400), `SC_UNAUTHORIZED` (401), `SC_FORBIDDEN` (403), `SC_NOT_FOUND` (404), `SC_INTERNAL_SERVER_ERROR` (500).

**Critical distinction — `setStatus()` vs `sendError()`:**

```java
response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Just sets the number — YOU must still write body content
```
vs.
```java
response.sendError(HttpServletResponse.SC_NOT_FOUND); // Sets the number AND triggers <error-page> lookup (Topic 5)
```

`sendError()` hands control **back to the container**, which checks your `web.xml`'s `<error-page>` mappings and forwards to your custom error JSP automatically. `setStatus()` does **not** trigger this — you're expected to write the response body yourself. Use `sendError()` when you want your configured error pages to kick in; use `setStatus()` when you're building the full response body yourself (common in REST APIs, where you don't want an HTML error page, just a status code and a JSON error body).

---

### Category 2: Headers and Content Type

| Method | Purpose |
|---|---|
| `setContentType(String type)` | Sets `Content-Type` header (e.g., `"text/html"`, `"application/json"`) |
| `setHeader(String name, String value)` | Sets/overwrites a header |
| `addHeader(String name, String value)` | Adds a header **without** removing existing ones of the same name (for repeatable headers) |
| `setContentLength(int len)` | Explicitly sets `Content-Length` (usually auto-calculated, rarely needed manually) |
| `setCharacterEncoding(String charset)` | Sets response character encoding (e.g., `"UTF-8"`) |

---

### Category 3: Writing the Body

| Method | Returns | Purpose |
|---|---|---|
| `getWriter()` | `PrintWriter` | Character-based output (text/HTML/JSON) — what we've used throughout |
| `getOutputStream()` | `ServletOutputStream` | Byte-based output (images, files, binary data) |

Same **mutual exclusivity rule** as the request side: call only one of `getWriter()`/`getOutputStream()` per response.

---

### Category 4: Redirection

| Method | Purpose |
|---|---|
| `sendRedirect(String location)` | Sends an HTTP 302 status with a `Location` header, telling the **browser** to make a brand-new GET request to a different URL |

We used this in Topic 7's login example. Full comparison against `forward()` is coming in the next dedicated topic ("Forward vs Include" — actually forward vs redirect will be clarified there too), but here's the essential mechanical difference, precisely:

```java
response.sendRedirect("dashboard");
```
- Sends **HTTP 302 Found**, header `Location: /myapp/dashboard`, empty body, back to the **browser**.
- Browser then issues a **completely new GET request** to `/myapp/dashboard`.
- **Browser's URL bar changes.**
- **Request-scoped attributes are lost** (Topic 7) — it's an entirely new request.
- Slightly slower (two full round-trips: original request/response, then new request/response) versus `forward()`'s single round-trip.

---

### Category 5: Cookies (Full depth next topic)

| Method | Purpose |
|---|---|
| `addCookie(Cookie cookie)` | Adds a `Set-Cookie` header to the response |

---

## EXECUTION FLOW — Tracing a Complete Request/Response Method Usage

```
Request arrives: POST /myapp/login  (username=admin&password=admin123)
        │
        ▼
request.getMethod()         → "POST"
request.getParameter("username") → "admin"  (triggers lazy body parsing, Topic 1 Step 8)
request.getParameter("password") → "admin123"
request.getHeader("User-Agent")  → "Mozilla/5.0 ..."
        │
        ▼
[Your validation/business logic runs]
        │
        ▼
response.setContentType("text/html")   → sets Content-Type header
response.setStatus(200)                → (or default 200 if unset)
response.getWriter().println(...)       → writes response body
        │
        ▼
Response flushed back to browser with all set headers + status + body
```

---

## COMMON ERRORS

**Error: `IllegalStateException: getOutputStream() has already been called for this response`**
- **Cause:** Calling both `getWriter()` and `getOutputStream()` on the same response object (Category 3's mutual exclusivity rule).
- **Fix:** Choose one, based on whether you're writing text (`getWriter()`) or binary data (`getOutputStream()`), and use only that one consistently within a single request-handling method.

**Error: `IllegalStateException` when setting headers after response is already committed**
- **Cause:** Calling `response.setHeader()` or `response.setContentType()` **after** you've already started writing body content via `getWriter()` — once the response is "committed" (buffer flushed), headers can no longer be modified, since they've already been sent to the client.
- **Fix:** Always set **all** headers/content-type/status **before** writing any body content — a strict ordering rule worth internalizing as a habit.

**Error: `getParameter()` returning `null` for a multi-value field**
- Already covered above — using `getParameter()` instead of `getParameterValues()` for checkboxes/multi-selects.

**Error: Forgetting `sendError()` triggers `<error-page>` but `setStatus()` doesn't**
- **Symptom:** You call `response.setStatus(404)` expecting your custom 404 JSP to render, but instead get a blank or default page.
- **Fix:** Use `sendError(404)` if you want your configured `<error-page>` to activate; otherwise write the full body yourself after `setStatus()`.

