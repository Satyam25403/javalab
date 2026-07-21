# TOPIC 1 (continued): Execution Flow

## STEP 8 — EXECUTION FLOW (Deep Object-Level Trace)

We now trace **exactly** what happens, object by object, from the moment the browser sends our login POST request to the moment the final rendered response reaches the browser. This ties together everything from Steps 1–7 into one continuous, concrete story.

---

### 8.1 — The Complete Journey

**Scenario:** User is on `login.html`, types `username=admin`, `password=admin123`, and clicks Submit.

---

**PHASE 1: Browser Constructs and Sends the Request**

1. Browser reads the form's `action="login"` and `method="post"` attributes.
2. Browser URL-encodes the form field values into a request body:
   ```
   username=admin&password=admin123
   ```
3. Browser constructs a raw HTTP request:
   ```
   POST /myapp/login HTTP/1.1
   Host: localhost:8080
   Content-Type: application/x-www-form-urlencoded
   Content-Length: 32

   username=admin&password=admin123
   ```
4. This raw text is sent over a **TCP socket connection** to `localhost:8080`.

---

**PHASE 2: Tomcat Receives the Request**

5. Tomcat's **Connector** component (listening on port 8080, configured in `server.xml` as we saw in Step 5.5) accepts the incoming TCP connection and reads the raw bytes.
6. The Connector's HTTP parser converts the raw text into Tomcat's internal `org.apache.catalina.connector.Request` object — this is Tomcat's own internal representation, which will later be wrapped/exposed to your code as the standard `HttpServletRequest` interface.
7. Tomcat's internal routing hierarchy processes this request through several nested components, in order:
   ```
   Connector → Engine → Host → Context → Wrapper
   ```
   - **Engine** — the top-level container, represents the entire Tomcat instance's request-processing entry point.
   - **Host** — represents a virtual host (e.g., `localhost`) — matched from the `Host` header.
   - **Context** — represents your specific deployed web application (`/myapp`) — matched from the URL path prefix.
   - **Wrapper** — represents the specific Servlet definition (`LoginServlet`) — matched via the **Mapper** component, which compares the remaining URL path (`/login`) against all registered `@WebServlet` URL patterns and `web.xml` `<url-pattern>` entries in this Context.

8. Having identified `LoginServlet` as the target, Tomcat checks: does an instance of `LoginServlet` already exist in memory for this Context? (From Step 2: this check happens on every request, but object creation only happens once.)
   - If this is the **first ever** request to `/login` since the app started: Tomcat loads the class via the WebappClassLoader, instantiates it via reflection, and calls `init()` (empty here, since we didn't override it in the advanced version — no explicit startup logic needed for this Servlet).
   - Since `login.html` was likely accessed first (a static file, not involving this Servlet at all) and this is the *first* form submission, this is indeed the first hit — so class loading + instantiation + `init()` happens right now, before proceeding.

---

**PHASE 3: Thread Assignment and Object Creation**

9. Tomcat's internal thread pool (the **Executor**, configured via `server.xml`'s Connector settings) assigns an **available worker thread** to handle this specific request. Let's call it `http-nio-8080-exec-3` (Tomcat's real thread-naming convention).
10. This thread wraps Tomcat's internal `Request`/`Response` objects into the standard `HttpServletRequest` and `HttpServletResponse` interface implementations — **fresh objects, created specifically for this request**, as established in Step 2.

---

**PHASE 4: Servlet Lifecycle Method Invocation**

11. The thread calls `LoginServlet.service(request, response)` — inherited from `HttpServlet`.
12. `service()` calls `request.getMethod()` internally, gets back the string `"POST"`, and dispatches to `doPost(request, response)`.
13. **Inside `doPost()`:**
    - `request.getParameter("username")` is called. **First call to any `getParameter()` method on this request** triggers Tomcat to **parse the request body** (since `Content-Type` is `application/x-www-form-urlencoded`) — splitting on `&`, then `=`, URL-decoding each key/value pair, and populating an internal parameter map. This map is then cached on the request object, so subsequent `getParameter()` calls (like our next line, for `password`) reuse this already-parsed map rather than re-parsing.
    - Returns `"admin"`. Assigned to local variable `username` — **on the stack of thread `exec-3`**, as established in Step 2.
    - `request.getParameter("password")` similarly returns `"admin123"`, stored in local variable `password`.

---

**PHASE 5: Model Invocation**

14. `LoginService.validateCredentials(username, password)` is called — a **static method call**, meaning no object instantiation of `LoginService` occurs at all (static methods belong to the class itself, not any instance).
15. Inside `validateCredentials()`: null/empty checks pass (both values are non-null, non-empty). Then `VALID_USERNAME.equals(username)` evaluates `"admin".equals("admin")` → `true`. Similarly for password → `true`. Both `true` → method returns `true`.
16. Back in `doPost()`, local variable `isValid` is set to `true`.

---

**PHASE 6: Preparing the Forward**

17. Since `isValid` is `true`: `request.setAttribute("username", username)` is called. This stores the key `"username"` mapped to value `"admin"` inside the `HttpServletRequest` object's **internal attribute map** (a `Map<String, Object>` internally). This is **different** from `getParameter()`'s parameter map — parameters come **from the client** (read-only, set by the browser), while attributes are a **server-side storage mechanism** you explicitly set, primarily for passing data between Controller and View.
18. `targetView` is set to `"/WEB-INF/views/welcome.jsp"`.

---

**PHASE 7: The Forward Operation**

19. `request.getRequestDispatcher("/WEB-INF/views/welcome.jsp")` is called. Internally, Tomcat's Context component resolves this path against the deployed application's file structure, confirms `welcome.jsp` exists at that location, and returns a `RequestDispatcher` object wrapping a reference to that JSP resource.
20. `dispatcher.forward(request, response)` is called. **Critically:** this is a **server-side, in-process method call** — no new HTTP request is created, no new TCP connection, no round-trip to the browser. The **same** `request` and `response` objects (with the attribute we just set still intact) are simply handed off to the JSP's execution machinery.
21. **Internally, here's what "forwarding to a JSP" actually triggers** (full JSP compilation details are formally covered in Module 2, but here's the essential preview): if this is the JSP's first invocation since deployment, Tomcat's **Jasper** component (Tomcat's built-in JSP engine) translates `welcome.jsp` into a generated Java Servlet source file (yes — every JSP ultimately **becomes** a Servlet internally), compiles it into a `.class` file (stored in Tomcat's `work/` directory, as we saw in the Tomcat folder structure in Step 4.3), loads that generated Servlet class, instantiates it, and calls **its** `service()` method — passing along the **same** `request`/`response` objects.
22. Inside that generated JSP-Servlet's execution, the `${username}` EL expression (from our `welcome.jsp`) evaluates by calling `request.getAttribute("username")` internally, retrieves `"admin"`, and writes `Login successful! Welcome, admin.` into the response body.

---

**PHASE 8: Response Assembly and Return**

23. Control returns from the JSP-Servlet's `service()` call back up through the `forward()` call, back into our original `LoginServlet.doPost()` method — which has no more code left to execute, so it returns.
24. Control returns to `HttpServlet.service()`, which returns to Tomcat's request-processing pipeline.
25. Tomcat flushes the completed response body (the HTML the JSP wrote) along with appropriate headers (`Content-Type: text/html`, `Content-Length`, status `200 OK`) back through the Connector, over the same TCP socket, to the browser.
26. Thread `exec-3` is **released back to the thread pool**, available for the next incoming request — it is **not destroyed**.

---

**PHASE 9: Browser Renders**

27. Browser receives the HTTP response, parses the HTML, and renders: **"Login successful! Welcome, admin."**
28. Critically: the browser's address bar still shows `http://localhost:8080/myapp/login` — **not** any URL referencing `welcome.jsp` — because, as emphasized in Step 7.14, `forward()` is entirely server-side and invisible to the browser.

---

### 8.2 — Every Object Involved (Consolidated List)

| Object | Created By | Lifetime |
|---|---|---|
| `LoginServlet` instance | Tomcat, via reflection | Lives for entire app lifetime (until `destroy()`) |
| `HttpServletRequest` | Tomcat, per request | Lives only for this one request (and anything it's forwarded to) |
| `HttpServletResponse` | Tomcat, per request | Same as above |
| `RequestDispatcher` | Returned by `request.getRequestDispatcher()` | Short-lived, local to this method call |
| Worker Thread (`exec-3`) | Tomcat's thread pool | Reused across many unrelated requests over time |
| Generated JSP-Servlet instance (`welcome_jsp`) | Tomcat's Jasper engine | Lives for entire app lifetime, just like any Servlet, created once |
| `username`, `password`, `isValid` (local variables) | Your code, inside `doPost()` | Live only on the thread's stack, destroyed when `doPost()` returns |

---

### 8.3 — Key Takeaways From This Trace

1. **One request = one thread = one pass through the pipeline**, but the **Servlet object itself is reused** across all requests, exactly as emphasized since Step 1.
2. **`forward()` does not create a new HTTP request/response cycle** — it's a single request, handled collaboratively by multiple server-side components (Controller Servlet + View JSP), sharing the same `request`/`response` objects.
3. **JSPs are, under the hood, just Servlets** — generated and compiled by Tomcat automatically. This single fact resolves a huge number of "how is JSP different from Servlet, really?" confusions you'll have going into Module 2.
4. **Request attributes (`setAttribute`) are the bridge** connecting Controller logic to View rendering in the MVC pattern — this is the mechanism, not magic.

