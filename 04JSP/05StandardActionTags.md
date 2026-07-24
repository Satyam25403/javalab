# TOPIC 25: Standard Action Tags

## CONCEPT

### Why this concept exists

Before JSTL existed, JSP already provided a small set of **built-in XML-style tags** — called **Standard Actions** — for common request-time operations: including content, forwarding, and working with JavaBeans. These use `<jsp:xxx>` syntax (part of the JSP specification itself, not a separate library requiring a `taglib` directive, Topic 22). Understanding these completes your picture of *every* way JSP can include/forward content, and introduces the JavaBean-integration pattern that Model classes (Topic 1's MVC, Topic 24's EL) rely on.

### Real-world analogy

If the Include Directive (Topic 22) is like **permanently binding** several separate documents into one physical book before printing (translation-time merge), `<jsp:include>` is like **photocopying a page from a different, currently-existing book and stapling it in fresh, every single time** someone asks for a copy (request-time, independently compiled resource) — same end goal (combining content), fundamentally different mechanism and timing.

---

## `<jsp:include>` — Request-Time Include (The Third "Include" Mechanism)

You've now seen **three** different "include" mechanisms across this course — this is precisely the moment to consolidate all three.

```jsp
<jsp:include page="header.jsp" />
```

### `<jsp:include>` vs. Include Directive vs. `RequestDispatcher.include()` — The Complete Picture

| Mechanism | Timing | Topic | Separately compiled? | Can pass parameters? |
|---|---|---|---|---|
| `<%@ include file="..." %>` | **Translation time** (source merge) | Topic 22 | No — becomes one combined class | No |
| `<jsp:include page="..." />` | **Request time** (method call) | This topic | Yes — independently compiled resource | **Yes**, via `<jsp:param>` |
| `RequestDispatcher.include()` | **Request time** (method call, from a Servlet) | Topic 9 | Yes | Via request attributes, not a dedicated tag |

**Critical realization:** `<jsp:include>` is essentially **JSP's own syntax wrapper around exactly the same `RequestDispatcher.include()` mechanism from Topic 9** — when Jasper translates `<jsp:include page="header.jsp" />`, it generates Java code that does precisely what you wrote by hand in Topic 9's `DashboardServlet` example (`request.getRequestDispatcher("header.jsp").include(request, response)`). This is the same underlying mechanism, just with more convenient, HTML-like syntax available directly inside a JSP, without needing a scriptlet.

### Code Example — `<jsp:include>` with Parameters

```jsp
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Dashboard" />
</jsp:include>
```

**Inside `header.jsp`, the passed parameter is accessible exactly like a normal request parameter:**
```jsp
<h1>${param.pageTitle}</h1>
<!-- Recall Topic 24: ${param.xxx} is EL's way of reading request parameters -->
```

**Why does this work, precisely?** `<jsp:param>` adds this key-value pair to the **request** that gets passed to the included resource — the included JSP sees it exactly as if it were a normal query-string/form parameter (Topic 8's `getParameter()`, or Topic 24's `${param.xxx}`), even though it was never actually part of the original browser request at all — it was added **server-side**, specifically for this one include operation.

---

## `<jsp:forward>` — Standard Action Wrapper Around `RequestDispatcher.forward()`

```jsp
<jsp:forward page="loginError.jsp" />
```

**Exactly equivalent to (Topic 9's mechanism, wrapped in JSP tag syntax):**
```java
request.getRequestDispatcher("loginError.jsp").forward(request, response);
```

**All of Topic 9's `forward()` rules apply identically:** browser URL doesn't change, request-scoped attributes carry over, response must not be already committed before forwarding.

```jsp
<%
    boolean isValid = LoginService.validateCredentials(username, password);
%>
<% if (!isValid) { %>
    <jsp:forward page="/WEB-INF/views/loginError.jsp">
        <jsp:param name="reason" value="invalid_credentials" />
    </jsp:forward>
<% } %>
```

**Why you'd rarely actually write this pattern in a well-architected app:** per Topic 1's MVC principle, **validation logic belongs in a Servlet (Controller), not a JSP (View)** — a JSP performing its own validation and forwarding, as shown above, is a **structural anti-pattern**, blending Controller responsibilities into the View layer. This tag exists and works correctly, and you should recognize/understand it (frequently tested in exams), but in real MVC-following projects (exactly what you'll build in Module 4), **Servlets** call `RequestDispatcher.forward()`, and JSPs mostly **receive** forwards rather than initiating their own.

---

## `<jsp:useBean>` — The JavaBean Integration Tag

### Purpose

Declares (and optionally creates) a **JavaBean** instance, making it available under a given scope, for later access via `<jsp:getProperty>`/`<jsp:setProperty>` or EL (Topic 24).

### Syntax

```jsp
<jsp:useBean id="student" class="com.company.myapp.model.Student" scope="request" />
```

| Attribute | Purpose |
|---|---|
| `id` | The variable name this bean will be referred to by (becomes both a scripting variable AND an attribute name in the given scope) |
| `class` | The fully-qualified class name to instantiate — **must have a public no-argument constructor** |
| `scope` | One of `page` (default), `request`, `session`, `application` — Topic 7 and Topic 23's four scopes, directly reused here |

### Precise Behavior — "Find or Create" Semantics

**This is a genuinely important, precise mechanic, frequently misunderstood:** `<jsp:useBean>` does **NOT** always create a new object. Its exact behavior:

```
1. Check the specified `scope` for an existing attribute named `id`
        │
   ┌────┴────┐
  EXISTS      DOESN'T EXIST
   │              │
   ▼              ▼
Reuse the      Instantiate a NEW object of `class`
existing        (using its no-arg constructor),
object          then store it in that scope under `id`
```

**Practical implication:** if a Servlet already did `request.setAttribute("student", someStudentObject)` before forwarding, a JSP's `<jsp:useBean id="student" class="..." scope="request" />` will **find and reuse that exact existing object** — it will **not** overwrite it with a fresh, empty instance. This "find or create" behavior is precisely why `<jsp:useBean>` is safe to place defensively in a JSP even when you expect the Controller to have already populated the bean — it gracefully handles both "already provided" and "not yet provided" cases with one consistent tag.

### `<jsp:setProperty>` and `<jsp:getProperty>`

```jsp
<jsp:useBean id="student" class="com.company.myapp.model.Student" scope="page" />
<jsp:setProperty name="student" property="name" value="Rahul" />
<jsp:setProperty name="student" property="age" value="21" />

<p>Name: <jsp:getProperty name="student" property="name" /></p>
<p>Age: <jsp:getProperty name="student" property="age" /></p>
```

**Precise mechanics (identical JavaBean-convention rule from Topic 24's EL section):**
- `<jsp:setProperty name="student" property="name" value="Rahul" />` calls `student.setName("Rahul")` internally.
- `<jsp:getProperty name="student" property="name" />` calls `student.getName()` internally, and prints the result.
- **Exactly the same JavaBean getter/setter naming convention requirement from EL (Topic 24) applies here** — your `Student` class **must** have `getName()`/`setName(String)`, `getAge()`/`setAge(int)`, etc., for these tags to function.

### The Genuinely Powerful Feature — Auto-Populating From Request Parameters

```jsp
<jsp:setProperty name="student" property="*" />
```

**The `property="*"` wildcard** automatically matches **every** request parameter whose name exactly matches a property on the bean, and calls the corresponding setter for each — **without you writing a single explicit mapping**. If your HTML form has `<input name="name">` and `<input name="age">`, and your `Student` bean has matching `setName()`/`setAge()` methods, this **one line** populates the **entire bean** from the submitted form data.

```jsp
<jsp:useBean id="student" class="com.company.myapp.model.Student" scope="request" />
<jsp:setProperty name="student" property="*" />
```

**This is a genuinely useful, real productivity feature** — though worth noting its limitation: it performs **no validation whatsoever** (directly connecting back to Topic 16's exception-handling principles — you'd still need to validate the populated bean's values afterward), and type mismatches (e.g., a non-numeric string submitted for an `int` property) can throw exceptions during this auto-population step, which you should be prepared to handle.

---

## Complete Example — Combining `<jsp:useBean>`, Auto-Population, and Display

**`Student.java` (Model, following Topic 1's MVC principle — no `jakarta.servlet.*` imports):**
```java
package com.company.myapp.model;

public class Student {
    private String name;
    private int age;

    public Student() {} // REQUIRED — no-arg constructor for <jsp:useBean>

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

**`registerStudent.html`:**
```html
<form action="studentResult.jsp" method="post">
    <input type="text" name="name" placeholder="Name">
    <input type="text" name="age" placeholder="Age">
    <input type="submit">
</form>
```

**`studentResult.jsp`:**
```jsp
<jsp:useBean id="student" class="com.company.myapp.model.Student" scope="page" />
<jsp:setProperty name="student" property="*" />

<html>
<body>
    <h2>Student Registered:</h2>
    <p>Name: <jsp:getProperty name="student" property="name" /></p>
    <p>Age: <jsp:getProperty name="student" property="age" /></p>
</body>
</html>
```

**Notice: zero scriptlets, zero manual `request.getParameter()` calls** — the entire form-to-bean population happens declaratively through these standard actions. This is a legitimately elegant pattern for simple cases — though for anything requiring real validation/business logic (which, per Topic 1's MVC principle, should live in a Servlet/Service layer, not directly in a JSP), you'd typically perform this same population **inside a Servlet** instead (manually, or using similar bean-utility patterns), then forward the already-validated bean to the JSP purely for display via EL.

---

## `<jsp:useBean>`/Properties vs. Plain EL — When to Use Which

| Approach | Best for |
|---|---|
| `<jsp:useBean>` + `<jsp:setProperty property="*">` | Quick prototypes, simple form-to-bean binding without validation needs |
| EL (`${student.name}`) | **Displaying** already-populated bean data (the far more common real-world JSP task) |

**Practical guidance for your Module 4 projects:** you'll overwhelmingly use **EL for display** (Topic 24) with beans populated and validated properly in **Servlets**, following strict MVC — `<jsp:useBean>`'s auto-population wildcard is a nice-to-know convenience, genuinely useful for quick exam/lab demonstrations, but real production-quality code generally prefers explicit, validated population in the Controller layer.

---

## EXECUTION FLOW — `<jsp:useBean>` "Find or Create" Trace

```
Servlet: request.setAttribute("student", new Student("Rahul", 21));
request.getRequestDispatcher("view.jsp").forward(request, response);
        │
        ▼
Inside view.jsp: <jsp:useBean id="student" class="..." scope="request" />
        │
        ▼
Jasper-generated code checks: does REQUEST scope already contain
an attribute named "student"?
        │
        ▼
   YES → reuse the EXISTING Student object (Rahul, 21) —
         does NOT instantiate a new, empty Student
        │
        ▼
${student.name} / <jsp:getProperty .../> now correctly show "Rahul", not null/empty
```

---

## COMMON ERRORS

**Error: `<jsp:useBean>` fails with instantiation exception**
```
java.lang.InstantiationException: com.company.myapp.model.Student
```
- **Cause:** The bean class lacks a **public no-argument constructor** (e.g., you only defined a constructor with parameters).
- **Fix:** Always provide a public no-arg constructor on any class used with `<jsp:useBean>`, exactly as shown (`public Student() {}`).

**Error: `<jsp:setProperty property="*">` throwing on type conversion**
```
jakarta.servlet.jsp.JspException: ... cannot convert "abc" to int
```
- **Cause:** A form field intended for an `int` property (like `age`) was submitted with non-numeric text.
- **Fix:** This is precisely why relying purely on this wildcard auto-population, without any validation layer, is risky for production use — reinforcing why Servlet-layer validation (Topic 16) remains important even when using this convenience feature.

**Error: Getter/setter naming mismatch (identical root cause to Topic 24's EL error)**
- **Fix:** Ensure exact JavaBean naming convention compliance — `property="name"` requires `getName()`/`setName()`, not `getname()` or `get_name()`.

**Error: Using `<jsp:forward>` for what should be Controller-layer logic (architectural issue, not a runtime error)**
- Already discussed above — recognize this as a **design smell** in real projects, even though it "works" correctly.

---

That completes **Standard Action Tags** — you now have the complete picture of all three "include" mechanisms in JSP (translation-time directive, request-time standard action, request-time `RequestDispatcher` from a Servlet), plus the JavaBean integration pattern (`useBean`/`setProperty`/`getProperty`) that underlies how JSP conventionally works with Model objects.

**Next up per your module order:** **JSTL Core Tags** — the modern, preferred replacement for scriptlet-based control flow (`<c:if>`, `<c:forEach>`, `<c:choose>`, `<c:set>`, `<c:out>`), directly delivering on what's been foreshadowed since Topic 21's scriptlet discussion and Topic 24's `<c:if>` preview.

Say **"Next"** to continue.