# MODULE 2: JSP

# TOPIC 20: JSP Introduction, Architecture, and Lifecycle

## CONCEPT

### Why this concept exists

Throughout Module 1, we deliberately used raw `out.println("<html>...")` inside Servlets (Topic 1), flagging it explicitly as bad practice each time. We also used `${username}` in JSP files (Topic 1's MVC example) without ever explaining that syntax. Now we resolve both: **JSP (JavaServer Pages) exists specifically to solve the "HTML mixed into Java strings" problem**, by inverting the relationship — instead of Java code containing HTML strings, **JSP files contain HTML, with small, targeted pieces of Java/dynamic logic embedded inside it.**

### What problem it solves

Compare these two approaches to generating the *exact same* HTML output:

**Servlet approach (Topic 1's `HelloServlet`):**
```java
out.println("<html><body><h1>Welcome, " + username + "!</h1></body></html>");
```
Every piece of HTML must be escaped into Java string literals — no syntax highlighting for HTML in most editors, error-prone string concatenation, and a **web designer with no Java knowledge cannot touch this file at all** without risking breaking the Java code.

**JSP approach:**
```jsp
<html><body><h1>Welcome, ${username}!</h1></body></html>
```
This **is** HTML — a designer can open this file, see exactly what they're used to seeing, and edit styling/structure freely, with `${username}` as the only "special" piece — clearly demarcated, easy to leave alone if you don't understand it yet.

**JSP is fundamentally about flipping the ratio: mostly markup, small amounts of embedded logic — rather than mostly code, with markup embedded as strings.**

### Real-world analogy

Think of a **mail-merge template** in a word processor — you write a normal-looking letter ("Dear [Name], your account balance is [Balance]"), with specific placeholders marked clearly, rather than writing a computer program that assembles the entire letter character-by-character from code. The letter's overall structure and formatting remain fully visible and editable to anyone, with only the placeholders needing "programming" understanding.

---

## JSP Architecture — The Big Reveal (Topic 1, Step 8's Foreshadowing, Now Fully Explained)

### The core secret: **A JSP file is NOT executed directly. It is translated into a Servlet, then compiled, then run as a Servlet — automatically, by the container.**

This was first flagged in Topic 1, Step 8, Phase 7 ("JSPs are, under the hood, just Servlets"). Now we explain the **complete mechanism**.

```
┌──────────────────────────────────────────────────────────────┐
│  1. TRANSLATION PHASE                                          │
│     welcome.jsp  ──(Jasper, Tomcat's JSP engine)──►  welcome_jsp.java │
│     (Your HTML+JSP tags are converted into equivalent           │
│      Java code that PRINTS that exact HTML via out.println())   │
├──────────────────────────────────────────────────────────────┤
│  2. COMPILATION PHASE                                            │
│     welcome_jsp.java  ──(javac, invoked internally)──►  welcome_jsp.class │
├──────────────────────────────────────────────────────────────┤
│  3. LOADING & INSTANTIATION                                       │
│     welcome_jsp.class is loaded, instantiated — EXACTLY like     │
│     any Servlet (Topic 1, Step 2's class loading/reflection)      │
├──────────────────────────────────────────────────────────────┤
│  4. EXECUTION                                                    │
│     The generated Servlet's service() method runs, producing     │
│     the exact HTML output your JSP was meant to produce           │
└──────────────────────────────────────────────────────────────┘
```

### Concretely — what does the generated Servlet actually look like?

Given this simple JSP:
```jsp
<html>
<body>
    <h2>Welcome, ${username}!</h2>
</body>
</html>
```

**Jasper generates something conceptually equivalent to:**
```java
public class welcome_jsp extends HttpJspBase implements HttpJspPage {

    @Override
    public void _jspService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        out.write("<html>\n<body>\n    <h2>Welcome, ");
        out.write(String.valueOf(request.getAttribute("username")));  // simplified EL evaluation
        out.write("!</h2>\n</body>\n</html>");
    }
}
```

**This is not an exact reproduction of Jasper's real generated code** (which is considerably more complex, handling buffering, exception pages, and more), but it captures the **essential truth**: **your HTML becomes string literals passed to `out.write()`, and your dynamic JSP expressions become Java expressions evaluated and inserted into that output stream.** Every JSP file, no matter how much HTML it contains, ultimately becomes exactly the kind of `out.println()`-based Servlet you wrote by hand back in Topic 1 — **JSP doesn't eliminate that mechanism, it just generates it for you automatically**, letting you write in a more natural, HTML-first syntax instead.

### `HttpJspPage` / `HttpJspBase` — completing the inheritance hierarchy

Recall Topic 1, Step 6.1's hierarchy: `Servlet` → `GenericServlet` → `HttpServlet`. JSP extends this **one level further**:

```
        jakarta.servlet.Servlet
                    │
                    ▼
        jakarta.servlet.GenericServlet
                    │
                    ▼
        jakarta.servlet.http.HttpServlet
                    │
                    ▼
        jakarta.servlet.jsp.HttpJspBase   (Tomcat/Jasper-specific)
                    │
                    ▼
        YOUR GENERATED welcome_jsp CLASS
```

**This is precisely why JSP has full access to `HttpServletRequest`, `HttpServletResponse`, sessions, everything you learned in Module 1** — it's still fundamentally a Servlet at the bottom of an inheritance chain; JSP adds a translation layer on top, not a different execution model underneath.

---

## JSP Lifecycle — Paralleling the Servlet Lifecycle (Topic 2), With One Extra Phase

```
┌─────────────────────────────────────────────────────────────┐
│  PHASE 0: TRANSLATION (JSP-specific, happens ONCE,            │
│           only on first request OR when the .jsp file changes)│
│  ─────────────────────────────────────────────────────       │
│  1. Jasper checks: does a compiled, up-to-date .class          │
│     already exist for this .jsp in Tomcat's work/ directory     │
│     (Topic 1, Step 4.3)? If yes AND unchanged, SKIP to Phase 1. │
│  2. If not (first time, or .jsp file has been edited since):    │
│     translate .jsp → generated .java source                     │
│  3. Compile generated .java → .class                             │
├─────────────────────────────────────────────────────────────┤
│  PHASE 1: INITIALIZATION (once per generated Servlet instance)│
│  ─────────────────────────────────────────────────────       │
│  4. Class loaded, instantiated (Topic 1, Step 2 mechanics)       │
│  5. jspInit() called — EXACTLY parallel to Servlet's init()       │
├─────────────────────────────────────────────────────────────┤
│  PHASE 2: EXECUTION (many times — once per request)             │
│  ─────────────────────────────────────────────────────       │
│  6. _jspService() called — EXACTLY parallel to Servlet's           │
│     service(), but you NEVER override this yourself (it's           │
│     entirely auto-generated from your JSP's content)               │
├─────────────────────────────────────────────────────────────┤
│  PHASE 3: DESTRUCTION (once)                                     │
│  ─────────────────────────────────────────────────────       │
│  7. jspDestroy() called — EXACTLY parallel to Servlet's destroy()│
└─────────────────────────────────────────────────────────────┘
```

**Critical, exam-favorite distinction:** `_jspService()` is **auto-generated entirely from your JSP's HTML/tags/scriptlets** — unlike a Servlet's `service()` (which you never override, but which dispatches to `doGet()`/`doPost()` that **you do write**), a JSP's `_jspService()` is **not something you write pieces of at all** — the entirety of your JSP file's content **becomes** this method's body, generated by Jasper. This is a subtle but genuinely important distinction: in Servlets, you write `doGet()`; in JSP, you don't write an equivalent method by hand at all — you write the *page*, and the container derives the entire method from it.

**`jspInit()` and `jspDestroy()` — do you ever actually use these?** Extremely rarely, in practice — since JSP is meant to focus on presentation, one-time setup/teardown logic more naturally belongs in a Servlet's `init()`/`destroy()` or a `ServletContextListener` (Topic 15). You *can* override `jspInit()`/`jspDestroy()` via a JSP declaration (covered in the upcoming Declarations topic), but it's uncommon in well-architected applications following proper MVC (Topic 1) — flagging their existence now for completeness and exam purposes, not as something you'll use often.

---

## Why Translation Happens Only Once (Performance Implication)

This directly parallels Topic 1's core motivating insight for Servlets themselves (avoiding CGI's per-request process creation): **JSP translation and compilation is expensive** (parsing, generating Java source, invoking `javac`), so Tomcat is careful to **only redo this when genuinely necessary**:

```
Incoming request for welcome.jsp
        │
        ▼
Does Tomcat's work/ directory already contain a compiled
welcome_jsp.class for this JSP?
        │
   ┌────┴────┐
   NO         YES
   │           │
   ▼           ▼
Translate    Is the .jsp file's last-modified timestamp NEWER
+ Compile    than the compiled .class file's timestamp?
   │           │
   │      ┌────┴────┐
   │      NO         YES (you edited the .jsp since last compile)
   │      │           │
   │      ▼           ▼
   │   Reuse the    Re-translate + Re-compile
   │   existing       (this is WHY editing a .jsp file and
   │   .class          refreshing the browser "just works" during
   │                    development, without restarting Tomcat —
   │                    unlike a .java Servlet change, which requires
   │                    a full rebuild/redeploy, Topic 2)
   └──────┬─────┘
          ▼
   Instantiate (if not already) → jspInit() (if first time)
          ▼
   _jspService() called for THIS request
```

**This explains a genuinely useful practical difference you'll experience firsthand:** editing a `.jsp` file and refreshing your browser often shows changes **immediately** (Tomcat detects the timestamp change and silently re-translates/recompiles behind the scenes) — whereas editing a `.java` Servlet requires Eclipse to rebuild and republish the whole application (Topic 2's lifecycle discussion) before changes appear. This is a direct, practical consequence of everything just explained about JSP's translation mechanism.

---

## First Complete JSP Example (Minimal, Building Toward Full Syntax Next Topic)

**File location:** `src/main/webapp/greeting.jsp`

```jsp
<html>
<head><title>Greeting Page</title></head>
<body>
    <h1>Hello, World! This is my first JSP page.</h1>
    <p>Current server time: <%= new java.util.Date() %></p>
</body>
</html>
```

**Access via:** `http://localhost:8080/myapp/greeting.jsp` — **directly**, no Servlet mapping needed at all. This is an important structural difference from everything in Module 1: **JSP files placed directly under `webapp/` (not inside `WEB-INF/`) are automatically, directly URL-addressable by their filename** — Tomcat's Jasper engine handles this mapping implicitly, without you writing any `@WebServlet` annotation or `web.xml` entry.

**`<%= new java.util.Date() %>`** — this is a **JSP Expression** (full syntax formally covered in the next topic, "JSP Syntax / Directives / Scriptlet / Declaration / Expression") — for now, just observe: this embeds a small piece of **actual Java code** directly inside otherwise-plain HTML, and its **result** gets printed into the output at exactly that spot. This is the direct answer to "how does dynamic content get into an HTML-first file?"

---

## EXECUTION FLOW — First Request to a New JSP

```
Browser: GET /myapp/greeting.jsp  (FIRST ever request to this file since deployment)
        │
        ▼
Tomcat's Context recognizes this maps to a JSP resource (not a compiled Servlet class)
        │
        ▼
Jasper checks work/ directory → no compiled version exists yet
        │
        ▼
TRANSLATION: greeting.jsp → greeting_jsp.java generated
   (every line of HTML becomes an out.write(...) call;
    <%= new java.util.Date() %> becomes out.print(new java.util.Date()))
        │
        ▼
COMPILATION: greeting_jsp.java → greeting_jsp.class
        │
        ▼
Class loaded, instantiated, jspInit() called (empty, unless overridden)
        │
        ▼
_jspService() called → executes all those out.write()/out.print() calls in sequence
        │
        ▼
Complete HTML response (with the actual current date/time inserted) sent to browser
        │
        ▼
[Compiled .class file REMAINS in work/ directory for all FUTURE requests —
 next request skips translation/compilation entirely, going straight to
 _jspService() on the already-loaded instance]
```

---

## COMMON ERRORS

**Error: JSP changes not reflecting in the browser**
- **Cause (rare, but real):** Some Tomcat configurations or IDE deployment modes disable automatic timestamp-checking/recompilation in production-oriented settings.
- **Fix:** For development, ensure your Tomcat/Eclipse setup allows JSP reloading (default behavior in standard development configurations); if changes genuinely don't appear, a full server restart/clean redeploy (Topic 2's troubleshooting) resolves it as a fallback.

**Error: Confusing compilation errors — errors reported against a `_jsp.java` file you never wrote**
```
An error occurred at line: 15 in the generated java file
```
- **Cause:** Since your `.jsp` is translated into Java source before compilation, a JSP-level syntax error surfaces as a **compilation error in the generated file**, which can be confusing since you never directly wrote that file.
- **Fix:** The error message typically **also** references the original `.jsp` file and line number — always check for that, and count on the fact that most JSP-level mistakes (like a typo in an expression) map fairly directly back to an identifiable spot in your original `.jsp` source, even though the raw error trace points at the generated file.

**Error: `500` error the very first time a JSP is accessed after deployment, but working fine afterward**
- **Cause:** The **very first** request to any given JSP pays the "translation + compilation" cost (as traced above) — if something is misconfigured (missing JSTL library once we reach that topic, for instance), this first-request compilation can fail, while a Servlet's equivalent problem would have surfaced at **application startup** instead (if `load-on-startup` was used) or been caught at Maven build time. JSP compilation errors are inherently a bit more "lazy" in when they surface.
- **Fix:** Test every JSP page at least once after any deployment, specifically checking for this class of error, rather than assuming "the app started successfully" means every JSP is also error-free.

---

That completes **JSP Introduction, Architecture, and Lifecycle** — the foundational reveal that resolves Module 1's biggest deferred question ("how does `${username}` work, and why were we told not to write HTML in Servlets?"). Every JSP concept from here forward builds directly on this translation-to-Servlet mental model.

**Next up per your module order:** **JSP Syntax — Directives, Scriptlet, Declaration, Expression** — the complete catalogue of every distinct JSP tag type, what Java code each one generates, and precisely when to use which.

Say **"Next"** to continue.