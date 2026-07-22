# TOPIC 3: doGet(), doPost(), doPut(), doDelete() — Complete HTTP Method Handling

## CONCEPT

### Why this concept exists

HTTP defines multiple **methods** (verbs) that express *intent* about what a client wants to do with a resource. `HttpServlet` provides a dedicated `doXxx()` method for each, rather than forcing you to inspect the method manually inside a single generic handler. This design lets you **cleanly separate logic by intent** — reading data, submitting data, updating data, and deleting data each get their own method, improving readability and aligning your code with HTTP's own semantics.

### The Four (Plus Others) Methods — Semantic Meaning

| Method | Intent | Idempotent? | Safe? (no side effects) | Typical Use |
|---|---|---|---|---|
| **GET** | Retrieve/read a resource | Yes | Yes | Loading a page, fetching a list, search |
| **POST** | Create a new resource / submit data | No | No | Login form, creating a new record |
| **PUT** | Update/replace an existing resource entirely | Yes | No | Updating a full user profile |
| **DELETE** | Remove a resource | Yes | No | Deleting a record |

**"Idempotent" explained precisely** (frequently misunderstood term, common interview question): An operation is idempotent if performing it **multiple times has the same effect as performing it once**. 
- `GET /users/5` — reading user 5 a hundred times doesn't change anything → idempotent.
- `PUT /users/5` with body `{name: "Rahul"}` — setting the name to "Rahul" a hundred times still leaves the name as "Rahul" → idempotent.
- `POST /users` — submitting this a hundred times creates **a hundred different user records** → **NOT** idempotent, each call has a cumulative new effect.
- `DELETE /users/5` — deleting it once removes it; "deleting" it again just confirms it's already gone (no *further* change) → considered idempotent by HTTP spec, even though the second call might return a 404.

**"Safe" explained:** A method is "safe" if it causes **no server-side state change at all** — only GET (and HEAD, OPTIONS) are safe. This is *why* browsers freely prefetch, cache, and re-issue GET requests without asking you, but will always warn you ("Are you sure you want to resubmit this form?") before repeating a POST.


### Why this matters for your Spring Boot future

This exact GET/POST/PUT/DELETE mapping is **precisely** how REST APIs are designed, and precisely what `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` in Spring Boot correspond to.

---

## SYNTAX — All Four Methods Side-by-Side

```java
package com.company.myapp.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/resource")
public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("GET: Retrieving resource data.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("POST: New resource created.");
        response.setStatus(HttpServletResponse.SC_CREATED); // 201
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("PUT: Resource fully updated.");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("DELETE: Resource removed.");
    }
}
```

### New elements explained

**`response.setStatus(HttpServletResponse.SC_CREATED)`**
- Explicitly sets the HTTP response status code to **201 Created** — the semantically correct status for a successful POST that created something (as opposed to the default `200 OK`, which is more generic). `HttpServletResponse` provides many such named constants (`SC_OK` = 200, `SC_NOT_FOUND` = 404, `SC_INTERNAL_SERVER_ERROR` = 500, etc.) — using these named constants instead of raw magic numbers like `201` is a **best practice**, improving readability and reducing typos.

### The Important Practical Catch: Browsers Can't Send PUT/DELETE from HTML Forms

This is a **crucial, often-missed practical detail**: plain HTML `<form method="...">` **only supports `GET` and `POST`** — there is no `method="put"` or `method="delete"` in HTML forms. This isn't a Servlet limitation; it's an **HTML specification limitation**.

**So how do you actually test/trigger `doPut()`/`doDelete()` in a classic Servlet app?**

1. **Using tools like Postman or cURL** (most common for testing):
```bash
curl -X PUT http://localhost:8080/myapp/resource
curl -X DELETE http://localhost:8080/myapp/resource
```

2. **Method override pattern via hidden field + Filter** (an older workaround, seen in some legacy frameworks) — a hidden form field like `<input type="hidden" name="_method" value="DELETE">` combined with a Filter that intercepts and reroutes — **generally NOT recommended for you to implement now**; I'm mentioning it only so you recognize the pattern if you see it in older codebases. Real REST clients (JavaScript `fetch()`, Postman, mobile apps, or Spring's `RestTemplate`) send PUT/DELETE directly at the HTTP protocol level — the HTML-form limitation simply doesn't apply outside of plain browser forms.


---

## INTERNAL WORKING — How the Container Decides Which Method to Call

Revisiting Topic 1 Step 6.4's `service()` dispatch logic, now completed with all methods:

```java
protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String method = req.getMethod();  // reads the actual HTTP method from the request line

    switch (method) {
        case "GET":    doGet(req, resp);    break;
        case "POST":   doPost(req, resp);   break;
        case "PUT":    doPut(req, resp);    break;
        case "DELETE": doDelete(req, resp); break;
        case "HEAD":   doHead(req, resp);   break;    // special: calls doGet() internally, then discards body
        case "OPTIONS":doOptions(req, resp);break;    // special: auto-generates Allow header
        case "TRACE":  doTrace(req, resp);  break;
        default:
            // Unknown/unsupported method
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
```

**Where does `req.getMethod()` get its value from?** All the way back in Phase 2 of Step 8's execution trace — Tomcat's Connector parses the **very first line** of the raw HTTP request (`PUT /myapp/resource HTTP/1.1`), extracts the method token (`PUT`), and stores it in the internal `Request` object, later exposed via `getMethod()`.

### `doHead()` — worth understanding briefly since it's automatically derived from `doGet()`

`HttpServlet`'s default `doHead()` implementation actually **calls your `doGet()` internally**, executes it fully, but then **discards the response body**, sending back only the headers (particularly `Content-Length`) that *would* have been sent. This lets HTTP clients check "does this resource exist, and how big is it?" without downloading the full body — you almost never need to override `doHead()` yourself; the inherited behavior is usually exactly what you want, automatically derived from whatever `doGet()` you wrote.

---

## COMMON ERRORS

**Error: Sending a PUT/DELETE request via an HTML form and getting confused**
```html
<form method="put" action="resource"> <!-- INVALID HTML -->
```
- **Cause:** As explained above, HTML simply doesn't support this — browsers silently fall back to treating it as `GET` (behavior varies by browser, but it's never actually a real PUT request).
- **Fix:** Use Postman/cURL for testing, or restructure the interaction as a POST with an explicit action parameter if you must support it from a plain browser form (a common real-world compromise in older-style non-REST apps).

**Error: 405 Method Not Allowed when testing via Postman**
- **Cause:** You sent a PUT/DELETE request, but the target Servlet doesn't override that specific `doXxx()` method — falls back to `HttpServlet`'s default, which returns 405.
- **Fix:** Confirm you've actually overridden the corresponding method, and double-check for typos in the method name (`doPut` not `doput` or `doPUT` — Java is case-sensitive, and a misspelled override just creates an unrelated, never-called method, exactly as flagged with `@Override` back in Topic 1 Step 7.3).

