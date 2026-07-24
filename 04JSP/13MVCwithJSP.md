# TOPIC 33: MVC with JSP — Capstone Integration

## CONCEPT

### Why this topic exists

This is the **integration point** for everything taught across both modules so far. Topic 1 established the MVC principle abstractly, using a minimal login example before JSTL/EL even existed in your toolkit. Now, with the **complete** View-layer toolkit (EL, JSTL Core/Formatting, implicit objects, session handling, includes), we build one **fully realistic, properly-layered** application slice — the **exact architectural template** every Module 4 project will follow, just swapped to real JDBC data in Module 3.

### The Complete Architecture, Precisely Restated

```
┌─────────────────────────────────────────────────────────────────┐
│  CONTROLLER (Servlet)                                              │
│  - Receives request                                                 │
│  - Reads parameters (Topic 8)                                        │
│  - Delegates to Model/Service for business logic (Topic 1)             │
│  - Manages session state — WRITES only (Topic 31)                       │
│  - Chooses the View, forwards request+response (Topic 9)                 │
│  - NO HTML. NO SQL directly (Topic 28's lesson). NO scriptlet-equivalent  │
└──────────────────────────────┬────────────────────────────────────┘
                                │ request.setAttribute(...) + forward()
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  MODEL (POJOs / Service / DAO — Module 3 will add real JDBC here)    │
│  - Plain Java classes, JavaBean convention (getters/setters, Topic 24)│
│  - ZERO jakarta.servlet.* imports (Topic 1)                            │
│  - Business logic and (eventually) data access live here                │
└─────────────────────────────────────────────────────────────────┘
                                │ objects/Lists passed as request attributes
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  VIEW (JSP)                                                          │
│  - JSTL Core for control flow (Topic 26)                              │
│  - JSTL Formatting for display (Topic 27)                              │
│  - EL for reading Model data (Topic 24)                                 │
│  - sessionScope for reading (never writing) session state (Topic 31)     │
│  - <c:out> for any user-submitted content (Topic 26's XSS fix)             │
│  - ZERO scriptlets in a well-built page                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Complete Worked Example — "Student Registration & Listing" Slice

This mirrors, in miniature, exactly what your Module 4 Student Management System project will look like once JDBC (Module 3) replaces the in-memory list shown here.

### Model — `Student.java`

```java
package com.company.myapp.model;

import java.util.Date;

public class Student {

    private int id;
    private String name;
    private int age;
    private Date enrollmentDate;

    public Student() {}

    public Student(int id, String name, int age, Date enrollmentDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.enrollmentDate = enrollmentDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
```

**Notice:** proper JavaBean getters/setters (required for EL, Topic 24), zero `jakarta.servlet.*` imports (Topic 1's MVC discipline), a no-arg constructor (required if ever used with `<jsp:useBean>`, Topic 25).

### Model — `StudentService.java` (Business Logic Layer — Foreshadowing Module 3's DAO Pattern)

```java
package com.company.myapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.company.myapp.model.Student;

public class StudentService {

    // TEMPORARY in-memory storage — Module 3 replaces this with a real
    // database-backed DAO (StudentDAO), following the exact same method signatures
    private static final List<Student> students = new ArrayList<>();
    private static int nextId = 1;

    public static List<Student> getAllStudents() {
        return students;
    }

    public static void addStudent(String name, int age) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Age must be a realistic positive number.");
        }
        students.add(new Student(nextId++, name.trim(), age, new Date()));
    }
}
```

**Why this class exists as a separate layer from the Servlet, even without a database yet:** this is the **Service layer** — recall Topic 16's `AccountService`/`LoginService` pattern — business rules (validation, "what counts as a valid student") live here, independent of HTTP concerns entirely. When Module 3 introduces JDBC, this class's method **bodies** change (replacing the `ArrayList` with SQL calls via a DAO), but its **method signatures**, and every Servlet/JSP that depends on them, **remain unchanged** — a direct, practical benefit of proper layering.

### Controller — `StudentController.java`

```java
package com.company.myapp.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.company.myapp.model.Student;
import com.company.myapp.service.StudentService;

@WebServlet("/students")
public class StudentController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = StudentService.getAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/WEB-INF/views/studentList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");

        try {
            int age = Integer.parseInt(ageStr);
            StudentService.addStudent(name, age);
            response.sendRedirect("students"); // Post-Redirect-Get, Topic 9
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Age must be a valid number.");
            request.getRequestDispatcher("/WEB-INF/views/studentForm.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/studentForm.jsp").forward(request, response);
        }
    }
}
```

**Every decision here reflects a topic already taught:** `doGet()`/`doPost()` split (Topic 3), delegating validation to the Model layer (Topic 16), `sendRedirect()` after successful POST — Post-Redirect-Get (Topic 9) — versus `forward()` on validation failure (staying on the same request so `errorMessage` remains visible), specific-before-general exception catching (Topic 16).

### View — `studentForm.jsp`

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<body>
    <h2>Register a Student</h2>

    <c:if test="${not empty errorMessage}">
        <p style="color:red;"><c:out value="${errorMessage}" /></p>
    </c:if>

    <form action="students" method="post">
        <label>Name:</label>
        <input type="text" name="name" value="${param.name}"><br>
        <label>Age:</label>
        <input type="text" name="age" value="${param.age}"><br>
        <input type="submit" value="Register">
    </form>
</body>
</html>
```

**Notice `value="${param.name}"`** — Topic 24's `param` implicit object — this **re-populates the form field** with whatever the user previously submitted, so a validation failure doesn't force them to retype everything — a genuinely good UX practice, achieved with zero scriptlets.

### View — `studentList.jsp`

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<body>
    <h2>Registered Students</h2>

    <c:choose>
        <c:when test="${empty students}">
            <p>No students registered yet.</p>
        </c:when>
        <c:otherwise>
            <table border="1">
                <tr><th>#</th><th>Name</th><th>Age</th><th>Enrolled</th></tr>
                <c:forEach var="s" items="${students}" varStatus="status">
                    <tr>
                        <td>${status.count}</td>
                        <td><c:out value="${s.name}" /></td>
                        <td>${s.age}</td>
                        <td><fmt:formatDate value="${s.enrollmentDate}" pattern="dd-MMM-yyyy" /></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <p><a href="studentFormPage.jsp">Register a new student</a></p>
</body>
</html>
```

**This single file demonstrates Topics 24, 26, and 27 working together in one realistic, complete display** — EL property access (`s.name`, `s.age`), JSTL conditionals (`<c:choose>`, `<c:when>`), JSTL iteration with status tracking (`<c:forEach varStatus>`), safe output (`<c:out>`), and formatted date display (`<fmt:formatDate>`) — **zero scriptlets anywhere in this View file.**

---

## EXECUTION FLOW — The Complete Round Trip

```
Browser: GET /myapp/students
        │
        ▼
StudentController.doGet()
   → StudentService.getAllStudents() [MODEL — business logic layer]
   → request.setAttribute("students", list)
   → forward() to studentList.jsp
        │
        ▼
studentList.jsp [VIEW]
   → <c:forEach> iterates the List, EL reads each Student's properties,
     <fmt:formatDate> formats the enrollment date
        │
        ▼
Rendered HTML response sent to browser

═══════════════════════════════════════════════════════

Browser: POST /myapp/students  (name=Rahul, age=abc — INVALID age)
        │
        ▼
StudentController.doPost()
   → Integer.parseInt("abc") throws NumberFormatException
   → caught, request.setAttribute("errorMessage", ...)
   → forward() (NOT redirect — same request, error message preserved)
     to studentForm.jsp
        │
        ▼
studentForm.jsp [VIEW]
   → <c:if test="${not empty errorMessage}"> displays the error
   → ${param.name}/${param.age} re-populate the form with prior input
        │
        ▼
User corrects the age, resubmits
        │
        ▼
StudentController.doPost() — this time StudentService.addStudent()
   succeeds → response.sendRedirect("students")  [Post-Redirect-Get, Topic 9]
        │
        ▼
Browser makes a NEW GET request to /myapp/students
        │
        ▼
[Same flow as the very first trace above — student list now includes the new entry]
```

---

## Why This Architecture Scales to Module 4's Real Projects

Every one of your five Module 4 projects (Login System, Student/Employee/Library Management, Banking) will follow this **exact same shape**:

```
[Model classes: POJOs with JavaBean getters/setters]
[Service/DAO layer: business logic, eventually real JDBC in Module 3]
[Controller Servlets: one or more per entity, doGet()/doPost() split]
[View JSPs: JSTL + EL, zero scriptlets, forms re-populated on error]
```

**The only thing Module 3 changes is what's *inside* the Service/DAO layer** — replacing the `ArrayList` here with real `PreparedStatement`/`ResultSet` JDBC calls (Module 3's upcoming topics) — the Controller and View layers, and the overall architecture, remain **structurally identical** to what you've just seen.

---

## COMMON ERRORS (Consolidating the Whole-Architecture Perspective)

**Error: Business logic accidentally creeping into the Servlet**
```java
// BAD — validation logic directly in the Servlet, bypassing the Service layer
if (name == null || name.isEmpty()) { ... }
```
- **Fix:** Push this into `StudentService`, exactly as shown — keeps the Servlet a thin, readable orchestrator.

**Error: Forgetting to re-populate form fields on validation failure (a UX issue, not a crash)**
- **Fix:** Use `${param.xxx}` in `value="..."` attributes, as shown, so users don't lose their input on a validation round-trip.

**Error: Mixing `forward()` and `sendRedirect()` inconsistently**
- **Fix:** Recall Topic 9's Post-Redirect-Get rule precisely: **redirect after success** (prevents duplicate resubmission), **forward on validation failure** (preserves the request-scoped error message and original input without an extra round trip).

---

That completes **MVC with JSP** — the capstone that unifies Module 1's architecture with Module 2's complete View-layer toolkit into one coherent, realistic, fully-traced example. This is, structurally, the **exact template** for everything in Module 4.

**Next up per your module order:** **JSP Best Practices** — the Module 2 equivalent of Topic 19's consolidated checklist, followed by **JSP vs Servlet** and **JSP vs Thymeleaf** to close out Module 2 entirely.

Say **"Next"** to continue.