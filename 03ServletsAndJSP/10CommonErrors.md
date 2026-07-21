# TOPIC 1 (continued): Common Errors

## STEP 9 — COMMON ERRORS (Compilation, Runtime, HTTP Status Errors, and Debugging)

This step is where theory meets real-world debugging. Every error here is something you **will** encounter while building the projects in Module 4 — understanding the *why* behind each one turns debugging from guesswork into a systematic process.

---

### 9.1 — Compilation Errors

These happen **before** deployment even matters — your `.java` file simply won't produce a `.class` file.

**Error 1: Package/folder mismatch**
```
error: class HelloServlet is public, should be declared in a file named HelloServlet.java
```
- **Cause:** Class name doesn't match filename, or the `package` declaration doesn't match the actual folder path (e.g., you wrote `package com.company.myapp.servlet;` but the file physically sits in `com/company/servlet/`).
- **Fix:** Ensure `package` statement exactly matches the folder hierarchy under `src/main/java`, and filename exactly matches the public class name (case-sensitive).

**Error 2: Missing import / unresolved symbol**
```
error: cannot find symbol
  symbol: class HttpServlet
```
- **Cause:** Missing `import jakarta.servlet.http.HttpServlet;`, OR — very commonly in your setup — the Servlet API dependency isn't resolved yet in `pom.xml` (Maven hasn't downloaded it, or you used the wrong artifact/version).
- **Fix:** Check `pom.xml` has the `jakarta.servlet-api` dependency with `provided` scope (Step 4.4). In Eclipse, right-click project → **Maven → Update Project** to force re-resolution if you just edited `pom.xml`.

**Error 3: `javax` vs `jakarta` mismatch (Step 4.1 come back to bite you)**
```
error: package javax.servlet.http does not exist
```
- **Cause:** You copied code from an old tutorial using `javax.servlet.*`, but your project uses Tomcat 10+/Jakarta EE 10, which only ships `jakarta.*` packages.
- **Fix:** Replace every `javax.servlet` import with `jakarta.servlet`. This is, by far, the single most common error beginners hit in 2024+ when following outdated tutorials — you now understand exactly why (Step 4.1), so you'll recognize it instantly instead of being confused by it.

---

### 9.2 — Deployment-Time / Startup Errors

**Error 4: Duplicate URL pattern**
```
SEVERE: A child container failed during start
java.lang.IllegalArgumentException: The urlPattern [/login] on servlet [LoginServlet] ... is already mapped
```
- **Cause:** Two Servlets (or a Servlet + a `web.xml` mapping) both claim the same `url-pattern`/`@WebServlet` value.
- **Fix:** Check for duplicate `@WebServlet("/login")` annotations, or conflicting `<servlet-mapping>` entries in `web.xml`, across your entire codebase. The container refuses to guess which one you meant.

**Error 5: `ClassNotFoundException` / `NoClassDefFoundError`**
```
java.lang.ClassNotFoundException: com.company.myapp.servlet.LoginServlet
```
- **Cause (most common in your Eclipse+Maven setup):** The compiled `.class` file isn't where Tomcat expects it (`WEB-INF/classes/`), usually because:
  1. The project wasn't rebuilt/republished after a code change (stale deployment).
  2. A Maven dependency required by the class (e.g., MySQL Connector, once we reach JDBC) wasn't marked with correct scope, so it's missing from the WAR at deploy time.
- **Fix:** In Eclipse, right-click the server → **Clean...** (forces a full republish), or right-click project → **Maven → Update Project**, then republish. Also verify the dependency's scope in `pom.xml` isn't accidentally `provided` when it actually needs to be bundled (recall: Servlet/JSP API = `provided`, but JSTL and MySQL Connector must NOT be `provided`, or they won't be packaged into the WAR at all).

**Distinguishing `ClassNotFoundException` vs `NoClassDefFoundError` (a real interview question):**

| | `ClassNotFoundException` | `NoClassDefFoundError` |
|---|---|---|
| Type | Checked Exception | Error (unchecked) |
| When it happens | You explicitly try to load a class by name at runtime (e.g., `Class.forName("...")`) and the classloader can't find it | A class **was available at compile-time** (code compiled fine) but is **missing at runtime** when the JVM actually tries to use it |
| Typical Servlet cause | Container tries to instantiate your Servlet class by name (from annotation/web.xml) but can't locate the `.class` file | Your code compiled successfully against a dependency (e.g., MySQL driver), but that JAR wasn't actually deployed into `WEB-INF/lib` at runtime |

---

### 9.3 — HTTP Status Code Errors (Runtime, After Successful Deployment)

**404 — Not Found**
- **Meaning:** The container received the request but found **no matching Servlet/resource** for that exact URL.
- **Common causes in your workflow:**
  1. Typo in URL (`/logni` instead of `/login`).
  2. `@WebServlet` URL pattern doesn't match what you're typing in the browser.
  3. Forgot the **context path** — you typed `http://localhost:8080/login` instead of `http://localhost:8080/myapp/login` (missing the app's context path, `/myapp`, derived from your project name).
  4. App failed to deploy at all (check Eclipse Console/`catalina.out` logs — Step 4.3 — for startup errors that silently prevented deployment).
- **How to debug:** Check Tomcat's `logs/catalina.out` (or Eclipse's Console view, which mirrors it) for deployment confirmation messages, and double/triple-check the exact URL pattern in your `@WebServlet` annotation against your browser URL.

**405 — Method Not Allowed**
- **Meaning:** The URL matched a Servlet correctly, but that Servlet **doesn't override** the `doXxx()` method corresponding to the HTTP method you used (exactly as explained in Step 6.4 — the default `HttpServlet` implementation of an un-overridden `doXxx()` returns 405).
- **Common cause:** You typed the `/login` URL directly into the browser's address bar (which always sends **GET**), but `LoginServlet` in our advanced example only overrides `doPost()`.
- **Fix:** Either submit via the actual form (which correctly sends POST), or deliberately override `doGet()` too if you want GET requests to be handled gracefully (as we briefly showed in the medium example, Step 7.7).

**500 — Internal Server Error**
- **Meaning:** The Servlet/JSP was found and invoked correctly, but an **unhandled exception** occurred during its execution (e.g., `NullPointerException`, `ArithmeticException`, uncaught `SQLException` once we reach JDBC).
- **How to debug:** This is the most important error to get comfortable reading. Tomcat's default 500 page (in development mode) shows a **full Java stack trace** — read it **top to bottom**, but the **most important line is usually the first "caused by" line closest to your own package name** (`com.company.myapp...`), since framework/container internals below that are rarely the actual bug source. This is a crucial practical debugging skill: don't panic at a long stack trace — scan for the first line mentioning **your own class**.

**Example stack trace and how to read it:**
```
HTTP Status 500 – Internal Server Error
java.lang.NullPointerException: Cannot invoke "String.equals(Object)" because "username" is null
    at com.company.myapp.service.LoginService.validateCredentials(LoginService.java:14)
    at com.company.myapp.servlet.LoginServlet.doPost(LoginServlet.java:22)
    at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)
    ...
```
- Read this as: *"Line 14 of `LoginService.java`, inside `validateCredentials()`, tried to call `.equals()` on a `null` `username` variable."* This tells you **exactly** where to look — no guessing needed. Notice how everything *below* your own package (the `jakarta.servlet.http.HttpServlet.service` line and beyond) is just the container's normal internal call chain — not where your bug is.

---

### 9.4 — `NullPointerException` — Deep Dive (Since It's the Single Most Common Runtime Error)

Given what you now know from Step 7.8, the most common Servlet-specific cause is:

```java
String username = request.getParameter("username"); // returns null if field missing/misspelled
if (username.equals("admin")) { ... }  // NPE if username is null
```

**Systematic prevention checklist (a genuinely useful habit for exams and real work):**
1. Always null-check the result of `request.getParameter()` before using it.
2. Prefer `CONSTANT.equals(variable)` over `variable.equals(CONSTANT)` when one side is guaranteed non-null (Step 7.8's defensive idiom).
3. Double-check HTML form field `name` attributes exactly match the `getParameter()` string — a mismatch (e.g., `name="user"` in HTML but `getParameter("username")` in code) silently returns `null`, with **no error at all** until you try to use it — this is a very sneaky, common bug.

---

### 9.5 — SQL Exceptions & Servlet/JSP Exceptions (Preview — Full Depth in Module 3)

Since we haven't reached JDBC yet, I'll only briefly flag these now and cover them exhaustively in Module 3:

- **`SQLException`** — checked exception, thrown by nearly every JDBC method; must always be caught or declared. Common causes: wrong credentials, DB server not running, table/column doesn't exist, connection not closed leading to pool exhaustion.
- **`ServletException`** — as established in Step 6.3, a general Servlet-layer exception; often you'll see it **wrapping** an underlying cause (e.g., a `ServletException` whose `getCause()` is actually a `SQLException` bubbled up from your DAO layer).
- **`JspException`** — thrown by custom JSP tag handlers or JSTL tags when something goes wrong during page rendering (covered in Module 2).

---

### 9.6 — Quick Reference: Error → Likely Cause → Fix

| Error | Most Likely Cause | Fix |
|---|---|---|
| Won't compile: "cannot find symbol" | Missing import or unresolved Maven dependency | Check import statement + `pom.xml` + Maven Update Project |
| `package javax.servlet does not exist` | Using old `javax.*` imports with Tomcat 10+ | Switch to `jakarta.*` |
| App won't deploy: duplicate URL pattern | Two Servlets mapped to same URL | Search codebase for duplicate `@WebServlet` values |
| `ClassNotFoundException` | Stale build / missing dependency scope | Clean + republish server; check `pom.xml` scopes |
| 404 | Wrong URL / missing context path / app didn't deploy | Check `catalina.out`, verify exact URL pattern |
| 405 | Wrong HTTP method for an un-overridden `doXxx()` | Submit via correct method, or override the missing method |
| 500 + NullPointerException | Usually `getParameter()` returning null, unchecked | Null-check all parameters; verify form field names match |
| 500 + stack trace | Any uncaught exception in your code | Read stack trace top-down, find first line in your own package |


