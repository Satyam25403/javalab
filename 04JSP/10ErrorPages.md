# TOPIC 30: Error Pages (JSP-Specific, Complete Treatment)

## CONCEPT

### Why this concept exists

Topic 22 introduced `errorPage`/`isErrorPage` as **attributes** of the page directive, and Topic 23 explained the resulting `exception` implicit object. Topic 16 covered `web.xml`'s application-wide `<error-page>`. This topic **consolidates all of it** into one complete, worked treatment — showing precisely how JSP-level and application-level error handling **interact**, and building genuinely complete, production-quality error pages.

### The Two Layers, Restated Precisely

| Layer | Declared where | Scope | Topic |
|---|---|---|---|
| **Page-level** | `<%@ page errorPage="..." %>` on the *source* JSP | Only exceptions thrown by **this specific JSP** | Topic 22 |
| **Application-level** | `<error-page>` in `web.xml` | **Any** uncaught exception/status code across the **entire app** (Servlets and JSPs alike) | Topic 5, 16 |

**Precedence, precisely restated:** if a JSP has its own `errorPage` attribute set, and an exception occurs on **that page**, the **page-level** setting takes effect first, forwarding to the JSP-specific error page. If a JSP has **no** `errorPage` attribute, any uncaught exception falls through to the **application-level** `web.xml` `<error-page>` mechanism instead. This is the same "specific overrides general" pattern seen throughout this course (Topic 4's URL mapping precedence, Topic 22's directive precedence).

---

## Complete Worked Example — Both Layers Working Together

### The "source" JSP that might throw an exception

```jsp
<%@ page import="java.util.List" %>
<%@ page errorPage="/WEB-INF/views/studentError.jsp" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<body>
    <h2>Student Details</h2>
<%
    List<Student> students = (List<Student>) request.getAttribute("students");
    Student first = students.get(0); // throws IndexOutOfBoundsException if list is empty!
%>
    <p>First student: <c:out value="${first.name}" /></p>
</body>
</html>
```

### The page-level error handler it points to

```jsp
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<body>
    <h2 style="color:red;">Unable to Display Student Details</h2>
    <p>We encountered a problem: <c:out value="${exception.message}" /></p>
    <p>Exception type: <c:out value="${exception.class.name}" /></p>
    <p><a href="students">Return to Student List</a></p>
</body>
</html>
```

**Notice `${exception.message}` and `${exception.class.name}` here — direct application of Topic 24's EL JavaBean-property access rule to the `exception` implicit object itself** — `exception.message` calls `exception.getMessage()`, `exception.class` calls `exception.getClass()` (a special-cased EL property, since every Java object has `getClass()`), and `.name` on that further calls `getName()`. This demonstrates EL's dot-notation chaining working smoothly even on exception objects, not just your own Model classes.

### The application-wide safety net (`web.xml`, Topic 5/16, unchanged from before but now shown working alongside the above)

```xml
<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/views/genericError.jsp</location>
</error-page>
```

### Tracing Precedence With a Concrete Scenario

**Scenario A — exception thrown inside the "source" JSP shown above (which HAS a page-level `errorPage`):**
```
Exception thrown in studentList.jsp
        │
        ▼
This JSP has errorPage="/WEB-INF/views/studentError.jsp" declared
        │
        ▼
Container forwards SPECIFICALLY to studentError.jsp
        │
        ▼
web.xml's generic <error-page> is NEVER consulted — the page-level
setting fully handles this, taking precedence
```

**Scenario B — exception thrown inside a DIFFERENT JSP (or a Servlet) with NO page-level `errorPage` set:**
```
Exception thrown in some OTHER page (no errorPage attribute at all)
        │
        ▼
No page-level handling exists for this specific page
        │
        ▼
Exception propagates all the way up to the container
        │
        ▼
Container consults web.xml's <error-page> declarations (Topic 16's mechanism)
        │
        ▼
Forwards to genericError.jsp instead
```

---

## Building a Genuinely Complete, Production-Quality Error Page

Combining everything learned across Topics 16, 22, 23, 24, 26:

```jsp
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head><title>Error</title></head>
<body>
    <h2 style="color:red;">Something went wrong</h2>

    <c:choose>
        <c:when test="${pageContext.errorData.statusCode == 404}">
            <p>The page you requested could not be found.</p>
        </c:when>
        <c:when test="${not empty exception}">
            <p>We encountered an unexpected error while processing your request.</p>
            <!-- Log full detail server-side; show minimal detail to the user -->
            <%
                getServletContext().log("Unhandled exception on error page", exception);
            %>
        </c:when>
        <c:otherwise>
            <p>An unknown error occurred.</p>
        </c:otherwise>
    </c:choose>

    <p>Requested URL: <c:out value="${pageContext.errorData.requestURI}" /></p>
    <p><a href="${pageContingContextPath}/index.jsp">Return to Home</a></p>
</body>
</html>
```

**New element: `pageContext.errorData`** — recall Topic 23's `pageContext` "super object" — on an error page (`isErrorPage="true"`), it exposes an `ErrorData` object via `getErrorData()`, offering `getStatusCode()`, `getRequestURI()`, `getThrowable()` — an alternative, slightly more structured way to access error information than the raw `exception` implicit object alone, particularly useful for distinguishing **status-code-triggered** errors (like 404s, which may have **no** associated exception at all) from **exception-triggered** errors.

**Critical security practice, directly reapplying Topic 16's core lesson:** notice the exception's full detail is **logged server-side** (`getServletContext().log(...)`), while the **user-facing** message stays generic and friendly — this is the same "log details, show friendliness" discipline from Topic 16, now demonstrated specifically within a JSP error page rather than a Servlet.

---

## EXECUTION FLOW — Complete Trace, Both Mechanisms Unified

```
Browser requests /myapp/students
        │
        ▼
StudentListServlet forwards to studentList.jsp with an EMPTY list
        │ (perhaps the database genuinely had zero students, an edge case
        │  the developer didn't anticipate)
        ▼
studentList.jsp's scriptlet: students.get(0) throws IndexOutOfBoundsException
        │
        ▼
Container checks: does studentList.jsp have a page-level errorPage set? YES
        │
        ▼
Forwards to studentError.jsp (page-level handler)
   → populates the `exception` implicit object (since isErrorPage="true" there)
        │
        ▼
studentError.jsp renders, using EL to display exception.message,
exception.class.name — user sees a friendly, specific message
        │
        ▼
[If studentError.jsp itself somehow also threw an exception — a genuinely
 rare "error page failing" edge case — that WOULD then fall through to
 web.xml's application-wide <error-page> as the final safety net]
```

---

## COMMON ERRORS

**Error: Infinite loop between error pages**
- **Cause:** An error page that itself throws an exception, and is (perhaps accidentally) configured to forward to itself, or to another error page that loops back.
- **Fix:** Keep error pages **extremely simple** — minimal logic, no database calls, no complex EL chains that could themselves fail — precisely to avoid this class of catastrophic failure. This is a genuinely important design principle: **your error-handling code must be more reliable than the code it's handling errors for.**

**Error: `exception` is `null` on a page reached via a status-code-based `<error-page>` (not an exception-based one)**
- **Cause:** Recall Topic 16 — `<error-code>`-triggered error pages (e.g., a 404) may have **no associated `Throwable` at all** — there was no exception, just a status code. Referencing `exception.getMessage()` in this scenario throws a `NullPointerException` **on your error page itself**.
- **Fix:** Always null-check `exception` before use (`${not empty exception}`, as shown in the complete example above), or use `pageContext.errorData.statusCode` for status-code-driven scenarios instead of assuming an exception object always exists.

**Error: Forgetting `isErrorPage="true"` (repeated from Topic 22/23, worth reinforcing in this consolidated context)**
- **Fix:** Every JSP that serves as an error-handling target (whether via page-level `errorPage` or application-level `web.xml` `<error-page>`) must declare `isErrorPage="true"` to safely access the `exception` implicit object.

---

That completes **Error Pages** — fully consolidating Topics 16, 22, and 23's scattered pieces into one complete, working error-handling strategy spanning both page-level and application-level mechanisms, with genuine production-quality practices (logging detail, showing friendliness, avoiding error-page fragility).

**Next up per your module order:** **Session Handling** (in JSP specifically) — largely a consolidation of Topic 11's `HttpSession` concepts as accessed through JSP's `session` implicit object (Topic 23) and EL's `sessionScope` (Topic 24), with JSP-specific patterns for login-state display.

Say **"Next"** to continue.