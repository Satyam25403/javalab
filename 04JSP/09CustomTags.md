# TOPIC 29: Custom Tags (Introduction)

## CONCEPT

### Why this concept exists

Throughout Topics 26–28, you've used `<c:if>`, `<c:forEach>`, `<fmt:formatNumber>`, `<sql:query>` — all **custom tags**, provided by JSTL. But JSTL isn't magic or built into the JSP specification itself — it's a **library of custom tags**, built using the **same tag-extension mechanism** available to **you**. This topic introduces that underlying mechanism, so you understand JSTL isn't a special, closed system — it's an example of a general capability you could use yourself.

### What problem it solves

Sometimes your application has **presentation logic that repeats across many pages**, but isn't covered by JSTL's built-in tags — e.g., a custom-formatted "star rating" widget, a company-specific date-badge display, or a reusable "permission check" wrapper. Custom tags let you package this kind of reusable presentation logic into your **own** tag, usable exactly like `<c:if>` or `<fmt:formatNumber>`, keeping your JSPs clean and scriptlet-free even for logic JSTL doesn't provide out of the box.

### Real-world analogy

If JSTL's tags are like using **pre-built furniture from a catalog** (a chair, a table — standard, ready-made), a Custom Tag is like **commissioning a carpenter to build one specific piece** to your exact specification, which you can then order repeatedly from your own personal catalog, exactly the same way you'd order any standard catalog item.

---

## Two Ways to Build Custom Tags

| Approach | Mechanism | Complexity |
|---|---|---|
| **Tag Handler Class** | A Java class implementing `SimpleTag` (or extending `SimpleTagSupport`) | More setup, full programmatic control |
| **Tag Files** | A `.tag` file, written in JSP-like syntax itself | Simpler, faster to write, less Java-heavy |

We'll cover both at an introductory level, since your syllabus lists this as "Introduction" — full production-grade custom tag library development is a deeper topic than this course's current scope, but understanding the mechanism thoroughly is valuable, both for exams and for genuinely understanding what JSTL *is*.

---

## Approach 1 — Tag Handler Class (`SimpleTagSupport`)

### Step 1: Write the Tag Handler Class

```java
package com.company.myapp.tag;

import java.io.IOException;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class GreetingTag extends SimpleTagSupport {

    private String name; // corresponds to a tag attribute

    // Standard JavaBean setter — REQUIRED for the tag library to
    // bind the "name" attribute value to this field (same convention as Topic 24/25)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().write("<b>Hello, " + name + "! Welcome to our site.</b>");
    }
}
```

**Explanation of key elements:**

- **`extends SimpleTagSupport`** — a convenience base class implementing the `SimpleTag` interface, parallel in spirit to how `HttpServlet` (Topic 1, Step 6.1) provides convenient defaults over the raw `Servlet` interface.
- **`setName(String name)`** — a plain JavaBean setter. The tag library infrastructure automatically calls this when the tag is used with a `name="..."` attribute — **exactly the same JavaBean-property convention** used since Topic 24 (EL) and Topic 25 (`<jsp:setProperty>`) — this consistency across the whole JSP ecosystem is not a coincidence; it's a deliberate, unifying design convention.
- **`doTag()`** — the method that actually executes when the tag is invoked, analogous to a Servlet's `service()`/`doGet()` (Topic 1) — this is where your tag's logic and output generation happens.
- **`getJspContext().getOut()`** — obtains the output writer (conceptually similar to the `out` implicit object from Topic 23), used to write this tag's contribution to the response.

### Step 2: Create the Tag Library Descriptor (`.tld` file)

**File location:** `src/main/webapp/WEB-INF/mytags.tld`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<taglib xmlns="https://jakarta.ee/xml/ns/jakartaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd"
        version="3.0">

    <tlib-version>1.0</tlib-version>
    <short-name>mytags</short-name>
    <uri>http://company.com/mytags</uri>

    <tag>
        <name>greeting</name>
        <tag-class>com.company.myapp.tag.GreetingTag</tag-class>
        <body-content>empty</body-content>
        <attribute>
            <name>name</name>
            <required>true</required>
            <rtexprvalue>true</rtexprvalue>
        </attribute>
    </tag>

</taglib>
```

**This `.tld` file is precisely analogous to `web.xml` (Topic 5) — a declarative registration document**, telling the container: "there's a tag called `greeting`, implemented by this Java class, expecting a required attribute called `name` that can accept runtime expressions (`rtexprvalue`, meaning EL like `name="${student.name}"` is allowed, not just a fixed literal string)."

### Step 3: Use the Custom Tag in a JSP

```jsp
<%@ taglib prefix="my" uri="http://company.com/mytags" %>

<html>
<body>
    <my:greeting name="Rahul" />
</body>
</html>
```

**Notice this is EXACTLY the same taglib directive pattern from Topic 22, 26, 27, 28** — just pointing at **your own** `uri` (matching the `<uri>` declared in the `.tld` file) instead of JSTL's built-in ones. This confirms directly: **JSTL's `<c:if>`, `<fmt:formatNumber>`, etc., are registered and used via exactly this same mechanism** — they simply ship with pre-written `.tld` files and tag handler classes, bundled as a library, rather than you writing them yourself.

---

## Approach 2 — Tag Files (Simpler, JSP-Syntax-Based)

For tags whose logic is primarily **more JSP-like content generation** rather than complex Java logic, **Tag Files** avoid writing a Java class entirely.

**File location:** `src/main/webapp/WEB-INF/tags/greeting.tag`

```jsp
<%@ attribute name="name" required="true" %>

<b>Hello, ${name}! Welcome to our site.</b>
```

**Using it in a JSP:**
```jsp
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<html>
<body>
    <my:greeting name="Rahul" />
</body>
</html>
```

**Key differences from the Tag Handler Class approach:**
- **`tagdir="/WEB-INF/tags"`** instead of a `uri` — pointing directly at a **directory** of `.tag` files, rather than a `.tld`-registered Java class. No `.tld` file needed at all for this simpler approach.
- **`<%@ attribute name="name" required="true" %>`** — a directive **specific to `.tag` files**, declaring an expected attribute, conceptually parallel to the `<attribute>` block in the `.tld` XML from Approach 1, but expressed in JSP-directive syntax instead of XML.
- The tag's **body** is written in **ordinary JSP syntax** (here, using EL, `${name}`) — meaning you can freely use everything from Topics 21–28 (EL, JSTL Core/Formatting tags) **inside** your own custom tag's implementation — custom tags can **compose** other tags, exactly like functions calling other functions.

**When to choose Tag Files over Tag Handler Classes:** Tag Files are simpler and faster to write for **presentation-focused** reusable snippets (much like a JSP `include`, Topic 22, but parameterized). Tag Handler Classes are appropriate when you need **genuine Java logic** — loops, conditionals, calls to Service/DAO classes (though, per Topic 1's MVC principle, business logic still shouldn't really live here) — Tag Files are restricted to JSP-like syntax and can't contain arbitrary Java computation the way a `SimpleTagSupport` class's `doTag()` method can.

---

## Custom Tags With a Body (Brief Introduction)

Both approaches can also support tags that **wrap content**, similar to how `<c:if>` wraps its conditionally-displayed content (Topic 26):

```jsp
<my:adminOnly>
    <p>This content is only visible to admins.</p>
</my:adminOnly>
```

This requires the tag handler to explicitly invoke its body content (via `getJspBody().invoke(...)` in a `SimpleTagSupport` class) conditionally — a genuinely more advanced pattern than this "Introduction"-level topic needs to fully implement, but worth recognizing conceptually: **this is precisely how `<c:if>`'s "show this content only if true" behavior is actually implemented internally** — `<c:if>` is, itself, just a tag handler class checking its `test` condition and conditionally invoking its body.

---

## Why This Topic Matters, Even at an Introductory Level

**The core insight to take away:** JSTL is not a special, hardcoded part of the JSP specification — it is a **library built using the exact same custom tag mechanism available to you**. Understanding this demystifies JSTL entirely: every tag you've used since Topic 26 works because someone (the JSTL project maintainers) wrote tag handler classes and `.tld` files following precisely this pattern, then packaged and distributed them as a reusable library — exactly the kind of thing you could theoretically do yourself for your own organization's reusable presentation components.

---

## EXECUTION FLOW — Custom Tag Invocation Trace

```
JSP contains: <my:greeting name="Rahul" />
        │
        ▼
Translation phase (Topic 20's mechanism, applying to custom tags too):
        │
        ▼
Jasper consults the .tld (or scans the tagdir) to find which class/file
implements the "greeting" tag under this prefix/uri
        │
        ▼
Generated code (conceptually) instantiates GreetingTag,
calls setName("Rahul"), then calls doTag()
        │
        ▼
doTag() executes: getJspContext().getOut().write("<b>Hello, Rahul!...</b>")
        │
        ▼
This output is merged into the surrounding page's output stream,
at exactly the position the <my:greeting> tag appeared
```

---

## COMMON ERRORS

**Error: `.tld` file not found / tag library not resolving**
```
JasperException: The uri ... http://company.com/mytags cannot be resolved
```
- **Cause:** `.tld` file misplaced (must be under `WEB-INF/` or `WEB-INF/lib/` inside a packaged JAR for distributed libraries) or the `uri` attribute in the taglib directive doesn't exactly match the `<uri>` declared inside the `.tld`.
- **Fix:** Double-check exact string matching between the JSP's `taglib` directive and the `.tld`'s declared `<uri>`.

**Error: Missing setter method for a declared attribute**
```
JasperException: Unable to find setter method for attribute: name
```
- **Cause:** The `.tld` declares an `<attribute><name>name</name></attribute>`, but the Java tag handler class lacks a corresponding `setName(String)` method — same JavaBean-convention requirement flagged repeatedly since Topic 24.
- **Fix:** Ensure exact naming alignment between the `.tld`'s declared attribute name and the tag handler's setter method name.

**Error: Confusing Tag Files' `tagdir` with Tag Handler Classes' `uri`**
- **Cause:** Attempting to use `uri="/WEB-INF/tags"` (Approach 1's syntax) for a Tag Files-based library, or vice versa — these are genuinely different attributes for genuinely different underlying mechanisms.
- **Fix:** Remember: `.tag` files → `tagdir` attribute; Java-class-based tags with a `.tld` → `uri` attribute.

---

That completes the **introduction to Custom Tags** — you now understand that JSTL (Topics 26–28) is not special/built-in magic, but rather a **library** built on the same extensible tag mechanism you could use yourself, via either Tag Handler Classes (full Java control) or the simpler Tag Files (JSP-syntax-based). This closes the loop on "where do all these tags actually come from?" that's been implicit since Topic 26.

**Next up per your module order:** **Error Pages** (JSP-specific error page mechanics, consolidating and completing Topic 22's `errorPage`/`isErrorPage` page-directive attributes with full worked examples) — followed by **Session Handling**, **Include Mechanisms**, **MVC with JSP**, **JSP Best Practices**, and **JSP vs Servlet / JSP vs Thymeleaf** to close out Module 2.

Say **"Next"** to continue.