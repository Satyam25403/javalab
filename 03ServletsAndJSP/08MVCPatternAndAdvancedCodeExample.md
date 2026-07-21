# TOPIC 1 (continued): MVC Pattern → Advanced Code Example

## MVC Pattern — Concept

### Why this concept exists

Look back at the medium example (`LoginServlet`). Notice the problem: **validation logic, business logic, AND HTML presentation are all mixed together in one Java class.** If a designer wants to change the login page's styling, they'd have to edit Java code and recompile. If you wanted to reuse the same validation logic for a mobile app's API, you'd have to rip HTML-printing code out of the middle of your business logic. This tangled structure is called **spaghetti code**, and it violates one of the most fundamental principles in software engineering: **Separation of Concerns**.

**MVC (Model-View-Controller)** exists specifically to solve this by splitting an application into three distinct, independently-changeable responsibilities.

### What problem it solves

- **Maintainability** — changing the UI shouldn't require touching business logic, and vice versa.
- **Reusability** — the same business logic (Model) can serve multiple views (a web JSP page, a mobile API response, a PDF report) without duplication.
- **Team collaboration** — front-end developers/designers can work on Views (JSP/HTML/CSS) while backend developers work on Models and Controllers, with minimal overlap/conflict.
- **Testability** — business logic isolated in Models can be unit-tested without needing a running web server or browser.

### Real-world analogy

Think of a **restaurant**, again, but mapped differently this time:

- **Model** = The kitchen and the food itself — the actual substance, prepared according to recipes (business rules), completely independent of how it's presented.
- **View** = The plate and table presentation — how the food is visually presented to the customer. The same dish (Model) can be plated differently (View) for a fine-dining table vs. a takeaway box, without changing the recipe at all.
- **Controller** = The waiter — takes the customer's order (request), tells the kitchen what to prepare (invokes Model logic), and then decides how/where the result gets presented (chooses the View, hands off the prepared dish to be plated).

The customer (browser) never talks directly to the kitchen (Model) — they always go through the waiter (Controller).

### Internal working (in Servlet/JSP terms)

```
Component      | Java Web Technology            | Responsibility
----------------|--------------------------------|----------------------------------------
Model          | Plain Java classes, DAO, Service | Business logic, data (from DB via JDBC)
View           | JSP (with JSTL/EL)               | Presentation only — displays data
Controller     | Servlet                          | Receives request, invokes Model, forwards to View
```

### Architecture / Flow diagram

```
     Browser
        │  1. HTTP Request (e.g., POST /login)
        ▼
   ┌─────────────┐
   │  Controller  │   (Servlet — e.g., LoginServlet)
   │              │   - Reads request parameters
   │              │   - Calls Model for business logic
   └──────┬───────┘
          │ 2. Delegates to
          ▼
   ┌─────────────┐
   │    Model     │   (Service/DAO classes)
   │              │   - Validates credentials
   │              │   - Talks to Database (JDBC)
   └──────┬───────┘
          │ 3. Returns result (success/failure, data object)
          ▼
   ┌─────────────┐
   │  Controller  │   - Stores result as a request attribute
   │              │   - Uses RequestDispatcher.forward() to a View
   └──────┬───────┘
          │ 4. Forwards to
          ▼
   ┌─────────────┐
   │     View     │   (JSP — e.g., welcome.jsp or error.jsp)
   │              │   - Reads attribute, renders HTML
   └──────┬───────┘
          │ 5. HTML Response
          ▼
     Browser
```

### Advantages

1. Clean separation — each layer has exactly one responsibility.
2. Easier debugging — you know exactly where to look (is it a data problem = Model, a display problem = View, or a routing/flow problem = Controller?).
3. Parallel development — designers and backend developers don't block each other.
4. Foundation for Spring MVC — Spring's `@Controller`, `@Service`, and view templates (Thymeleaf/JSP) are the **exact same pattern**, just with dependency injection and annotations layered on top. Learning this now directly prepares you for Spring MVC later.

### Disadvantages

1. More files/classes for simple applications — can feel like overkill for a trivial one-page app.
2. Requires discipline — nothing *forces* you to follow MVC in raw Servlets/JSP; a lazy developer can still write HTML inside a Servlet (as we did in the medium example) or SQL inside a JSP. The pattern is a **convention**, not a compiler-enforced rule (this changes somewhat once you reach Spring, which structurally encourages it more strongly).

### When to use / When NOT to use

- **Use** MVC for any application beyond a trivial single-page demo — essentially always, in real projects.
- **Skip strict MVC** only for tiny throwaway test scripts or single-file proof-of-concept demos (like our very first `HelloServlet`, which was intentionally simple for teaching purposes).

### Best Practices

1. Controller (Servlet) should contain **no HTML** and ideally **no direct JDBC/SQL** — only orchestration logic.
2. Model classes should have **zero knowledge** of HTTP — no `HttpServletRequest`/`HttpServletResponse` imports inside Model/DAO classes. This keeps them reusable outside a web context.
3. Views (JSP) should contain **minimal Java logic** — ideally only JSTL tags and EL expressions (Module 2), never raw JDBC calls or business logic.
4. Data flows from Controller to View via **request attributes** (`request.setAttribute()`), read in JSP via **EL** (`${attributeName}`) — this is the standard data-passing mechanism, which we'll use starting right now in the advanced example.

---

Now let's apply this to our login example.

---

## STEP 7 (continued) — Advanced Example: MVC-Based Login using `RequestDispatcher.forward()`

### 7.10 — Updated Project Structure for This Example

```
webapp/
├── login.html                     (unchanged — the entry form)
└── WEB-INF/
    └── views/
        ├── welcome.jsp             (View — success page)
        └── loginError.jsp          (View — failure page)
```

Recall from Step 3: placing JSPs inside `WEB-INF/views/` means they are **not directly browser-accessible** by URL — they can only be reached via `RequestDispatcher.forward()` from a Servlet. This *enforces* proper MVC flow — a user cannot bypass the Controller and hit the View directly.

### 7.11 — The Controller: `LoginServlet.java` (Rewritten, MVC Style)

```java
package com.company.myapp.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Delegate validation to the Model layer — NOT done here in the Controller
        boolean isValid = LoginService.validateCredentials(username, password);

        String targetView;

        if (isValid) {
            request.setAttribute("username", username);
            targetView = "/WEB-INF/views/welcome.jsp";
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            targetView = "/WEB-INF/views/loginError.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetView);
        dispatcher.forward(request, response);
    }
}
```

### 7.12 — The Model: `LoginService.java`

**File location:** `src/main/java/com/company/myapp/service/LoginService.java`

```java
package com.company.myapp.service;

public class LoginService {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    public static boolean validateCredentials(String username, String password) {

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }

        return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
    }
}
```

Notice: **zero imports from `jakarta.servlet.*`** here. This class has no idea it's being used in a web application — it could just as easily be called from a command-line program or a unit test. That's the entire point of keeping Model logic HTTP-agnostic.

### 7.13 — The Views (Brief Preview — Full JSP Syntax Comes in Module 2)

**`WEB-INF/views/welcome.jsp`:**
```jsp
<html>
<body>
    <h2 style="color:green;">Login successful! Welcome, ${username}.</h2>
</body>
</html>
```

**`WEB-INF/views/loginError.jsp`:**
```jsp
<html>
<body>
    <h2 style="color:red;">${errorMessage}</h2>
    <a href="login.html">Try again</a>
</body>
</html>
```

Don't worry about the `${username}` syntax yet — that's **Expression Language (EL)**, formally taught in Module 2. For now, just understand conceptually: it reads the value we stored via `request.setAttribute("username", username)` in the Servlet.

---

### 7.14 — Explanation of the New Concepts

**`RequestDispatcher` (interface)**
- An object that lets you **forward** a request to another resource (another Servlet, or a JSP) **on the server side**, or **include** another resource's output within your own response. We used `forward()` here — full `forward()` vs `include()` comparison is a dedicated upcoming sub-topic in Module 1 (listed in your syllabus), so I'll go much deeper there. For now: `forward()` means *"hand off this exact request/response pair to another resource, and let that resource produce the final response."*

**`request.getRequestDispatcher(targetView)`**
- Obtains a `RequestDispatcher` object bound to the given path. The path here is **server-relative** (starts with `/`, resolved against the web application's root) — `/WEB-INF/views/welcome.jsp` refers to that exact file location, even though (as established) a browser could never request that URL directly.

**`dispatcher.forward(request, response)`**
- Performs the actual hand-off. Critically: **the browser's URL bar does NOT change** — the browser still shows `.../myapp/login`, even though the JSP is now producing the response. This is because forwarding happens entirely **server-side**; the browser never knows a "different resource" produced the response — from its perspective, it sent one request and got one response. (Contrast this with `response.sendRedirect()`, which **does** change the browser's URL, because it sends the browser a new request entirely — an important distinction we'll cover fully in the dedicated "Forward vs Include" and later "Redirect" topics.)

**`request.setAttribute("username", username)`**
- Stores a key-value pair in the **request scope** — meaning this data is available **only for the duration of this single request**, and accessible to any resource this request gets forwarded to (like our JSP). This is the primary mechanism for passing data from Controller to View in the MVC pattern. Full **Request/Session/Application scope** comparison is another dedicated upcoming sub-topic in your syllabus — I'm giving you the practical usage now and will formalize the complete theory later.

---

### 7.15 — Comparing All Three Versions

| Version | HTML location | Business logic location | Follows MVC? |
|---|---|---|---|
| Step 7.2 (`HelloServlet`) | Inside Servlet | None | No (intentionally simple) |
| Step 7.7 (medium `LoginServlet`) | Inside Servlet | Inside Servlet | No |
| Step 7.11–7.13 (advanced) | Inside JSP (View) | Inside `LoginService` (Model) | **Yes** |

