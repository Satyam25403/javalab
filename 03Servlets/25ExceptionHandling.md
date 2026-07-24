# TOPIC 16: Exception Handling

## CONCEPT

### Why this concept exists

This topic **consolidates** all exception handling into a complete, deliberate strategy: how to catch, wrap, propagate, and gracefully display errors — the difference between an application that **crashes ugly** and one that **fails gracefully**, which is a genuine mark of production-quality code.

### What problem it solves

Left unhandled, exceptions in Servlets/JSPs result in Tomcat's default **500 Internal Server Error** page — which, in production mode, can either show a raw, security-revealing stack trace (bad — leaks internal package names, file paths, sometimes SQL fragments) or a blank, unhelpful generic error (also bad — gives the user no path forward).

### Real-world analogy

Think of a restaurant kitchen catching fire (an exception). A well-run restaurant has a **fire suppression protocol** (exception handling strategy) — staff calmly redirect customers to a safe waiting area with an apologetic note and a free drink (a friendly custom error page), while the actual fire gets handled and logged by staff behind the scenes (server-side logging) — the customer never needs to see the flames or understand the electrical fault that caused them (the raw stack trace).

---

## Two Complementary Strategies: Programmatic vs. Declarative

| Strategy | Mechanism | When to use |
|---|---|---|
| **Programmatic** | `try-catch` blocks directly in your Java code | When you can **recover** or handle the specific error meaningfully right where it occurs (e.g., retry, use a default value, show a specific validation message) |
| **Declarative** | `<error-page>` in `web.xml`, catching **uncaught** exceptions at the container level | As a **safety net** for anything you didn't (or couldn't reasonably) catch explicitly — the "last line of defense" |

**Best practice: use both, layered.** Catch what you can meaningfully handle close to the source; let everything else propagate up to your declarative `<error-page>` safety net.

---

## Programmatic Exception Handling — `try-catch` in Servlets

```java
@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String amountStr = request.getParameter("amount");

        try {
            double amount = Double.parseDouble(amountStr); // may throw NumberFormatException

            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }

            // Simulated business logic (real version in Module 3/4 uses JDBC)
            out.println("<h2>Transfer of $" + amount + " processed successfully.</h2>");

        } catch (NumberFormatException e) {
            out.println("<h2 style='color:red;'>Error: '" + amountStr + "' is not a valid number.</h2>");
            // Also log server-side for diagnostics — the user sees a friendly message,
            // but developers/ops can see the full detail in logs
            getServletContext().log("Invalid amount input received: " + amountStr, e);

        } catch (IllegalArgumentException e) {
            out.println("<h2 style='color:red;'>Error: " + e.getMessage() + "</h2>");
        }
    }
}
```

### New elements explained

**`getServletContext().log(String message, Throwable throwable)`**
- First introduced in Topic 6's method table, now used practically. This writes to the container's log file (`catalina.out` / Eclipse Console), including the full exception stack trace, **without** exposing any of it to the end user. This is the correct pattern: **user gets a clean message; the log gets the full diagnostic detail.**
- Compare this to `System.out.println()` — while both technically end up visible in Eclipse's console during development, `getServletContext().log()` is the **container-aware, spec-correct** logging mechanism, and in real production deployments (where `System.out` might not even be captured/rotated properly), using the container's logging facility is the professional standard.

---

## Declarative Exception Handling — `<error-page>` Revisited and Completed

Recall the syntax from Topic 5:
```xml
<error-page>
    <error-code>404</error-code>
    <location>/WEB-INF/views/error404.jsp</location>
</error-page>

<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/views/error500.jsp</location>
</error-page>
```

**Two distinct triggering mechanisms, worth being precise about:**

1. **`<error-code>`** — triggers when the response's status code matches, **specifically when set via `sendError()`** — recall: `setStatus()` alone does **not** trigger this, only `sendError()` does.
2. **`<exception-type>`** — triggers when an **uncaught exception** propagates all the way up out of your Servlet/JSP code, back to the container. The container catches it, matches its type (including subclasses — Java's normal polymorphic exception matching applies) against your declared `<exception-type>` entries, and forwards to the corresponding page.

**Matching specificity rule (parallels Java's own catch-block specificity rule above):** if you declare error pages for both `java.sql.SQLException` **and** `java.lang.Exception`, and a `SQLException` is thrown, the container matches the **most specific** declared type — `SQLException`'s dedicated error page — not the general `Exception` one.

```xml
<error-page>
    <exception-type>java.sql.SQLException</exception-type>
    <location>/WEB-INF/views/dbError.jsp</location>
</error-page>

<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/views/genericError.jsp</location>
</error-page>
```

---

## Accessing Exception Details Inside an Error Page

When the container forwards to an error page (either via `<error-code>` or `<exception-type>`), it automatically populates several **request attributes** — a container-level example of exactly the request-scope mechanism from Topic 7 — that your error page can read:

| Attribute Name | Type | Contains |
|---|---|---|
| `jakarta.servlet.error.status_code` | `Integer` | The HTTP status code |
| `jakarta.servlet.error.exception_type` | `Class` | The exception's class |
| `jakarta.servlet.error.message` | `String` | The exception's message |
| `jakarta.servlet.error.exception` | `Throwable` | The actual exception object itself |
| `jakarta.servlet.error.request_uri` | `String` | The original URL that caused the error |
| `jakarta.servlet.error.servlet_name` | `String` | Which Servlet was executing when the error occurred |

**Code example — a custom error page reading these attributes** (shown as a Servlet generating the error page's content for now, since JSP is discussed later):

```java
@WebServlet("/errorHandler")
public class ErrorHandlerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        Throwable exception = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Something went wrong.</h2>");
        out.println("<p>We encountered an error processing your request to: " + requestUri + "</p>");
        out.println("<p>Status Code: " + statusCode + "</p>");

        // IMPORTANT: only show raw exception details in a controlled DEVELOPMENT setting —
        // never expose this to end users in a real production deployment (security risk)
        if (exception != null) {
            getServletContext().log("Unhandled exception on " + requestUri, exception);
        }

        out.println("<p>Our team has been notified. Please try again later.</p>");
        out.println("</body></html>");
    }
}
```

**Critical production practice, worth flagging explicitly:** notice that we **log** the exception's full detail server-side via `getServletContext().log()`, but the **user-facing HTML** never prints the raw stack trace or exception message directly. This is the professional standard — **detailed diagnostics go to logs (for developers), friendly generic messages go to users**.

---

## Custom Exception Classes (Introducing Your Own Exception Types)

Real applications often define their own exception classes to represent **business-specific** error conditions clearly:

```java
package com.company.myapp.exception;

public class InsufficientBalanceException extends Exception {

    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

**Using it — checked exception pattern:**

```java
public class AccountService {
    public static void withdraw(double balance, double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance: requested " + amount + ", available " + balance);
        }
        // proceed with withdrawal logic
    }
}
```

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        AccountService.withdraw(500.0, 700.0);
    } catch (InsufficientBalanceException e) {
        request.setAttribute("errorMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/withdrawError.jsp").forward(request, response);
    }
}
```

**Why extend `Exception` (checked) rather than `RuntimeException` (unchecked) here?** This is a genuine design decision,("when do you use checked vs unchecked exceptions?"): `InsufficientBalanceException` represents an **expected, recoverable business scenario** — the caller (your Servlet) is **required by the compiler** to explicitly handle it (via `throws` or `try-catch`), which is appropriate because "insufficient balance" is a normal, anticipated outcome of a withdrawal attempt, not a programming bug. Contrast this with something like a `NullPointerException` (unchecked) — which typically indicates an actual **programming error**, not a foreseeable business condition.

---

## EXECUTION FLOW — Full Trace: Custom Exception → Declarative Error Page

```
Request: POST /myapp/withdraw  (amount exceeds balance)
        │
        ▼
WithdrawServlet.doPost() calls AccountService.withdraw()
        │
        ▼
AccountService throws InsufficientBalanceException
        │
        ▼
Caught by Servlet's own try-catch (PROGRAMMATIC handling)
   → forwards to /WEB-INF/views/withdrawError.jsp with a friendly message
        │
        ▼
[This exception was handled programmatically — it NEVER reaches
 the container's declarative <error-page> mechanism at all,
 because it never escaped the Servlet uncaught]


-------- CONTRAST: an exception NOT caught anywhere in your code --------

Request: POST /myapp/withdraw  (a genuine bug — e.g., NullPointerException
                                  somewhere deep in unrelated code)
        │
        ▼
Exception propagates up, uncaught, out of doPost()
        │
        ▼
Out of service(), out of the Servlet entirely
        │
        ▼
Container catches it (this is the DECLARATIVE safety net activating)
   → populates jakarta.servlet.error.* request attributes
   → matches against <error-page><exception-type> declarations
   → forwards to the matched error page (e.g., genericError.jsp)
        │
        ▼
User sees a friendly generic error page; full stack trace logged server-side
```

---

## COMMON ERRORS

**Error: Catch blocks in wrong order (compile error)**
```java
catch (IllegalArgumentException e) { ... }
catch (NumberFormatException e) { ... } // COMPILE ERROR: already caught above
```
- **Fix:** Order catch blocks from most specific subclass to most general superclass, exactly as explained above.

**Error: Swallowing exceptions silently (a genuinely bad practice to actively avoid)**
```java
try {
    riskyOperation();
} catch (Exception e) {
    // empty catch block — the error vanishes with NO trace at all
}
```
- **Why this is dangerous:** The operation fails, the user gets no feedback, and there's **no log entry anywhere** — this is one of the hardest categories of bugs to diagnose, because there's no evidence anything went wrong at all.
- **Fix:** Always **at minimum** log the exception (`getServletContext().log(msg, e)`), even if you choose not to show the user a detailed message.

**Error: Exposing stack traces to end users in what should be a production-mode deployment**
- **Cause:** No `<error-page>` configured for `java.lang.Exception`, so Tomcat's own default (often quite technical/revealing) error page is shown.
- **Fix:** Always configure at least one catch-all `<error-page>` for `java.lang.Exception` (Topic 5), ensuring users never see a raw container-generated stack trace.