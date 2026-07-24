# TOPIC 28: JSTL SQL Tags (Basics)

## CONCEPT

### Why this concept exists — and an important, honest framing upfront

Your syllabus lists this as "JSTL SQL Tags (basics)" — and it's taught here **specifically as basic, exam-relevant awareness**, not as a technique you should actually use in real applications. This topic exists because: (1) university courses and exams frequently test whether you **know these tags exist and how they work**, and (2) understanding *why* they're discouraged in practice is itself a valuable lesson about **architecture and separation of concerns** — directly reinforcing Topic 1's MVC principle from a new angle.

### What these tags do, conceptually

JSTL's **SQL tag library** (`sql:` prefix) lets a JSP execute database queries **directly inside the page** — setting up a data source, running a query, and iterating over results — all without a single line of Java or a separate Servlet/DAO layer.

### Why this is explicitly discouraged in real projects (the honest, important part)

This directly **violates Topic 1's MVC principle** in the most severe way possible: it puts **data access logic directly inside the View layer**. Recall Topic 1's best practice: *"Views (JSP) should contain minimal Java logic... never raw JDBC calls or business logic."* `sql:` tags do **exactly** the thing that principle warns against — they make a JSP responsible for connecting to a database, executing SQL, and handling connection lifecycle, all mixed directly into presentation markup.

**Concrete problems this causes:**
1. **No separation of concerns** — a designer editing this JSP for styling could accidentally break a database query, or vice versa.
2. **SQL Injection risk** (foreshadowing Module 3's dedicated topic) — improper use of these tags with unescaped user input is a common, severe vulnerability.
3. **No reusability** — the exact same query logic can't be reused by a different View or a non-web client (e.g., a REST API) without duplicating it.
4. **Testing difficulty** — you cannot unit-test a JSP's embedded SQL the way you can test a properly isolated DAO class (Module 3's upcoming DAO Pattern topic).

**This is precisely why your course syllabus itself lists "MVC using Servlet + JSP + JDBC" and "DAO Pattern" as Module 3 topics** — the proper, professional approach **replaces** what you're about to learn here with a clean Servlet → Service → DAO → JSP flow. Learn this topic for genuine understanding and exam readiness; **do not use it** in your Module 4 projects.

---

## Setup — Taglib Directive

```jsp
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
```

A **third** distinct JSTL tag library URI (alongside Topic 26's `jakarta.tags.core` and Topic 27's `jakarta.tags.fmt`) — same overall JSTL dependency, separate namespace.

---

## `<sql:setDataSource>` — Configuring the Database Connection

```jsp
<sql:setDataSource var="myDataSource"
    driver="com.mysql.cj.jdbc.Driver"
    url="jdbc:mysql://localhost:3306/mydb"
    user="root"
    password="yourpassword" />
```

This establishes connection details **directly inside the JSP**, made available under the variable name `myDataSource` for subsequent `sql:` tags in the same page. (Full JDBC connection mechanics — `DriverManager`, `Connection` objects — are formally covered in Module 3; this tag is essentially wrapping that same underlying JDBC machinery.)

---

## `<sql:query>` — Executing a SELECT Query

```jsp
<sql:query var="studentResults" dataSource="${myDataSource}">
    SELECT id, name, age FROM students
</sql:query>
```

**`var="studentResults"`** now holds a `Result` object (a JSTL-specific wrapper), which behaves somewhat like a simplified `ResultSet` (Module 3's upcoming topic) — accessible via EL and `<c:forEach>` (Topic 26).

### Displaying Results — Combining With Topic 26's `<c:forEach>`

```jsp
<table border="1">
    <tr><th>ID</th><th>Name</th><th>Age</th></tr>
    <c:forEach var="row" items="${studentResults.rows}">
        <tr>
            <td>${row.id}</td>
            <td><c:out value="${row.name}" /></td>
            <td>${row.age}</td>
        </tr>
    </c:forEach>
</table>
```

**Notice `${studentResults.rows}`** — the `Result` object exposes a `.rows` property (an array/collection of row objects), and each `row.columnName` accesses that column's value for the current row — following the **exact same JavaBean-style dot-notation convention** established since Topic 24.

---

## `<sql:update>` — Executing INSERT/UPDATE/DELETE

```jsp
<sql:update dataSource="${myDataSource}" var="rowsAffected">
    INSERT INTO students (name, age) VALUES ('Rahul', 21)
</sql:update>

<p>Rows affected: ${rowsAffected}</p>
```

---

## `<sql:param>` — Parameterized Queries (Critically Important, Even at "Basics" Level)

```jsp
<sql:query var="result" dataSource="${myDataSource}">
    SELECT * FROM students WHERE name = ?
    <sql:param value="${param.searchName}" />
</sql:query>
```

**This is genuinely important to flag even in a "basics"-level topic, foreshadowing Module 3's SQL Injection Prevention:** notice the `?` placeholder combined with `<sql:param>`, rather than directly concatenating `${param.searchName}` into the SQL string like this:

```jsp
<!-- DANGEROUS — NEVER DO THIS, even in a basics example -->
<sql:query var="result" dataSource="${myDataSource}">
    SELECT * FROM students WHERE name = '${param.searchName}'
</sql:query>
```

**Why the second version is a severe SQL Injection vulnerability:** if a malicious user submitted `searchName` as `' OR '1'='1`, the resulting query becomes `SELECT * FROM students WHERE name = '' OR '1'='1'` — which returns **every single row** in the table, completely bypassing the intended filter. This exact vulnerability class, and its proper prevention (`PreparedStatement`'s `?` placeholders), is the **entire subject** of Module 3's dedicated "SQL Injection Prevention" topic — `<sql:param>` here is simply JSTL's tag-based wrapper around that same safe, parameterized-query principle. **Even though you shouldn't use `sql:` tags in real projects, the underlying lesson about parameterization applies identically and directly to the JDBC `PreparedStatement` code you'll write by hand in Module 3.**

---

## Complete Example (For Understanding Only — Not a Pattern to Reuse)

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<sql:setDataSource var="ds"
    driver="com.mysql.cj.jdbc.Driver"
    url="jdbc:mysql://localhost:3306/mydb"
    user="root" password="pass" />

<sql:query var="students" dataSource="${ds}">
    SELECT id, name, age FROM students ORDER BY name
</sql:query>

<html>
<body>
    <h2>Student List (Demonstration Only)</h2>
    <table border="1">
        <tr><th>ID</th><th>Name</th><th>Age</th></tr>
        <c:forEach var="row" items="${students.rows}">
            <tr>
                <td>${row.id}</td>
                <td><c:out value="${row.name}" /></td>
                <td>${row.age}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
```

**What the corresponding, actually-correct Module 3/4 architecture will look like instead (preview):**
```
Browser → StudentListServlet (Controller)
              │
              ▼
       StudentDAO.getAllStudents()  (Model — proper JDBC, Module 3)
              │
              ▼
       returns List<Student>
              │
              ▼
       request.setAttribute("students", list); forward to JSP
              │
              ▼
       studentList.jsp (View) — uses <c:forEach> over the List,
       with ZERO knowledge of SQL, connections, or the database at all
```

**This is precisely the architecture Topic 26's complete example already showed you** — you're now seeing, explicitly, why that pattern (Servlet fetches data, JSP only displays it) is the correct one, by directly contrasting it against the discouraged alternative this topic covers.

---

## EXECUTION FLOW — What Happens Internally (Brief, Since This Won't Be Your Real Pattern)

```
Request arrives for a JSP containing <sql:query>
        │
        ▼
Translation/execution reaches the <sql:setDataSource> tag
   → establishes a JDBC Connection internally (Module 3 mechanics)
        │
        ▼
<sql:query> tag executes
   → obtains a Statement/PreparedStatement (Module 3),
     runs the SQL, wraps the ResultSet into a JSTL Result object
        │
        ▼
Connection is closed internally by the tag (connection lifecycle
management is hidden from you — which is ALSO part of the problem:
you have no control over connection pooling, Module 3's upcoming topic,
when it's buried inside a JSP tag like this)
        │
        ▼
<c:forEach> iterates the Result object's rows, rendering HTML
```

---

## COMMON ERRORS

**Error: `ClassNotFoundException` for the JDBC driver**
```
java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
- **Cause:** MySQL Connector JAR (Topic 1, Step 4.4) missing or incorrectly scoped in `pom.xml`.
- **Fix:** Same fix as Module 1's dependency troubleshooting — verify `mysql-connector-j` is present with default (non-`provided`) scope.

**Error: SQL Injection via unparameterized queries (the most important conceptual "error" in this topic)**
- Already covered in full detail above — **never** concatenate user input directly into SQL text, always use `<sql:param>` (or, in real code, `PreparedStatement`, Module 3).

**Error: Confusing this topic's convenience with actual best practice**
- **The most important "error" to avoid is architectural, not syntactic:** using `sql:` tags in your actual Module 4 projects, believing they're an acceptable shortcut. They are not — Module 3's DAO pattern is the required, correct approach for your coursework, and this topic exists purely for recognition/exam purposes.

---

That completes **JSTL SQL Tags (Basics)** — deliberately taught with equal emphasis on **how it works** (for exam completeness) and **why you won't actually use it** (reinforcing MVC and foreshadowing Module 3's proper JDBC/DAO/SQL-Injection-Prevention topics). This is a good example of a topic where understanding the discouraged approach *is* part of understanding the correct one.

**Next up per your module order:** **Custom Tags (Introduction)** — a brief conceptual introduction to writing your **own** JSP tags (Tag Handler classes, Tag Files), understanding this as the mechanism JSTL itself is built on top of.

Say **"Next"** to continue.