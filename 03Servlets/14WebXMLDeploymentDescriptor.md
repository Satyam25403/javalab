# TOPIC 5: web.xml and Deployment Descriptor — Complete Consolidated Reference

## CONCEPT

### Why this concept exists

We've already used `web.xml` pieces throughout Topics 1 and 4 (servlet declarations, mappings, error pages, session config). This topic consolidates everything into **one authoritative reference**, and fills in the gaps we haven't shown yet: `<filter>`, `<listener>` declarations, strict element ordering rules, and why a Deployment Descriptor matters even in an annotation-heavy, modern project.

**Core purpose, restated precisely:** The Deployment Descriptor is an **externalized configuration contract** between your application and the container. It exists so that **operational/configuration concerns** (URL routing, security constraints, session timeout, error handling) can be changed **without recompiling Java code** — a deployment engineer or system administrator can tweak `web.xml` and redeploy, without ever touching your `.java` source.

### Why it still matters in 2026, despite annotations

Even though `@WebServlet`, `@WebFilter`, `@WebListener` handle most day-to-day declarations now, `web.xml` remains necessary for:
1. **Overriding** annotation-based config without recompiling.
2. Declaring things that **have no annotation equivalent at all**: `<welcome-file-list>`, `<error-page>`, `<session-config>`, security constraints (`<security-constraint>`, out of scope for this course but exists), context parameters that need to be visible before any class loads.

---

## STRICT ELEMENT ORDERING — The Part That Silently Breaks Beginners' XML

This is a genuinely important, frequently-missed detail: **`web.xml` enforces a strict element ordering** according to its XML Schema Definition (XSD). Unlike most config files where you can list things in any order, **`web.xml` will fail to parse (or worse, silently misbehave) if elements appear in the wrong sequence.**

**The mandatory ordering (partial, most relevant elements for this course):**

```
1. <display-name>
2. <context-param>       (all context-params grouped together)
3. <filter>              (all filter declarations)
4. <filter-mapping>      (all filter mappings)
5. <listener>            (all listener declarations)
6. <servlet>             (all servlet declarations)
7. <servlet-mapping>     (all servlet mappings)
8. <session-config>
9. <welcome-file-list>
10. <error-page>
```

**Why does this matter practically?** If you place a `<filter>` block *after* a `<servlet>` block, most modern XML validators (and Tomcat's own parser) will throw a schema validation error at deployment time, something like:
```
cvc-complex-type.2.4.a: Invalid content was found starting with element 'filter'
```
This confuses beginners because the error message doesn't clearly say "wrong order" — it just complains about "invalid content." **Now that you know this rule explicitly, you'll recognize this exact error instantly**.

---

## COMPLETE web.xml — Every Element We've Learned, Correctly Ordered, Plus New Ones

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <display-name>MyWebApp</display-name>

    <!-- ============ 1. Context Parameters ============ -->
    <context-param>
        <param-name>appEnvironment</param-name>
        <param-value>development</param-value>
    </context-param>

    <!-- ============ 2. Filter Declarations ============ -->
    <filter>
        <filter-name>AuthFilter</filter-name>
        <filter-class>com.company.myapp.filter.AuthFilter</filter-class>
    </filter>

    <!-- ============ 3. Filter Mappings ============ -->
    <filter-mapping>
        <filter-name>AuthFilter</filter-name>
        <url-pattern>/admin/*</url-pattern>
    </filter-mapping>

    <!-- ============ 4. Listener Declarations ============ -->
    <listener>
        <listener-class>com.company.myapp.listener.AppContextListener</listener-class>
    </listener>

    <!-- ============ 5. Servlet Declarations ============ -->
    <servlet>
        <servlet-name>LoginServlet</servlet-name>
        <servlet-class>com.company.myapp.servlet.LoginServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- ============ 6. Servlet Mappings ============ -->
    <servlet-mapping>
        <servlet-name>LoginServlet</servlet-name>
        <url-pattern>/login</url-pattern>
    </servlet-mapping>

    <!-- ============ 7. Session Configuration ============ -->
    <session-config>
        <session-timeout>30</session-timeout>
    </session-config>

    <!-- ============ 8. Welcome Files ============ -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>

    <!-- ============ 9. Error Pages ============ -->
    <error-page>
        <error-code>404</error-code>
        <location>/WEB-INF/views/error404.jsp</location>
    </error-page>

    <error-page>
        <exception-type>java.lang.Exception</exception-type>
        <location>/WEB-INF/views/error500.jsp</location>
    </error-page>

</web-app>
```

### New elements explained (Filter and Listener declarations — not yet formally shown)

**`<filter>` block**
- `<filter-name>` — internal alias, same cross-reference concept as `<servlet-name>`.
- `<filter-class>` — fully qualified class name of a class implementing `jakarta.servlet.Filter` (the actual Filter mechanics — `doFilter()`, chain-of-responsibility behavior — are covered in depth in the dedicated **Filters** topic).

**`<filter-mapping>` block**
- Links a `<filter-name>` to a `<url-pattern>` (or, alternatively, a `<servlet-name>` — a filter can be scoped to a specific Servlet by name instead of a URL pattern, useful when you want a filter to apply regardless of what URL reaches a particular Servlet).
- **Order matters here too, in a different sense:** when multiple filters map to overlapping URL patterns, they execute in the **order their `<filter-mapping>` elements appear** in `web.xml` — this is actually the *one* place where declaration order has functional runtime significance beyond just schema validity.

**`<listener>` block**
- Simply declares a class implementing one of the Listener interfaces (`ServletContextListener`, `HttpSessionListener`, etc. — full explanation in the dedicated **Listeners** topic). No `<listener-name>` or mapping needed — listeners aren't mapped to URLs at all; they react to lifecycle *events* (app startup/shutdown, session creation/destruction), not requests.

---

## Multiple-Environment Configuration Pattern (Practical Industry Note)

A common real-world question: *"How do I have different `db.properties` or context-params for development vs. production without rewriting `web.xml` each time?"*

**Standard approaches (conceptual overview — we'll implement the properties-file version concretely in Module 3 when we reach JDBC):**

1. **Externalized `.properties` files** — instead of hardcoding values in `<context-param>`, store them in `src/main/resources/db.properties`, and load them programmatically inside a `ServletContextListener` at startup (Listeners topic), making them available via `ServletContext` attributes. Switching environments means swapping the properties file, not editing XML.
2. **Separate `context.xml` per environment** (Topic 1, Step 5.4) — since `context.xml` defines JNDI `DataSource` resources, different Tomcat installations (dev server vs. production server) can have different `context.xml` files pointing to different databases, while your `web.xml` and code remain identical across environments.
3. **Maven profiles** — Maven can filter/substitute different property values into config files based on a build profile (`-Pdev` vs `-Pprod`), producing environment-specific WAR builds from the same source.

---

## EXECUTION FLOW — When Does the Container Actually Read web.xml?

```
Tomcat starts up / app is deployed
        │
        ▼
Tomcat's deployment process locates WEB-INF/web.xml
        │
        ▼
XML parser validates the file against the Servlet 6.0 XSD schema
        │  → If ordering/schema violated: deployment FAILS here, app never starts
        ▼
Container builds an internal in-memory model of:
   - all <context-param> entries → becomes ServletContext init parameters
   - all <filter>/<filter-mapping> → registered in the filter chain
   - all <listener> → instantiated and contextInitialized() event fired
   - all <servlet>/<servlet-mapping> → registered in the Mapper (merged with @WebServlet-scanned annotations, following the override rule from Topic 4)
   - <session-config>, <welcome-file-list>, <error-page> → stored as app-wide settings
        │
        ▼
Any <servlet> with <load-on-startup> is instantiated and init() called NOW
        │
        ▼
Application is now fully deployed and ready to accept requests
```

**Important connection back to Listeners (upcoming topic):** notice that `<listener>` classes are instantiated and their startup event fired **before** servlets with `load-on-startup` — this ordering matters if your Servlet's `init()` logic depends on something a `ServletContextListener` is expected to set up first (like a database connection pool stored as a `ServletContext` attribute).

---

## COMMON ERRORS

**Error: Schema validation failure due to wrong element order**
```
cvc-complex-type.2.4.a: Invalid content was found starting with element 'servlet-mapping'.
One of '{"...":filter-mapping, ...}' is expected.
```
- **Cause:** Exactly the ordering issue explained above — e.g., a `<servlet-mapping>` placed before all `<filter-mapping>` entries.
- **Fix:** Reorder elements strictly following: display-name → context-param → filter → filter-mapping → listener → servlet → servlet-mapping → session-config → welcome-file-list → error-page.

**Error: Wrong schema version namespace mismatch**
```
error: cvc-elt.1: Cannot find the declaration of element 'web-app'
```
- **Cause:** Mixing an old `version="4.0"` (Java EE, `javax.*` namespace URLs like `http://xmlns.jcp.org/xml/ns/javaee`) with Tomcat 10+, or vice versa — a Jakarta EE 10 (`version="6.0"`) header used against an older Tomcat that doesn't recognize that schema.
- **Fix:** Always match your `web.xml` header exactly to the example given in Topic 1 Step 5.2 (`https://jakarta.ee/xml/ns/jakartaee`, `version="6.0"`) for Tomcat 10.1.x — never mix and match namespace versions from different-era tutorials.

**Error: Annotation and web.xml both declaring the same Servlet with conflicting mappings, causing confusing behavior**
- **Cause:** As discussed in Topic 4, `web.xml` overrides the annotation silently — a developer might edit the `@WebServlet` value, rebuild, and be confused why the URL still doesn't work, not realizing a stale/forgotten `web.xml` entry is still taking precedence.
- **Fix:** When debugging "my annotation change isn't taking effect," always check whether a corresponding `web.xml` entry exists for that same class and is silently overriding it.

