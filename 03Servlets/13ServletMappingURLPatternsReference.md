# TOPIC 4: Servlet Mapping, URL Patterns, and @WebServlet — Complete Reference

## CONCEPT

### Why this concept exists

A Servlet Container can host **dozens or hundreds** of Servlets in a single application. When a request arrives with a specific URL path, the container needs a **deterministic, unambiguous algorithm** to decide exactly which Servlet handles it. URL mapping is that algorithm's configuration layer — you declare *patterns*, and the container's internal **Mapper** component matches incoming request paths against them using a strict, well-defined precedence order (not just "first match wins" arbitrarily).

### What problem it solves

Without structured mapping rules, you'd need to hardcode routing logic yourself (parsing URLs, string-matching, branching) inside a single giant Servlet — exactly the kind of manual, error-prone, unmaintainable logic the Servlet specification was designed to eliminate. URL mapping externalizes this routing decision to the container, based on declarative configuration.

### Real-world analogy

Think of a large office building's directory board in the lobby. Instead of wandering every floor looking for "Accounts Department," you check the board, which lists **exact rules** ("Room 401 = Accounts," "Floor 5, any room = Sales," "any file with .pdf extension goes to the Print Room"). The receptionist (the container's Mapper) uses this board to route visitors (requests) to the correct destination, following a strict priority order when multiple rules could apply.

---

## URL Pattern Types — The Four Categories (Critical for Exams)

The Servlet specification defines **exactly four** categories of URL patterns, each with different matching behavior:

### 1. Exact Match

```java
@WebServlet("/login")
```
- Matches **only** the literal path `/login` — nothing more, nothing less.
- `http://localhost:8080/myapp/login` → matches.
- `http://localhost:8080/myapp/login/extra` → does **NOT** match this pattern.

### 2. Path Mapping (Wildcard Suffix)

```java
@WebServlet("/admin/*")
```
- Matches the literal prefix `/admin/` followed by **anything** (including nothing after the slash, or deeply nested paths).
- `http://localhost:8080/myapp/admin/` → matches.
- `http://localhost:8080/myapp/admin/users/5/edit` → matches.
- Very commonly used for **front-controller** style routing (a single Servlet handling many logical sub-routes, common in hand-rolled MVC frameworks before Spring MVC existed).

### 3. Extension Mapping (Wildcard Prefix)

```java
@WebServlet("*.do")
```
- Matches **any** URL ending in the literal suffix `.do`, regardless of path depth.
- `http://localhost:8080/myapp/login.do` → matches.
- `http://localhost:8080/myapp/admin/users.do` → matches.
- This is an **old-school convention** (common in early-2000s Java web apps, e.g., Struts framework used `.do` extensively) — you'll still encounter it in legacy enterprise codebases and sometimes in university exam questions, even though modern REST-style routing has largely replaced it.
- **Important restriction:** Extension mapping **cannot** be combined with a path prefix — `/admin/*.do` is **invalid** per the Servlet spec.

### 4. Default Servlet Mapping

```java
@WebServlet("/")
```
- A **single forward slash** is special: it overrides the container's own **built-in default Servlet** (the one responsible for serving static files like images, CSS, plain HTML when no other Servlet matches).
- Rarely used directly by application developers unless building a custom front-controller that should handle *literally every* unmatched request — this is conceptually very close to what Spring MVC's `DispatcherServlet` does (it's typically mapped to `/`, intercepting everything).

---

## Matching Precedence Order (The Part Almost Everyone Gets Wrong)

When multiple patterns *could* match the same incoming URL, the container follows this **exact, non-negotiable priority order** (from the Servlet specification):

```
1. Exact Match             (highest priority)
2. Longest Path Mapping match (most specific prefix wins)
3. Extension Mapping
4. Default Servlet ("/")    (lowest priority — fallback only)
```

**Worked example — this is a classic exam/interview trick question:**

Suppose these three Servlets are all registered:
```java
@WebServlet("/admin/users")       // ServletA — exact match
@WebServlet("/admin/*")           // ServletB — path mapping
@WebServlet("*.do")               // ServletC — extension mapping
```

| Incoming URL | Which Servlet handles it? | Why |
|---|---|---|
| `/admin/users` | **ServletA** | Exact match always wins over path/extension mapping |
| `/admin/reports` | **ServletB** | No exact match exists; path mapping `/admin/*` applies |
| `/admin/users.do` | **ServletA is NOT matched** — this exact string `/admin/users.do` isn't the exact pattern `/admin/users`. Between ServletB (`/admin/*`) and ServletC (`*.do`), **ServletB wins** | Path mapping takes priority over extension mapping per the precedence order above |
| `/report.do` | **ServletC** | No exact/path match applies; extension mapping is the only candidate |

---

## SYNTAX — Multiple URL Patterns on One Servlet

```java
@WebServlet(urlPatterns = {"/login", "/signin", "/authenticate"})
public class LoginServlet extends HttpServlet {
    // This single Servlet now handles THREE different exact-match URLs
}
```
- Useful when you want several different "friendly" URLs to route to identical logic — e.g., supporting both `/login` and `/signin` as aliases.

## Equivalent web.xml Form (Full Comparison)

```xml
<servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>com.company.myapp.servlet.LoginServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/login</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/signin</url-pattern>
</servlet-mapping>
```
- Notice: you can have **multiple `<servlet-mapping>` blocks** referencing the **same** `<servlet-name>`, achieving the identical effect as the annotation's array syntax. This reinforces something from Topic 1 Step 5.2: `<servlet-name>` is purely an internal cross-reference key — it's what lets `web.xml` link one `<servlet>` declaration to potentially several `<servlet-mapping>` entries.

---

## Annotation vs web.xml — When Does Each Actually Get Used, and Can They Conflict?

**Important nuance for real-world/legacy understanding:** If a Servlet class has **both** an `@WebServlet` annotation **and** a corresponding `<servlet>`/`<servlet-mapping>` entry in `web.xml` for the **same class**, the `web.xml` entry **takes precedence**, and the annotation's `urlPatterns` are effectively **overridden/ignored** for that mapping. This is intentional — it lets operations teams **override** a developer's hardcoded annotation mapping via configuration, without recompiling code, in situations where redeployment flexibility matters (common in larger enterprise environments with separate dev/ops teams).

---

## EXECUTION FLOW — The Mapper's Decision Process

Extending Step 8's Phase 2 trace with full mapping-resolution detail:

```
Incoming request: GET /myapp/admin/users.do
        │
        ▼
Tomcat's Context (matched from "/myapp" prefix) hands off to its Mapper
        │
        ▼
Mapper checks EXACT matches first
        │  → Is there a Servlet registered for exactly "/admin/users.do"? NO
        ▼
Mapper checks PATH mappings (longest prefix wins)
        │  → Is there a Servlet registered for "/admin/*"? YES → candidate found
        │  → (Mapper would also continue checking for even longer/more specific
        │     path patterns like "/admin/users/*" if they existed, since
        │     "longest match wins" among path mappings specifically)
        ▼
Since a path mapping candidate was found, EXTENSION mapping is NOT even considered
        │  (path mapping outranks extension mapping per precedence order)
        ▼
Selected Servlet: the one mapped to "/admin/*"
        ▼
Wrapper component invokes that Servlet's service() → doGet()
```

---

## COMMON ERRORS

**Error: Ambiguous mapping confusion — "why is the wrong Servlet handling my request?"**
- **Cause:** Developer assumes extension mapping (`*.do`) will win because it "looks more specific" for that particular URL, without realizing path mapping always outranks it per the precedence order.
- **Fix:** Explicitly trace through the four-tier precedence order (Exact → Path → Extension → Default) rather than guessing intuitively.

**Error: Invalid pattern combining prefix and extension**
```java
@WebServlet("/admin/*.do")  // INVALID
```
```
java.lang.IllegalArgumentException: Invalid <url-pattern> ... "/admin/*.do"
```
- **Cause:** Servlet spec explicitly forbids combining path-prefix wildcards with extension-suffix wildcards in a single pattern.
- **Fix:** Choose one style — either `/admin/*` (path) or `*.do` (extension) — never both combined into one pattern string.

**Error: Two Servlets registered with identical exact-match URL pattern**
- Already covered in Topic 2 (Common Errors, Error 4) — `IllegalArgumentException` at deployment, app fails to start entirely. Worth remembering this connects directly to this topic's mapping rules: **duplicate exact-match patterns are never allowed**, since the container has no tiebreaker rule for two identical exact matches.