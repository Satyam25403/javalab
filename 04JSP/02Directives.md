# TOPIC 22: Directives — Page, Include, and Taglib

## CONCEPT

### Why this concept exists

Topic 21 introduced the **directive syntax category** (`<%@ ... %>`) generically. JSP defines **exactly three** directive types, each configuring a fundamentally different aspect of page translation/behavior: **page-level settings**, **static content inclusion at translation time**, and **tag library registration**. This topic gives each its complete, dedicated treatment.

---

## 1. Page Directive — `<%@ page ... %>`

### Purpose

Configures **page-wide settings** — how the page is translated, what it imports, how errors are handled, session participation, and more. **Multiple `<%@ page %>` directives can appear in one file** (each setting a different attribute), though convention often groups them at the top.

### Complete Attribute Reference

| Attribute | Example | Purpose |
|---|---|---|
| `import` | `import="java.util.*, java.text.SimpleDateFormat"` | Adds Java imports to the generated class — the JSP equivalent of a Servlet's `import` statements |
| `contentType` | `contentType="text/html;charset=UTF-8"` | Sets the response's `Content-Type` header — equivalent to `response.setContentType()` (Topic 1) |
| `session` | `session="true"` (default) or `"false"` | Controls whether the implicit `session` object (upcoming Implicit Objects topic) is available at all in this page |
| `errorPage` | `errorPage="errorHandler.jsp"` | Specifies a JSP to forward to if **this page** throws an uncaught exception — a JSP-level parallel to `web.xml`'s `<error-page>` (Topic 5, 16) |
| `isErrorPage` | `isErrorPage="true"` | Marks **this** JSP as one that legitimately receives forwarded exceptions (unlocks the implicit `exception` object, covered in Implicit Objects) |
| `language` | `language="java"` | The scripting language (in practice, always `"java"` — other values are effectively obsolete) |
| `extends` | rarely used | Lets the generated class extend a custom base class instead of `HttpJspBase` — advanced, rarely needed |
| `isELIgnored` | `isELIgnored="false"` (default) | Whether `${...}` Expression Language syntax (used since Topic 1's MVC example, formally covered in an upcoming topic) is processed or treated as literal text |
| `autoFlush` | `autoFlush="true"` (default) | Whether the output buffer auto-flushes when full |
| `buffer` | `buffer="8kb"` (default) | Size of the output buffering before it's sent to the client |

### Code Example — Page Directive in Practice

```jsp
<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="errorHandler.jsp" %>

<html>
<body>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formattedDate = sdf.format(new Date());
%>
    <h2>Current Date: <%= formattedDate %></h2>
</body>
</html>
```

**`errorPage="errorHandler.jsp"` in action — the corresponding error page:**

```jsp
<%@ page isErrorPage="true" %>
<html>
<body>
    <h2 style="color:red;">Something went wrong on this page.</h2>
    <p>Error: <%= exception.getMessage() %></p>
</body>
</html>
```

**How this connects directly to Topic 16's exception handling:** if the first JSP throws any uncaught exception (e.g., a `NullPointerException` inside a scriptlet), the container forwards **specifically to `errorHandler.jsp`** — a **JSP-scoped** error-page mechanism, working **alongside** (not replacing) `web.xml`'s application-wide `<error-page>` declarations from Topic 5/16. **`isErrorPage="true"`** is what unlocks the implicit `exception` object inside `errorHandler.jsp` — attempting to reference `exception` in a JSP that **hasn't** declared `isErrorPage="true"` causes a **translation-time error**, since the generated Servlet class simply won't have that variable declared at all otherwise.

**Precedence note (a genuinely important, testable detail):** a page-level `errorPage` attribute takes effect **specifically for exceptions thrown within that one JSP**; `web.xml`'s `<error-page>` (Topic 5) applies **application-wide**, as a broader fallback. If both could apply, the page-level `errorPage` attribute wins for that specific page — this mirrors the general pattern (seen with Servlet mapping precedence in Topic 4) of **more specific configuration overriding more general configuration**.

---

## 2. Include Directive — `<%@ include file="..." %>`

### Purpose

Includes the content of **another file** into this JSP — but critically, this happens at **translation time**, not at request time. This is fundamentally different from the `RequestDispatcher.include()` you learned in Topic 9 — same word, **completely different mechanism and timing**.

### Syntax

```jsp
<%@ include file="header.jsp" %>

<html>
<body>
    <h2>Main page content goes here.</h2>
</body>
</html>

<%@ include file="footer.jsp" %>
```

### The Critical Distinction: Include Directive (translation-time) vs. `RequestDispatcher.include()` (request-time)

| Aspect | `<%@ include %>` (Directive) | `RequestDispatcher.include()` (Topic 9) |
|---|---|---|
| **When it happens** | **Translation time** — the included file's raw content is copied/merged into the including JSP's source **before** compilation even happens | **Request time** — happens fresh, on every single request, as a separate method call during execution |
| **Result** | Both files become **one single** generated `_jsp.java`/`.class` — genuinely merged into one Servlet | **Two separate** compiled Servlets (or JSP-Servlets) at runtime, one calling into the other |
| **Performance** | Slightly faster at request time (no separate method dispatch — it's all one method already) | Slightly more overhead per request (an actual method call across two objects) |
| **Flexibility** | The included file's path is **fixed** at translation time — cannot be decided dynamically based on request data | Can compute the included resource's path **dynamically** based on request parameters/logic |
| **If the included file changes** | Requires the **including** JSP to be re-translated too (though Tomcat's timestamp-checking, Topic 20, generally handles this correctly in development) | No such concern — each file is independently compiled; changes take effect independently |

**Why this timing difference matters practically:** because `<%@ include %>` merges source **before** compilation, if `header.jsp` declares a scriptlet variable, **the including page can actually reference that variable directly** (since, after merging, it's all just one continuous piece of Java code) — something `RequestDispatcher.include()` could **never** do, since that mechanism involves two genuinely separate, independently-compiled Servlet objects with no shared local variable scope at all. This is a subtle but real technical distinction worth understanding, not just a naming coincidence.

### Code Example

**`header.jsp`:**
```jsp
<html>
<body>
<div style="background:navy;color:white;padding:10px;">My Application</div>
```

**`footer.jsp`:**
```jsp
<div style="background:gray;color:white;padding:10px;">&copy; 2026 My Company</div>
</body>
</html>
```

**`mainPage.jsp`:**
```jsp
<%@ include file="header.jsp" %>
    <h2>Welcome to the main content area.</h2>
<%@ include file="footer.jsp" %>
```

**At translation time, Jasper conceptually produces ONE merged source before compiling:**
```jsp
<html>
<body>
<div style="background:navy;color:white;padding:10px;">My Application</div>
    <h2>Welcome to the main content area.</h2>
<div style="background:gray;color:white;padding:10px;">&copy; 2026 My Company</div>
</body>
</html>
```
...**then this single merged result** is what actually gets translated into `mainPage_jsp.java` and compiled — `header.jsp` and `footer.jsp` never exist as independently compiled Servlets at all when accessed only via this include mechanism (they *would* also be independently compilable if requested directly by URL, but that's a separate concern).

**When to use `<%@ include %>` vs. `<jsp:include>` (an action tag, covered in the upcoming Standard Action Tags topic) vs. `RequestDispatcher.include()`:** For **static, rarely-changing structural pieces** (headers, footers, navigation menus that are truly identical every time) — `<%@ include %>` is efficient and simple. For content that needs to be **decided dynamically per-request** (e.g., including a different "widget" JSP based on a user's role) — you need the request-time mechanisms instead. We'll make this decision framework fully concrete once `<jsp:include>` is formally introduced.

---

## 3. Taglib Directive — `<%@ taglib ... %>`

### Purpose

Declares a **tag library** — a collection of **custom, reusable tags** (like JSTL's `<c:if>`, `<c:forEach>`, which you'll learn in depth in upcoming JSTL topics) — making them available for use in this JSP file, under a chosen **prefix**.

### Syntax

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

- **`prefix="c"`** — the short alias you'll use before each tag from this library (e.g., `<c:if>`, `<c:forEach>` — the `c:` prefix is pure convention, universally used for JSTL Core, but technically you could choose any prefix name).
- **`uri="jakarta.tags.core"`** — identifies **which** tag library this refers to (this exact URI string is specific to Jakarta EE 9+'s JSTL Core library — older `javax`-based JSTL used a different URI string, `http://java.sun.com/jsp/jstl/core`, another concrete instance of Topic 4's `javax` vs `jakarta` migration affecting exact syntax you must get right for your specific Tomcat version).

**We will use this directive constantly starting from the upcoming JSTL Core Tags topic** — for now, understand its **purpose and placement**: it must appear **before** any tag from that library is used in the file, conventionally placed alongside the page directive at the very top.

**Preview — what taglib unlocks (full depth in JSTL topics):**
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<body>
    <c:if test="${username != null}">
        <p>Welcome, ${username}!</p>
    </c:if>
</body>
</html>
```
This `<c:if>` tag is **not** raw HTML — it's a **custom action** provided by the JSTL Core library, which Jasper translates into equivalent conditional Java logic during compilation, **without you writing a single scriptlet**. This is precisely the "cleaner alternative to scriptlets" flagged at the end of Topic 21 — we're now just naming the mechanism (`taglib` directive) that makes it possible; full JSTL syntax and tag catalogue comes in dedicated upcoming topics.

---

## EXECUTION FLOW — All Three Directives Together

```jsp
<%@ page import="java.util.Date" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="header.jsp" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html><body>
    <c:if test="${true}">
        <p>Time: <%= new Date() %></p>
    </c:if>
</body></html>
```

```
TRANSLATION TIME (once, or when files change):
        │
        ├── <%@ page import %>        → adds "import java.util.Date;" to generated class
        ├── <%@ page errorPage %>      → registers error-handling metadata for this page
        ├── <%@ include file %>        → MERGES header.jsp's raw content into this source, NOW
        ├── <%@ taglib %>               → tells Jasper how to interpret <c:if> tags below,
        │                                  generating the equivalent conditional Java logic
        │                                  (full mechanics in JSTL topics)
        │
        ▼
   Complete merged/translated Java source compiled into one .class file
        │
        ▼
REQUEST TIME (every request):
        │
        ▼
   _jspService() executes the fully-compiled logic — header content,
   conditional logic, and the date expression all run as ONE continuous method
```

---

## COMMON ERRORS

**Error: `<%@ include %>` file not found**
```
JasperException: File "/header.jsp" not found
```
- **Cause:** Incorrect relative path — `<%@ include file="..." %>` paths are typically relative to the **including JSP's own location**, not the web application root (unless you use a leading `/`, which makes it root-relative).
- **Fix:** Double-check relative vs. absolute (`/`-prefixed) paths; use a leading `/` for clarity when the included file lives in a different directory structure (e.g., `/WEB-INF/includes/header.jsp`).

**Error: Referencing `exception` implicit object without `isErrorPage="true"`**
```
JasperException: exception cannot be resolved
```
- **Cause:** Forgetting `<%@ page isErrorPage="true" %>` on the JSP that's supposed to **receive** forwarded exceptions.
- **Fix:** Always pair an `errorPage` JSP's declaration with `isErrorPage="true"` on the **target** page, exactly as shown in the example above.

**Error: Wrong taglib URI for your Jakarta EE version**
```
JasperException: The absolute uri: [http://java.sun.com/jsp/jstl/core] cannot be resolved
```
- **Cause:** Using the old `javax`-era JSTL URI with a Jakarta EE 10/Tomcat 10+ project (Topic 4's namespace migration issue, now manifesting specifically in taglib URIs).
- **Fix:** Use `jakarta.tags.core` (and the corresponding `jakarta.tags.*` URIs for other JSTL libraries) for Tomcat 10+/Jakarta EE 9+ projects — never mix `javax`-era tutorial URIs into a Jakarta-based project.

**Error: Multiple conflicting `<%@ page %>` attributes across the SAME page**
```
JasperException: The page attribute contentType is redefined
```
- **Cause:** Declaring the same page directive attribute (like `contentType`) **twice** with **different** values in the same file (though the **same** value repeated is generally tolerated) — the spec explicitly disallows redefining most attributes to conflicting values.
- **Fix:** Consolidate all `<%@ page %>` settings for a given attribute into a single, consistent declaration.

---

That completes **Directives — Page, Include, and Taglib** in full depth. You now understand exactly how page-wide configuration, translation-time file merging, and custom tag library registration each work — with the Include Directive's translation-time-vs-request-time distinction being a particularly important, precise piece of understanding to carry forward.

**Next up per your module order:** **Implicit Objects** — the complete catalogue of `request`, `response`, `session`, `out`, `application`, `config`, `pageContext`, `exception`, and `page`, explaining exactly where each comes from and when each is (or isn't) available.

Say **"Next"** to continue.