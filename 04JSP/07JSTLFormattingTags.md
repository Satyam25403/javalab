# TOPIC 27: JSTL Formatting Tags

## CONCEPT

### Why this concept exists

Topic 26 gave you `<c:forEach>` to loop over a `List<Employee>`, but what happens when you need to display an `Employee`'s salary as `$45,000.00` instead of the raw double `45000.0`, or a `Student`'s enrollment date as `23-Jul-2026` instead of Java's default `Date.toString()` format (`Thu Jul 23 00:00:00 IST 2026`)? **JSTL Formatting Tags** (the `fmt:` prefix library) exist specifically to solve **presentation formatting** for numbers, currency, and dates — declaratively, without scriptlets or manual `SimpleDateFormat`/`NumberFormat` Java code embedded in your JSP.

### What problem it solves precisely

Recall Topic 22's Page Directive example, which used a scriptlet:
```jsp
<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formattedDate = sdf.format(new Date());
%>
<%= formattedDate %>
```

This is **exactly** the pattern JSTL Formatting Tags eliminate — replacing Java date/number formatting classes with declarative tags, continuing the same philosophy established in Topic 26 (JSTL Core replacing control-flow scriptlets; JSTL Formatting replaces data-formatting scriptlets).

### Setup — Taglib Directive

```jsp
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
```

**Note the different URI** from Core's `jakarta.tags.core` (Topic 26) — Formatting is a **separate** tag library, requiring its own taglib declaration, even though it's part of the same overall JSTL distribution/dependency (Topic 1, Step 4.4's `jakarta.servlet.jsp.jstl` JAR covers both Core and Formatting tags together).

---

## `<fmt:formatNumber>` — Numbers and Currency

```jsp
<fmt:formatNumber value="45000" type="currency" />
<!-- Output (US locale default): $45,000.00 -->

<fmt:formatNumber value="0.1834" type="percent" />
<!-- Output: 18% -->

<fmt:formatNumber value="1234567.891" type="number" maxFractionDigits="2" />
<!-- Output: 1,234,567.89 -->
```

| Attribute | Purpose |
|---|---|
| `value` | The number to format (accepts EL expressions: `value="${employee.salary}"`) |
| `type` | `"number"` (default), `"currency"`, or `"percent"` |
| `pattern` | A custom format pattern (e.g., `"#,##0.00"`), overriding `type` for full control |
| `maxFractionDigits` / `minFractionDigits` | Controls decimal precision |
| `groupingUsed` | `true`/`false` — whether to show thousand-separators (commas) |

### Code Example — Formatting an Employee's Salary

```jsp
<c:forEach var="emp" items="${employees}">
    <tr>
        <td><c:out value="${emp.name}" /></td>
        <td><fmt:formatNumber value="${emp.salary}" type="currency" /></td>
    </tr>
</c:forEach>
```

**Notice this combines directly with Topic 26's `<c:forEach>` and `<c:out>`** — this is exactly how real Module 4 project JSPs (Employee Management, specifically) will display formatted salary figures, with zero scriptlets, zero manual `NumberFormat` calls.

### Custom Patterns — `pattern` Attribute

```jsp
<fmt:formatNumber value="45000.5" pattern="Rs. #,##0.00" />
<!-- Output: Rs. 45,000.50 -->
```

The `pattern` attribute uses **exactly the same pattern syntax** as Java's `java.text.DecimalFormat` (`#` = optional digit, `0` = required digit, `,` = grouping separator, `.` = decimal point) — useful when the built-in `type="currency"` locale-based symbol (`$`, by default) doesn't match your needs (e.g., wanting to show `Rs.` for Indian Rupees explicitly, rather than relying on locale detection, covered next).

---

## `<fmt:formatDate>` — Dates and Times

```jsp
<fmt:formatDate value="${student.enrollmentDate}" pattern="dd-MMM-yyyy" />
<!-- Output: 23-Jul-2026 -->
```

| Attribute | Purpose |
|---|---|
| `value` | A `java.util.Date` object (must already be a `Date` — **not** a `String**; formatting a date **stored as text** requires converting it to a real `Date` object first, typically in your Servlet/Model layer) |
| `type` | `"date"` (default), `"time"`, or `"both"` — controls which portion is shown when using `dateStyle`/`timeStyle` instead of a custom `pattern` |
| `pattern` | A custom format pattern (same syntax as Java's `SimpleDateFormat`: `dd` = day, `MM` = month number, `MMM` = month abbreviation, `yyyy` = 4-digit year, `HH:mm:ss` = 24-hour time) |
| `dateStyle` / `timeStyle` | `"short"`, `"medium"`, `"long"`, `"full"` — locale-aware preset styles, alternative to a manual `pattern` |

### Code Example

```jsp
<p>Enrolled on: <fmt:formatDate value="${student.enrollmentDate}" pattern="dd-MM-yyyy" /></p>
<p>Enrolled on (long style): <fmt:formatDate value="${student.enrollmentDate}" dateStyle="long" /></p>
<!-- Output: July 23, 2026 -->

<p>Current timestamp: <fmt:formatDate value="${now}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></p>
```

**Important prerequisite — `${now}` in the example above requires a real `java.util.Date` object to already exist in some scope** — typically set via a scriptlet or, more idiomatically, `<jsp:useBean>` with a class that provides the current date, or simply passed from the Servlet:
```java
request.setAttribute("now", new java.util.Date());
```

**Common exam/practical gotcha, worth flagging precisely:** `<fmt:formatDate>` **cannot** format a `java.time.LocalDate`/`LocalDateTime` (Java 8+'s modern date-time API) directly in older JSTL versions — it expects the legacy `java.util.Date` type. If your Model classes use `LocalDate` (a genuinely common modern choice), you'd typically convert it to `java.util.Date` in your Servlet/Model layer before passing it to the JSP, or use a scriptlet/custom formatting as a workaround. This is a real, practical friction point between "modern Java" and "JSTL's somewhat dated API design" worth being aware of as you move into Module 3's JDBC work, where date handling becomes very relevant.

---

## Locale Handling — `<fmt:setLocale>`

```jsp
<fmt:setLocale value="fr_FR" />
<fmt:formatNumber value="45000" type="currency" />
<!-- Output: 45 000,00 € (French formatting: space as grouping separator, comma as decimal, Euro symbol) -->
```

**Why this matters conceptually:** all of JSTL's formatting tags are **locale-aware by default** — without any explicit `<fmt:setLocale>`, they use the **server's default locale** (or, in more sophisticated setups, the browser's `Accept-Language` header, Topic 8's header discussion). `<fmt:setLocale>` lets you **override** this explicitly — genuinely useful for applications that need to support multiple regions/currencies (an "internationalization," or "i18n," concern) — though for your university coursework and Module 4 projects, you'll typically rely on the default locale unless a project specifically calls for multi-locale support.

---

## `<fmt:parseNumber>` and `<fmt:parseDate>` — The Reverse Operation

Occasionally you need to go the **other direction** — parsing a formatted **String** back into a numeric/date value (e.g., reading a user-submitted date string and validating it):

```jsp
<fmt:parseNumber var="parsedSalary" value="45,000.50" type="currency" />
<p>Parsed value: ${parsedSalary}</p>
<!-- Output: 45000.5 (as an actual number, grouping/currency symbols stripped) -->
```

This is less commonly needed in typical display-focused JSPs (parsing user input is more naturally done in the **Servlet/Java layer**, following Topic 1's MVC principle, rather than in the View) — included here for reference completeness, since it directly parallels `formatNumber`/`formatDate` and may appear in exam questions testing the full `fmt:` tag family.

---

## Complete Realistic Example — Employee Management Display (Direct Module 4 Preview)

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<body>
    <h2>Employee Directory</h2>
    <table border="1">
        <tr><th>Name</th><th>Joining Date</th><th>Salary</th></tr>
        <c:forEach var="emp" items="${employees}">
            <tr>
                <td><c:out value="${emp.name}" /></td>
                <td><fmt:formatDate value="${emp.joiningDate}" pattern="dd-MMM-yyyy" /></td>
                <td><fmt:formatNumber value="${emp.salary}" type="currency" /></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
```

This single example demonstrates **JSTL Core and Formatting tags working together seamlessly** — exactly the View-layer code you'll write for the Employee Management System project in Module 4, once `emp.joiningDate` and `emp.salary` are populated from a real MySQL database via JDBC (Module 3).

---

## EXECUTION FLOW — Confirming the Same Translation Principle

```
<fmt:formatNumber value="${emp.salary}" type="currency" />
        │
        ▼
Translation phase (same Jasper mechanism, Topic 20/26):
        │
        ▼
Generated Java code (conceptually):
   NumberFormat nf = NumberFormat.getCurrencyInstance(currentLocale);
   out.write(nf.format(pageContext.findAttribute("emp") 
                          → getSalary() call → formatted string));
        │
        ▼
Still ordinary Java, using java.text.NumberFormat/DateFormat classes
internally — fmt: tags are a declarative wrapper around EXACTLY the
same Java formatting APIs you could call manually in a scriptlet
```

**Reinforcing Topic 26's core insight once more:** JSTL Formatting tags aren't a separate formatting engine — they're generating calls to the **same** `java.text.NumberFormat`/`DateFormat`/`SimpleDateFormat` classes from the standard Java library, just wrapped in clean, XML-based, scriptlet-free syntax.

---

## COMMON ERRORS

**Error: `<fmt:formatDate>` throwing a type mismatch / ClassCastException**
```
jakarta.el.ELException: Cannot convert ... to java.util.Date
```
- **Cause:** Passing a `String` (or a `java.time.LocalDate`, per the gotcha flagged above) instead of an actual `java.util.Date` object to the `value` attribute.
- **Fix:** Ensure the Model/Servlet layer provides a genuine `java.util.Date` object; convert `LocalDate`/`LocalDateTime` to `Date` beforehand if needed (e.g., `Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())`).

**Error: Forgetting the separate `fmt` taglib directive (having only declared `c`)**
```
JasperException: tag library ... prefix fmt ... does not have a definition
```
- **Fix:** Both `<%@ taglib prefix="c" uri="jakarta.tags.core" %>` AND `<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>` must be declared independently — one does not imply the other, even though both come from the same underlying JSTL dependency.

**Error: Unexpected currency symbol (e.g., expecting ₹ or Rs., seeing $ instead)**
- **Cause:** No explicit `<fmt:setLocale>` set, so the JVM/container's **default** locale (often US, depending on server configuration) is used for `type="currency"`.
- **Fix:** Either use `<fmt:setLocale value="en_IN" />` for Indian Rupee formatting, or use an explicit `pattern` attribute (e.g., `pattern="₹#,##0.00"`) to sidestep locale-dependence entirely when you need a guaranteed specific symbol regardless of server configuration.

**Error: `<fmt:formatNumber>`/`<fmt:formatDate>` silently displaying nothing**
- **Cause:** Same root cause as Topic 24/26's silent-failure pattern — `value="${emp.salary}"` evaluates to `null` (attribute doesn't exist, or property getter mismatch, Topic 24's JavaBean convention issue) — no exception, just blank output.
- **Fix:** Verify the underlying EL expression resolves correctly on its own (`<c:out value="${emp.salary}" />` alone) before layering formatting tags on top, to isolate whether the problem is the data itself or the formatting tag.

---

That completes **JSTL Formatting Tags**. Combined with Topic 26's Core tags, you now have the complete, professional, scriptlet-free toolkit for both **control flow** and **data presentation** in JSP — exactly what every View in your Module 4 projects will rely on.

**Next up per your module order:** **JSTL SQL Tags (basics)** — the `sql:` tag library that lets a JSP execute basic database queries directly, along with an important, honest discussion of why this is taught only at a "basics" awareness level and why real projects (including yours in Module 4) will instead use the proper JDBC/DAO pattern from Module 3.

Say **"Next"** to continue.