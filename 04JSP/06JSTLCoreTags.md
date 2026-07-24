# TOPIC 26: JSTL Core Tags

## CONCEPT

### Why this concept exists

This topic delivers on promises made since **Topic 21** (scriptlets being discouraged for control flow) and **Topic 24** (the `<c:if>` preview). **JSTL (JavaServer Pages Standard Tag Library)** — specifically its **Core** tag set — provides HTML-like tags for the exact things scriptlets were previously used for: conditionals, loops, variable assignment — **without writing a single line of raw Java** inside your JSP. This is the **single biggest practical upgrade** in how real-world JSP pages are written, and it's why nearly every production JSP you'll ever see uses JSTL heavily and scriptlets rarely, if at all.

### What problem it solves precisely

Recall Topic 21's scriptlet example:
```jsp
<% if (isLoggedIn) { %>
    <p>Welcome back!</p>
<% } else { %>
    <p>Please log in.</p>
<% } %>
```

This mixes raw Java control-flow syntax (`if`/`{`/`}`) directly into what should be a designer-editable template — a designer with no Java background cannot safely modify this without risking a syntax error (a missing brace, Topic 21's common-error example). JSTL replaces this with:
```jsp
<c:choose>
    <c:when test="${isLoggedIn}">
        <p>Welcome back!</p>
    </c:when>
    <c:otherwise>
        <p>Please log in.</p>
    </c:otherwise>
</c:choose>
```

Every tag here **is** valid XML — no braces, no semicolons, no Java syntax at all — just tags, which any HTML-literate person can read, and more importantly, **cannot introduce a Java compilation error**, only well-formed-XML errors (a much smaller, more forgiving error class).

### Setup — the Taglib Directive (Topic 22, Now Put to Use)

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

**This must appear at the top of every JSP that uses JSTL Core tags.** Recall Topic 22: `uri="jakarta.tags.core"` is the correct URI for Jakarta EE 9+/Tomcat 10+ (the `javax.*`-era URI would be different and incompatible with your project's setup, Topic 4).

**Dependency reminder (Topic 1, Step 4.4):** the JSTL **implementation** JAR (`org.glassfish.web:jakarta.servlet.jsp.jstl`) must be a regular (non-`provided`) Maven dependency — unlike the Servlet/JSP APIs, Tomcat does **not** bundle JSTL's implementation itself, so it must be packaged into your WAR.

---

## `<c:out>` — Safe Output (Directly Fixing Topic 13's XSS Concern)

```jsp
<c:out value="${username}" />
```

**Functionally similar to `${username}` alone, but with one critical difference: `<c:out>` automatically HTML-escapes its output by default.**

```jsp
<!-- If username = "<script>alert('hacked')</script>" -->
${username}              <!-- Prints the RAW script tag — XSS vulnerability! -->
<c:out value="${username}" />   <!-- Prints: &lt;script&gt;alert('hacked')&lt;/script&gt; -- SAFE, displayed as text -->
```

**This is the exact, concrete fix promised back in Topic 13's hidden-field XSS warning.** `<c:out>` converts `<`, `>`, `&`, `"`, `'` into their HTML entity equivalents, so any user-submitted content containing HTML/script tags is displayed as **harmless visible text**, rather than being **executed** as actual HTML/JavaScript by the browser.

**Practical guidance:** use `<c:out>` (not bare `${}`) whenever displaying **user-submitted data** you haven't already sanitized elsewhere — for trusted, purely internal/computed values (like a formatted date you generated), bare EL is fine and more concise; for anything a user typed into a form, `<c:out>` is the safer default.

```jsp
<c:out value="${comment}" default="No comment provided" />
```
The `default` attribute prints a fallback if `value` evaluates to `null`.

---

## `<c:set>` and `<c:remove>` — Declarative Variable Assignment

```jsp
<c:set var="taxRate" value="0.18" scope="page" />
<p>Tax rate: ${taxRate}</p>

<c:remove var="taxRate" scope="page" />
```

**Equivalent scriptlet you'd otherwise write:**
```jsp
<% pageContext.setAttribute("taxRate", 0.18); %>
```

`<c:set>` is simply a **tag-based wrapper** around setting an attribute in a given scope (Topic 7/23's four scopes) — `var` is the attribute name, `value` is what to store, `scope` defaults to `page` if omitted. This directly replaces the Declaration/scriptlet-based state-setting pattern from Topic 21, in a way that's both simpler and avoids Topic 21's flagged thread-safety risk (since `<c:set>` with `page` scope, the default, is inherently request-local, never shared across threads).

---

## `<c:if>` — Single-Branch Conditional

```jsp
<c:if test="${age >= 18}">
    <p>You are an adult.</p>
</c:if>
```

**Critical limitation, frequently tested:** `<c:if>` has **no `else` branch at all** — it's purely a single-condition "show this if true" tag. For if/else logic, you need `<c:choose>` (next).

```jsp
<c:if test="${empty errorMessage}">
    <p>No errors.</p>
</c:if>
<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>
```

**Notice `not empty` here** — directly reusing Topic 24's EL `empty` operator and `not` word-operator (chosen specifically, recall, because it's XML-attribute-safe, unlike `!`).

---

## `<c:choose>`, `<c:when>`, `<c:otherwise>` — Full If/Else-If/Else Logic

```jsp
<c:choose>
    <c:when test="${score >= 90}">
        <p>Grade: A</p>
    </c:when>
    <c:when test="${score >= 75}">
        <p>Grade: B</p>
    </c:when>
    <c:when test="${score >= 60}">
        <p>Grade: C</p>
    </c:when>
    <c:otherwise>
        <p>Grade: F</p>
    </c:otherwise>
</c:choose>
```

**Precise mechanics:** `<c:choose>` evaluates its child `<c:when>` tags **in order**, executing the **first** one whose `test` is `true`, and **skipping all the rest** (exactly like Java's `if`/`else if`/`else` chain — **not** like a series of independent `<c:if>` tags, which would each be evaluated independently regardless of earlier matches). `<c:otherwise>` is the fallback, executed only if **none** of the `<c:when>` conditions matched — directly analogous to Java's final `else`.

**This is precisely the tag-based translation of Topic 21's split-scriptlet if/else example** — same logical structure, zero Java syntax.

---

## `<c:forEach>` — The Loop Tag (Genuinely High-Value, Used Constantly)

### Basic Iteration Over a Collection

```java
// In a Servlet:
List<String> hobbies = Arrays.asList("Reading", "Cricket", "Coding");
request.setAttribute("hobbies", hobbies);
```

```jsp
<ul>
    <c:forEach var="hobby" items="${hobbies}">
        <li>${hobby}</li>
    </c:forEach>
</ul>
```

**Attributes explained:**
- `var="hobby"` — the loop variable name, freshly available inside each iteration's body (scoped to Page scope, existing only within the loop's execution).
- `items="${hobbies}"` — the collection/array/Map being iterated — **note the EL syntax** here, directly reusing Topic 24.

**Equivalent scriptlet you'd otherwise write (the exact pattern JSTL replaces):**
```jsp
<%
    for (String hobby : hobbies) {
%>
    <li><%= hobby %></li>
<%
    }
%>
```

### Iterating With Index — `varStatus`

```jsp
<c:forEach var="hobby" items="${hobbies}" varStatus="status">
    <li>${status.index}: ${hobby} (Count: ${status.count})</li>
</c:forEach>
```

| `varStatus` property | Meaning |
|---|---|
| `status.index` | Zero-based position (0, 1, 2, ...) |
| `status.count` | One-based position (1, 2, 3, ...) |
| `status.first` | `true` if this is the first iteration |
| `status.last` | `true` if this is the last iteration |

**Practical use — alternating row styling (a genuinely common real-world need):**
```jsp
<c:forEach var="student" items="${students}" varStatus="status">
    <tr style="background-color: ${status.count % 2 == 0 ? 'lightgray' : 'white'};">
        <td>${student.name}</td>
    </tr>
</c:forEach>
```

### Fixed-Count Loop (Numeric `begin`/`end`, Not Iterating a Collection at All)

```jsp
<c:forEach var="i" begin="1" end="5">
    <p>Number: ${i}</p>
</c:forEach>
```
Prints numbers 1 through 5 — directly equivalent to a Java `for (int i = 1; i <= 5; i++)`, useful for pagination controls, fixed-repetition displays, etc.

### Iterating Over a `Map`

```java
Map<String, String> userRoles = new HashMap<>();
userRoles.put("admin", "Full Access");
userRoles.put("guest", "Read Only");
request.setAttribute("roles", userRoles);
```

```jsp
<c:forEach var="entry" items="${roles}">
    <p>${entry.key}: ${entry.value}</p>
</c:forEach>
```

**Why `entry.key`/`entry.value` work here (directly extending Topic 24's JavaBean property rule):** each iteration's `entry` variable is a `Map.Entry` object; `.key`/`.value` map to its `getKey()`/`getValue()` methods, following the **exact same** JavaBean-convention property-access rule EL uses everywhere else.

---

## Complete Realistic Example — Combining Everything

**Servlet (Controller, following Topic 1's MVC principle):**
```java
@WebServlet("/students")
public class StudentListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = new ArrayList<>();
        students.add(new Student("Rahul", 21));
        students.add(new Student("Priya", 19));
        students.add(new Student("Amit", 22));

        request.setAttribute("students", students);
        request.getRequestDispatcher("/WEB-INF/views/studentList.jsp").forward(request, response);
    }
}
```

**`studentList.jsp` (View — zero scriptlets, zero raw Java):**
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<body>
    <h2>Student List</h2>

    <c:if test="${empty students}">
        <p>No students registered yet.</p>
    </c:if>

    <c:if test="${not empty students}">
        <table border="1">
            <tr><th>#</th><th>Name</th><th>Age</th><th>Status</th></tr>
            <c:forEach var="student" items="${students}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td><c:out value="${student.name}" /></td>
                    <td>${student.age}</td>
                    <td>
                        <c:choose>
                            <c:when test="${student.age >= 18}">Adult</c:when>
                            <c:otherwise>Minor</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>
```

This example is a **complete, direct preview** of the display pattern you'll use constantly across every Module 4 project (Student/Employee/Library Management) — a Servlet fetches a `List` (eventually from a database via JDBC, Module 3), forwards it, and a JSTL-powered JSP renders it as a table, with zero scriptlets.

---

## EXECUTION FLOW — `<c:forEach>` Translation (Confirming It's Still "Just Java" Underneath)

```
<c:forEach var="hobby" items="${hobbies}"> ... </c:forEach>
        │
        ▼
Translation phase (Topic 20's mechanism, applying equally to JSTL tags):
        │
        ▼
Generated Java code (conceptually):
   Iterator it = ((Collection) hobbiesValue).iterator();
   while (it.hasNext()) {
       Object hobby = it.next();
       pageContext.setAttribute("hobby", hobby);
       out.write("<li>");
       out.write(String.valueOf(pageContext.findAttribute("hobby")));
       out.write("</li>");
   }
        │
        ▼
This is STILL translated into a real Java loop inside _jspService() —
JSTL tags are NOT a different execution engine; they're simply a
higher-level, XML-based syntax that Jasper translates into the exact
same kind of Java loop you'd have written by hand in a scriptlet (Topic 21)
```

**This is the crucial unifying insight:** JSTL doesn't introduce new magic — it's **syntactic sugar**, translated by the same Jasper engine, into the same category of Java code scriptlets would have produced — just safer (XML-well-formed, not raw-Java-syntax-fragile) and more maintainable for non-Java-fluent team members to read and edit.

---

## COMMON ERRORS

**Error: `<c:when>` used outside `<c:choose>`**
```
JasperException: The tag <c:when> is not permitted outside <c:choose>
```
- **Fix:** `<c:when>`/`<c:otherwise>` must always be direct children of `<c:choose>` — never used standalone.

**Error: Forgetting the taglib directive**
```
JasperException: The tag library ... prefix c ... does not have a definition
```
- **Fix:** Always include `<%@ taglib prefix="c" uri="jakarta.tags.core" %>` at the top of any JSP using JSTL Core tags (Topic 22).

**Error: Wrong URI (javax vs jakarta, repeated from Topic 22 in this new context)**
- **Fix:** Confirm `jakarta.tags.core` is used, matching your Tomcat 10+/Jakarta EE 9+ setup.

**Error: `<c:forEach>` producing no output at all, no error**
- **Cause:** `items="${hobbies}"` evaluates to `null` or an empty collection (perhaps a typo in the attribute name, or the Servlet forgot to call `setAttribute`) — recall Topic 24's EL "silent failure" behavior; this silence propagates into `<c:forEach>` as well — zero iterations, zero errors, zero visible feedback.
- **Fix:** Verify the Servlet actually populated the exact attribute name being referenced, and consider temporarily adding `<c:out value="${hobbies}" />` alone to confirm the collection is genuinely present and non-empty before debugging the loop itself.

**Error: Missing JSTL JAR dependency (Topic 1, Step 4.4 reminder)**
```
JasperException: ... cannot resolve ... jakarta.tags.core
```
- **Cause:** The `org.glassfish.web:jakarta.servlet.jsp.jstl` Maven dependency is missing or was accidentally marked `provided`.
- **Fix:** Confirm this dependency is present in `pom.xml` with default (non-`provided`) scope, so it's actually bundled inside your WAR.

---

That completes **JSTL Core Tags** — the single most practically important topic in Module 2 for real-world JSP development. You now have the complete toolkit (`<c:out>`, `<c:set>`, `<c:if>`, `<c:choose>`/`<c:when>`/`<c:otherwise>`, `<c:forEach>`) to write fully dynamic, data-driven JSP pages **without a single scriptlet** — directly enabling the clean, professional View layer every Module 4 project will use.

**Next up per your module order:** **JSTL Formatting Tags** — the `fmt:` tag library for formatting numbers, currency, dates, and handling locale-aware output, commonly needed alongside Core tags in real display logic (e.g., formatting a `Student`'s enrollment date, or an `Employee`'s salary as currency).

Say **"Next"** to continue.