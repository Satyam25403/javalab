# TOPIC 13: Hidden Form Fields

## CONCEPT

### Why this concept exists

We've now covered three state-management mechanisms: Session scope (server-side memory), Cookies (client-side storage), and URL Rewriting (session ID in the URL). **Hidden Form Fields** are a fourth, much simpler technique — a way to pass a specific piece of data **from one page to the next**, embedded invisibly inside an HTML form, with **zero server-side memory** and **zero client-side storage** involved at all.

### What problem it solves

Sometimes you don't need a full session or a cookie — you just need to carry **one specific value** forward through a **single, immediate** multi-step form flow (e.g., a multi-page checkout process, or a "confirm your details" page before final submission).

### Real-world analogy

Think of filling out a **multi-part paper form** at a government office. Page 1 asks for your name and application type; when you flip to Page 2, there's a small pre-printed box in the corner that already says "Application Type: Passport Renewal" — carried forward from what you selected on Page 1, without anyone needing to "remember" it separately in a filing cabinet (server session) — it's just physically written on the page itself.

---

## Syntax

```html
<input type="hidden" name="fieldName" value="someValue">
```

- Renders **nothing visible** in the browser — the user cannot see or interact with it directly through the normal UI.
- Behaves **exactly like any other form field** when the form is submitted — its `name`/`value` pair is included in the request body (or query string, if `method="get"`), retrievable via `request.getParameter("fieldName")`, exactly as you've done since Topic 1.

---

## Code Example — Multi-Step Form Using Hidden Fields

**Scenario:** A two-step registration flow — Step 1 collects username/email, Step 2 collects address, but we need to carry the Step 1 data forward to Step 2 without a session, purely via hidden fields.

**Step 1 — `registerStep1.jsp`** (conceptually; full JSP syntax formally taught in Module 2 — treat this as HTML for now):
```html
<html>
<body>
<h2>Registration - Step 1</h2>
<form action="registerStep2" method="post">
    <label>Username:</label>
    <input type="text" name="username" required><br>

    <label>Email:</label>
    <input type="email" name="email" required><br>

    <input type="submit" value="Next">
</form>
</body>
</html>
```

**Servlet handling Step 1 → generates Step 2's form dynamically, embedding hidden fields:**

```java
@WebServlet("/registerStep2")
public class RegisterStep2Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Registration - Step 2</h2>");
        out.println("<form action='registerFinish' method='post'>");

        // Carry Step 1's data forward invisibly
        out.println("<input type='hidden' name='username' value='" + username + "'>");
        out.println("<input type='hidden' name='email' value='" + email + "'>");

        out.println("<label>Address:</label>");
        out.println("<input type='text' name='address' required><br>");

        out.println("<input type='submit' value='Finish Registration'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}
```

**Servlet handling final submission — receives ALL three fields together:**

```java
@WebServlet("/registerFinish")
public class RegisterFinishServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Notice: username and email arrive here NOT because of session/cookie,
        // but purely because they rode along as hidden fields in Step 2's form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Registration Complete!</h2>");
        out.println("<p>Username: " + username + "</p>");
        out.println("<p>Email: " + email + "</p>");
        out.println("<p>Address: " + address + "</p>");
        out.println("</body></html>");
    }
}
```

**Key observation:** No `HttpSession` object was created anywhere in this entire flow. No cookie was set. The data traveled **purely through the sequence of form submissions themselves** — each step's form carries forward everything collected so far.

---

## Critical Security Warning — This Is Genuinely Important

Notice this line in `RegisterStep2Servlet`:
```java
out.println("<input type='hidden' name='username' value='" + username + "'>");
```

**This is directly inserting user-submitted data back into HTML output without any sanitization** — this is a textbook **Cross-Site Scripting (XSS)** vulnerability. If a malicious user submitted a `username` value like:
```
"><script>alert('hacked')</script>
```
It would get embedded directly into the generated HTML, and the browser would **execute that script** when rendering Step 2's page — because the raw string breaks out of the `value='...'` attribute and injects a new, malicious tag.

**This is a genuinely important lesson, not a side note:** "Hidden" only means **invisible in the rendered UI** — it does **not** mean "hidden from the user" in any security sense. **Anyone can:**
1. View the page source and see the hidden field's exact value.
2. Open browser developer tools and **edit** the hidden field's value directly before submitting.
3. Bypass your HTML form entirely and submit a raw HTTP POST request with **any value they want** for that field name, using a tool like Postman or `curl` — your server has **no way to know** whether a request "really" came from your Step 1 form or was fabricated entirely.

**Correct practices:**
1. **Always validate/re-validate data on the server** for every step, even data that "should" already be validated from a previous step — never trust that a hidden field's value is what your own earlier form actually put there.
2. **Escape HTML special characters** before writing any user-submitted value back into HTML output (`<`, `>`, `"`, `'`, `&` need to be converted to their HTML entity equivalents — `&lt;`, `&gt;`, etc.) — JSTL's `<c:out>` tag (Module 2) does this **automatically**, which is one of many reasons raw Servlet HTML-printing is discouraged in favor of proper JSP/JSTL usage.
3. **Never use hidden fields for anything sensitive or trust-critical** — e.g., never do this:
   ```html
   <input type="hidden" name="price" value="999.99">
   ```
   expecting the price to be trustworthy on the next page — a malicious user can trivially edit this to `0.01` before submitting. Prices, permissions, user IDs used for authorization, and similar sensitive values must always be **re-derived or re-validated server-side** (e.g., look up the actual price from the database again using a product ID, rather than trusting a hidden "price" field at face value).

---

## Hidden Form Fields vs. Session vs. Cookies — When to Use Which

| Mechanism | Best for | Server memory used? | Survives across separate browser tabs/visits? | Tamper risk |
|---|---|---|---|---|
| **Hidden Form Fields** | Carrying data through an immediate, linear multi-step form flow | No | No — only within this exact form chain | High — fully client-editable |
| **Session** | Login state, shopping cart, anything spanning many separate page visits over time | Yes (per user) | Yes, until timeout | Low — data lives server-side, only the ID is client-visible |
| **Cookies** | Small persistent preferences, "remember me," client-side tracking | No (server), Yes (client disk) | Yes, until expiry | Medium — client-editable, but doesn't grant access to server-side data directly unless misused for authorization |

**Practical guidance:** Use hidden fields only for **non-sensitive, re-validatable** data within a **single continuous form-flow**. For anything spanning independent page visits, or anything even slightly sensitive (user identity, permissions, prices, quantities affecting billing), use session scope instead, precisely because it keeps the actual trusted data server-side, exposing only an opaque, container-managed ID to the client.

---

## EXECUTION FLOW

```
Request 1: GET registerStep1.jsp → static form rendered
        │
        ▼
Request 2: POST /registerStep2  (username=rahul, email=rahul@x.com)
        │
        ▼
RegisterStep2Servlet reads parameters, WRITES them back as hidden
fields in the HTML it generates for Step 2's form
        │
        ▼
Browser renders Step 2 — user sees only the "Address" field,
but the page's underlying HTML secretly contains the hidden
username/email fields too
        │
        ▼
Request 3: POST /registerFinish
   Body: username=rahul&email=rahul@x.com&address=123+Main+St
   (ALL THREE fields present — the hidden ones travelled invisibly)
        │
        ▼
RegisterFinishServlet reads all three via getParameter() — no
session, no cookie, no server memory was ever involved
```

---

## COMMON ERRORS

**Error: Data appears "lost" between steps**
- **Cause:** Forgetting to add a hidden field for a value collected earlier, or a `name` mismatch between what was collected and what the hidden field re-declares.
- **Fix:** Carefully audit that every piece of data needed downstream is either re-collected as a visible field OR explicitly carried forward as a hidden field at each step.

**Error: XSS vulnerability from directly embedding unescaped user input (already covered in depth above)**
- **Fix:** Escape HTML output manually (or, better, use JSTL `<c:out>` once you reach Module 2) whenever writing user-submitted values back into generated HTML.

**Error: Trusting a hidden field's value for a security/business-critical decision**
- Already covered above (the "price" example) — **Fix:** re-validate/re-derive sensitive values server-side, never trust the client's copy at face value.