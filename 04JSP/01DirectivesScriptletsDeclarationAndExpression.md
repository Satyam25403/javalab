# TOPIC 21: JSP Syntax — Directives, Scriptlet, Declaration, Expression

## CONCEPT

### Why this concept exists

Topic 20 showed one JSP expression (`<%= new java.util.Date() %>`) without formally categorizing it. JSP actually defines **several distinct tag types**, each translating into a **different part** of the generated Servlet class (Topic 20's `welcome_jsp.java`). Understanding exactly *which* tag generates *which* piece of Java code is what lets you predict JSP behavior precisely, rather than guessing.

### The Four Core Syntax Elements

| Syntax | Name | Generates... | Purpose |
|---|---|---|---|
| `<%@ ... %>` | **Directive** | Page-level configuration (not executable code) | Import statements, page settings, tag library references |
| `<% ... %>` | **Scriptlet** | Code placed directly inside `_jspService()`'s method body | Arbitrary Java logic, loops, conditionals |
| `<%! ... %>` | **Declaration** | Fields/methods declared at the **class level**, outside `_jspService()` | Instance variables, helper methods |
| `<%= ... %>` | **Expression** | A value passed to `out.print(...)`, inside `_jspService()` | Printing a single computed value inline |

---

## 1. Directives — `<%@ ... %>`

Directives configure **how the page itself is translated/behaves** — they don't produce output directly; they're instructions to the JSP engine (Jasper) itself. (Full deep-dive on each directive type is its own upcoming topic — this section introduces the **syntax category**.)

```jsp
<%@ page import="java.util.Date, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
```

**What this generates (conceptually), in the generated Servlet:**
```java
import java.util.Date;
import java.util.List;

public class mypage_jsp extends HttpJspBase {
    public void _jspService(...) {
        response.setContentType("text/html;charset=UTF-8");
        ...
    }
}
```

Directives affect the **surrounding generated class structure** (imports at the top, certain settings applied at the start of `_jspService()`), not a specific inline spot in the output.

---

## 2. Scriptlet — `<% ... %>`

**The most "raw Java" of the four** — code inside `<% %>` is inserted **verbatim** into the generated `_jspService()` method body, exactly where it appears in your JSP.

```jsp
<html>
<body>
<%
    int hour = new java.util.Date().getHours();
    String greeting;
    if (hour < 12) {
        greeting = "Good morning";
    } else if (hour < 18) {
        greeting = "Good afternoon";
    } else {
        greeting = "Good evening";
    }
%>
    <h2><%= greeting %>!</h2>
</body>
</html>
```

**What this generates (conceptually):**
```java
public void _jspService(HttpServletRequest request, HttpServletResponse response) {
    PrintWriter out = response.getWriter();
    out.write("<html>\n<body>\n");

    int hour = new java.util.Date().getHours();
    String greeting;
    if (hour < 12) {
        greeting = "Good morning";
    } else if (hour < 18) {
        greeting = "Good afternoon";
    } else {
        greeting = "Good evening";
    }

    out.write("\n    <h2>");
    out.print(greeting);
    out.write("!</h2>\n</body>\n</html>");
}
```

**Key insight:** Notice how the HTML **surrounding** the scriptlet becomes separate `out.write()` calls, while the scriptlet's Java code is inserted **as-is**, in the exact position it appeared — this is precisely why you can freely **interleave** scriptlets with HTML, and even split a single Java control structure (like an `if` block, shown next) across multiple scriptlet tags with HTML in between.

### Scriptlets Spanning Multiple Tags (An Important, Sometimes Confusing Capability)

```jsp
<% if (isLoggedIn) { %>
    <p>Welcome back!</p>
<% } else { %>
    <p>Please log in.</p>
<% } %>
```

**Generated (conceptually):**
```java
if (isLoggedIn) {
    out.write("\n    <p>Welcome back!</p>\n");
} else {
    out.write("\n    <p>Please log in.</p>\n");
}
```

This is a genuinely important capability to understand precisely: the **opening brace `{`** of the `if` and its **closing brace `}`** live in **separate scriptlet tags**, with plain HTML sitting **between** them — Jasper concatenates everything in the correct order during translation, so the resulting Java is perfectly valid, even though it looks fragmented across your `.jsp` source. This pattern (splitting control-flow braces across scriptlets, with HTML "inside" the braces) is exactly how old-style JSP pages implemented conditional/repeated HTML rendering, **before** JSTL (Module 2's upcoming topic) provided a cleaner alternative.

---

## 3. Declaration — `<%! ... %>`

**The one tag type that escapes `_jspService()` entirely** — content inside `<%! %>` becomes **class-level** code: fields or entire methods, declared **outside** any method, exactly like a Servlet's instance variables or helper methods (Topic 1, 2, 18).

```jsp
<%!
    private int pageVisitCount = 0;

    private String formatCount(int count) {
        return count + " visit" + (count == 1 ? "" : "s");
    }
%>

<html>
<body>
<%
    pageVisitCount++;
%>
    <p>This page has been visited <%= formatCount(pageVisitCount) %> since the server started.</p>
</body>
</html>
```

**What this generates (conceptually):**
```java
public class visits_jsp extends HttpJspBase {

    private int pageVisitCount = 0;  // CLASS-LEVEL field, from the declaration

    private String formatCount(int count) {  // CLASS-LEVEL method, from the declaration
        return count + " visit" + (count == 1 ? "" : "s");
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = response.getWriter();
        out.write("\n\n<html>\n<body>\n");

        pageVisitCount++;  // scriptlet code, INSIDE _jspService()

        out.write("\n    <p>This page has been visited ");
        out.print(formatCount(pageVisitCount));  // expression
        out.write(" since the server started.</p>\n</body>\n</html>");
    }
}
```

### CRITICAL Danger — Applying Topic 18's Thread Safety Lesson Directly to JSP

**`pageVisitCount` here is EXACTLY the same danger as Topic 6/18's Servlet instance-variable counter problem** — because remember (Topic 20): **a JSP becomes a Servlet**, and that generated Servlet follows the **exact same one-instance-many-threads model** (Topic 1, Step 2). `pageVisitCount++` inside `_jspService()`, mutating a **declaration-level (class-level)** field, is a **genuine race condition**, identical in every respect to Topic 18's unsynchronized counter — just now happening inside a JSP instead of a hand-written Servlet.

**This is precisely why Declarations for mutable state are strongly discouraged in real JSP usage** — and it's one of several concrete reasons (alongside the general MVC principle from Topic 1) that **JSP Best Practices** (upcoming topic) will explicitly recommend keeping JSP focused purely on presentation, with **no mutable class-level state**, pushing anything like a visit counter back into a proper Servlet/Listener (Topic 15's `ServletContext`-based, properly-synchronized approach) instead.

---

## 4. Expression — `<%= ... %>`

Already used since Topic 20 — a **single Java expression** (not a full statement — no semicolon, no `if`/`for`, just something that **evaluates to a value**) whose result is automatically passed to `out.print(...)`.

```jsp
<p>2 + 2 = <%= 2 + 2 %></p>
<p>Current year: <%= java.time.Year.now() %></p>
<p>Username: <%= request.getParameter("username") %></p>
```

**Generated (conceptually):**
```java
out.write("<p>2 + 2 = ");
out.print(2 + 2);
out.write("</p>\n<p>Current year: ");
out.print(java.time.Year.now());
out.write("</p>\n<p>Username: ");
out.print(request.getParameter("username"));
out.write("</p>");
```

**Critical syntax rule — no semicolon inside `<%= %>`:**
```jsp
<%= 2 + 2; %>  <!-- WRONG — causes a translation/compilation error -->
<%= 2 + 2 %>   <!-- CORRECT -->
```
This makes sense once you see the generated code: `out.print(2 + 2;)` would be invalid Java syntax (a stray semicolon inside the method call's parentheses) — the expression tag's content is inserted **directly as the argument** to `out.print(...)`, so it must be a valid, semicolon-free Java **expression**, not a statement.

---

## Where Do Implicit Objects Like `request`, `response`, `out` Come From? (Brief Preview)

Notice the Expression example above used `request.getParameter(...)` **without ever declaring or importing anything**. This works because JSP automatically provides several **implicit objects** — `request`, `response`, `session`, `out`, `application`, and others — already available inside scriptlets/expressions, without any setup on your part. This is formally its own upcoming topic (**Implicit Objects**) — flagging it now since we've used `request` here slightly ahead of its dedicated explanation.

---

## Consolidated Comparison Table

| Tag | Where code goes in generated class | Can contain statements? | Can contain a bare expression? | Typical use |
|---|---|---|---|---|
| `<%@ %>` | Imports / class-level settings / page directives | No | No | `import`, `contentType`, taglib declarations |
| `<%! %>` | Class body (fields/methods, outside `_jspService()`) | Yes (as method bodies) | No | Helper methods (rarely: fields — thread-safety risk!) |
| `<% %>` | Inside `_jspService()`, in-place | Yes | No (needs a semicolon-terminated statement) | Loops, conditionals, general logic |
| `<%= %>` | Inside `_jspService()`, as `out.print(...)` argument | No | Yes (required) | Printing a single value inline |

---

## EXECUTION FLOW — Tracing All Four Together in One Page

```jsp
<%@ page import="java.util.Date" %>
<%!
    private String appName = "My JSP App";
%>
<html>
<body>
<%
    Date now = new Date();
%>
    <h1>Welcome to <%= appName %></h1>
    <p>Current time: <%= now %></p>
</body>
</html>
```

```
Translation phase processes each tag type differently:
        │
        ├── <%@ page import %>  → becomes: import java.util.Date; (top of generated file)
        │
        ├── <%! private String appName... %>  → becomes: a FIELD in the class body
        │
        ├── Plain HTML + <% Date now = new Date(); %> + more HTML
        │      → becomes: out.write(...) calls interleaved with the raw
        │        "Date now = new Date();" statement, all INSIDE _jspService()
        │
        └── <%= appName %> and <%= now %>
               → become: out.print(appName); and out.print(now);
                 INSIDE _jspService(), at their exact respective positions
        │
        ▼
Final generated _jspService() runs top-to-bottom exactly as it appears,
producing the complete HTML response with "My JSP App" and the current
date/time correctly inserted at their designated spots
```

---

## COMMON ERRORS

**Error: Semicolon inside an Expression tag**
```jsp
<%= username; %>
```
```
JasperException: Unable to compile class for JSP
The method print(String) is not applicable... unexpected ';'
```
- **Fix:** Remove the semicolon — expression tags take a bare expression, not a statement.

**Error: Mismatched braces across split scriptlets**
```jsp
<% if (loggedIn) { %>
    Welcome!
<%-- forgot the closing } --%>
```
```
JasperException: ... 'else' without 'if' / unexpected end of file
```
- **Cause:** Forgetting to close an `if`/`for`/`while` block opened in an earlier scriptlet — since Jasper concatenates all the Java fragments in order, a missing brace produces genuinely invalid generated Java, surfacing as a compilation error against the generated file (Topic 20's error-message caveat).
- **Fix:** Carefully track every opened brace across scriptlets in the same page; treat it like editing one continuous Java method that just happens to have HTML "commented out" between fragments.

**Error: Thread-safety bugs from Declaration-level mutable fields (already covered above in depth)**
- **Fix:** Avoid mutable state in `<%! %>` declarations; use Application/Session scope (Topic 7) with proper synchronization (Topic 18) instead, or better, keep this kind of state out of JSP entirely.

**Error: Using scriptlets extensively where JSTL/EL would be cleaner (a "best practice" issue, not a compile error)**
- **Cause:** Not a bug per se, but a maintainability anti-pattern — heavy `<% %>` scriptlet usage for control flow (loops over data, conditionals) is exactly what JSTL Core Tags (an upcoming topic) exists to replace with cleaner, HTML-like tag syntax. Flagging this now so you recognize, once we reach JSTL, *why* it's presented as the preferred modern approach over what you've just learned.

---

That completes the **four core JSP syntax elements** — Directives, Scriptlet, Declaration, Expression — each mapped precisely to where its content ends up in the generated Servlet class, directly extending Topic 20's translation mechanism with concrete, tag-by-tag detail.

**Next up per your module order:** **Directives in full depth — Page Directive, Include Directive, Taglib Directive** — expanding Section 1 above into a complete reference for each of the three directive types and their most important attributes.

Say **"Next"** to continue.