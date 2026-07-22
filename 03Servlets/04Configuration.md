# TOPIC 1 (continued): Configuration

## STEP 5 — CONFIGURATION

This step explains **every configuration file and every tag** you'll encounter in a Servlet-based project. This is dense — treat it as a reference you'll come back to repeatedly.

---

### 5.1 — Two Ways to Configure a Servlet: Annotations vs `web.xml`

Since **Servlet 3.0** (2009 onward), you have **two competing approaches**:

| Approach | How it works | When introduced |
|---|---|---|
| **Annotations** (`@WebServlet`) | Metadata written directly in the Java class | Servlet 3.0+ |
| **Deployment Descriptor** (`web.xml`) | Metadata written in a separate XML file | Original approach, still fully supported |

**Important nuance:** These are **not mutually exclusive** — a real project often uses both. Annotations are simpler for basic mappings; `web.xml` is still necessary for things annotations *cannot* express (like `<welcome-file-list>`, `<error-page>`, session timeout, and overriding annotation-based config without recompiling). You must know **both** deeply — university exams and legacy enterprise codebases lean heavily on `web.xml`, while modern quick development favors annotations.

---

### 5.2 — `web.xml` (The Deployment Descriptor)

**Location:** `WEB-INF/web.xml` — exact name, exact location, non-negotiable (mandated by the Servlet spec).

**What it is:** An XML file that describes to the Servlet Container **everything about your web application** — which servlets exist, how URLs map to them, filters, listeners, session config, error pages, welcome files, context parameters, security constraints.

**Complete example `web.xml` (Jakarta EE 10, Servlet 6.0):**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <display-name>MyWebApp</display-name>

    <!-- ============ Context Parameters ============ -->
    <context-param>
        <param-name>appEnvironment</param-name>
        <param-value>development</param-value>
    </context-param>

    <!-- ============ Servlet Declaration ============ -->
    <servlet>
        <servlet-name>HelloServlet</servlet-name>
        <servlet-class>com.company.myapp.servlet.HelloServlet</servlet-class>
        <init-param>
            <param-name>greeting</param-name>
            <param-value>Welcome!</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- ============ Servlet Mapping ============ -->
    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>

    <!-- ============ Welcome File List ============ -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>

    <!-- ============ Error Pages ============ -->
    <error-page>
        <error-code>404</error-code>
        <location>/WEB-INF/views/error404.jsp</location>
    </error-page>

    <error-page>
        <exception-type>java.lang.Exception</exception-type>
        <location>/WEB-INF/views/error500.jsp</location>
    </error-page>

    <!-- ============ Session Configuration ============ -->
    <session-config>
        <session-timeout>30</session-timeout> <!-- in minutes -->
    </session-config>

</web-app>
```

**Tag-by-tag explanation:**

**`<web-app>` (root element)**
- The outermost tag; everything is nested inside it.
- `xmlns`, `xsi:schemaLocation`, `version="6.0"` — these declare which **Servlet specification version** this descriptor conforms to. This is critical: `version="6.0"` corresponds to **Jakarta EE 10 / Servlet 6.0**, which uses `jakarta.ee` namespace URLs. If you copy an old tutorial's `web.xml` with `version="4.0"` and `javax.` namespace URLs, it's referring to the **old Java EE spec** and will conflict with Tomcat 10+.

**`<display-name>`**
- A human-readable name for the app, shown in some server admin tools. Purely descriptive, no functional effect.

**`<context-param>`**
- Defines **application-wide** configuration parameters, available to **every** Servlet, Filter, and JSP in the app via `ServletContext.getInitParameter("appEnvironment")`.
- Different from `<init-param>` (below), which is scoped to **one specific Servlet only**.
- Real use case: database URLs, feature flags, environment names — anything the whole app needs access to.

**`<servlet>` block**
- `<servlet-name>` — An **internal alias/nickname** you invent for this Servlet, used to link this declaration to its mapping below. It's *not* the class name and *not* the URL — purely an internal XML cross-reference key.
- `<servlet-class>` — The **fully qualified class name** (package + class), telling the container exactly which compiled class to instantiate.
- `<init-param>` — A configuration parameter scoped **only to this one Servlet**, retrieved inside the Servlet via `getServletConfig().getInitParameter("greeting")`. Different from `<context-param>` in scope.
- `<load-on-startup>1</load-on-startup>` — **Extremely important tag.** By default, a Servlet is lazily instantiated — the container waits until the **first matching request arrives** before calling `init()`. This tag **overrides that behavior**, forcing the container to instantiate the Servlet and call `init()` **immediately when the application starts up**, not on first request. The **integer value** (`1`, `2`, `3`...) determines **loading order** when multiple servlets specify this tag — lower numbers load first. This matters practically: if your Servlet's `init()` does something expensive (like establishing a connection pool), you often want it ready at startup rather than making the first unlucky user wait for that initialization delay.

**`<servlet-mapping>` block**
- Links a `<servlet-name>` (the internal alias) to a **URL pattern** — this is what actually determines which incoming request URL triggers this Servlet.
- `<url-pattern>/hello</url-pattern>` means: `http://localhost:8080/myapp/hello` will invoke this Servlet.
- URL pattern rules (very commonly tested in exams):
  - Exact match: `/hello` matches only `/hello`.
  - Path wildcard: `/admin/*` matches `/admin/anything/here`.
  - Extension wildcard: `*.do` matches any URL ending in `.do` (a very old-school but still-tested convention, e.g., `/login.do`).
  - Default servlet: `/` alone (single forward slash) overrides the container's built-in default servlet that normally serves static files.

**`<welcome-file-list>`**
- Defines which file to serve automatically when a user requests a **directory** without specifying a filename — e.g., visiting `http://localhost:8080/myapp/` (no filename) triggers the container to look for `index.jsp` first, then `index.html`, in the order listed, and serve whichever exists first.

**`<error-page>`**
- Maps specific **HTTP error codes** (like 404, 500) or **Java exception types** to custom JSP/HTML pages, instead of showing Tomcat's default ugly stack-trace error page to end users. Critical for professional applications — never let raw stack traces reach production users (security risk — reveals internal package structure/code).

**`<session-config>` → `<session-timeout>`**
- Defines, in **minutes**, how long an `HttpSession` remains valid without activity before the container automatically invalidates it. Default in most containers is 30 minutes if unspecified.

---

### 5.3 — Annotations (Modern Alternative to `web.xml` entries)

**`@WebServlet`** — equivalent to `<servlet>` + `<servlet-mapping>` combined:

```java
@WebServlet(
    name = "HelloServlet",
    urlPatterns = {"/hello"},
    loadOnStartup = 1,
    initParams = {
        @WebInitParam(name = "greeting", value = "Welcome!")
    }
)
public class HelloServlet extends HttpServlet {
    // ...
}
```

Every attribute here maps **directly** to a `web.xml` tag you just learned:
- `name` → `<servlet-name>`
- `urlPatterns` → `<url-pattern>` (note: **plural**, since a Servlet can map to multiple URL patterns simultaneously — something a single `<servlet-mapping>` block can also do with multiple `<url-pattern>` children)
- `loadOnStartup` → `<load-on-startup>`
- `initParams` → `<init-param>`

**`@WebFilter`** — declares a Filter (deep dive comes later in the "Filters" topic):
```java
@WebFilter("/admin/*")
public class AuthFilter implements Filter { ... }
```

**`@WebListener`** — declares a Listener (deep dive comes later in "Listeners" topic):
```java
@WebListener
public class AppContextListener implements ServletContextListener { ... }
```

---

### 5.4 — `context.xml` (Tomcat-Specific Context Configuration)

**Location options:** Either inside `META-INF/context.xml` (per-app, bundled in your WAR) or globally in Tomcat's own `conf/context.xml`.

**What it configures:** Tomcat-**specific** settings that aren't part of the standard Servlet spec — most commonly, **JNDI resources** like connection pool (`DataSource`) definitions:

```xml
<Context>
    <Resource name="jdbc/myDB"
              auth="Container"
              type="javax.sql.DataSource"
              maxTotal="20"
              maxIdle="10"
              maxWaitMillis="10000"
              username="root"
              password="yourpassword"
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/mydb"/>
</Context>
```

We'll use this heavily once we reach **Connection Pooling** in Module 3 (JDBC), rather than at this early stage — I'm showing it now just so you recognize the file when you see it.

---

### 5.5 — `server.xml` (Tomcat-Level, Not Application-Level)

**Location:** `<TOMCAT_HOME>/conf/server.xml` — this configures **Tomcat itself**, not any individual application. You'll rarely edit this as a beginner, but you should recognize its purpose:

```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

- Defines the **port** Tomcat listens on (default 8080), thread pool settings for the Connector, SSL configuration, virtual hosts, etc. Basic awareness is enough at this stage — we'll revisit only if you need to change the default port (e.g., if 8080 is already occupied by another process on your machine).

---

### 5.6 — MANIFEST.MF (Brief Mention)

Located at `META-INF/MANIFEST.MF` inside a WAR/JAR — contains metadata about the archive (version info, main class for executable JARs — not typically relevant for WAR-deployed web apps, but you'll see it exists when inspecting a WAR's internal structure). Eclipse generates this automatically; you almost never hand-edit it for Servlet projects.

---

### Summary Table — Configuration Files

| File | Location | Purpose | Who manages it |
|---|---|---|---|
| `web.xml` | `WEB-INF/web.xml` | Servlet/filter/listener declarations, mappings, error pages, session config | You (or auto-generated by Eclipse) |
| `context.xml` | `META-INF/context.xml` | Tomcat-specific resources (DataSource/JNDI) | You, when using connection pooling |
| `server.xml` | `<TOMCAT_HOME>/conf/` | Tomcat server-level config (ports, connectors) | Rarely edited by app developers |
| `MANIFEST.MF` | `META-INF/MANIFEST.MF` | Archive metadata | Auto-generated |
| `@WebServlet` etc. | Inside `.java` files | Modern annotation-based alternative to `web.xml` entries | You |


