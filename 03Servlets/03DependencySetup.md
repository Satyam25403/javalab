# TOPIC 1 (continued): Dependency Setup

## STEP 4 — DEPENDENCY SETUP

This step is where a huge number of beginners get stuck — not because the concepts are hard, but because **version mismatches** between Tomcat, Servlet API, and Java cause cryptic errors. We'll go through this very carefully.

---

### 4.1 — Jakarta EE vs Java EE (Understand This FIRST — It Changes Everything)

This is a **critical piece of history** you must understand before touching any dependency, because it directly affects **which import statements you write**.

**The story:**
- Originally, this technology stack was called **Java EE (Java Enterprise Edition)**, owned and governed by **Oracle** (after Oracle acquired Sun Microsystems).
- All package imports were under the namespace: `javax.servlet.*`, `javax.servlet.http.*`, etc.
- In **2017**, Oracle **donated Java EE to the Eclipse Foundation** (an open-source organization) for governance reasons.
- However, Oracle **retained legal ownership of the `javax.*` trademark/namespace** and would not allow the Eclipse Foundation to continue using it.
- As a result, the Eclipse Foundation **rebranded the entire platform as Jakarta EE**, and — critically — starting from **Jakarta EE 9 (2020)** — **all package names changed from `javax.*` to `jakarta.*`**.

This means:

| | Java EE (older) | Jakarta EE 9+ (modern) |
|---|---|---|
| Import statement | `import javax.servlet.http.HttpServlet;` | `import jakarta.servlet.http.HttpServlet;` |
| Governing body | Oracle | Eclipse Foundation |
| Tomcat version | Tomcat 9 and earlier | Tomcat 10 and later |
| Servlet spec version | Servlet 4.0 and earlier | Servlet 5.0 and later |

**Why this matters practically:** If you download **Tomcat 10 or 11** (the current versions as of now) but write `import javax.servlet.http.HttpServlet;` in your code (copied from an old tutorial or Stack Overflow answer), your application will **fail to deploy**, often with confusing `ClassNotFoundException` or `NoClassDefFoundError` messages, because Tomcat 10+ **only understands `jakarta.*` packages internally**.

**For this course**, since you're learning fresh and industry is moving toward Jakarta EE (and Spring Boot 3.x also uses `jakarta.*` now), **we will use Jakarta EE (Tomcat 10/11, `jakarta.servlet.*` imports)** throughout. This is the correct, future-proof, industry-current choice. I'll explicitly flag it every time we write an import statement so this never becomes a silent point of confusion.

---

### 4.2 — Java Version Compatibility

| Component | Minimum Java Version |
|---|---|
| Tomcat 10.x | Java 11+ |
| Tomcat 11.x | Java 17+ |
| Jakarta EE 9/10 | Java 11+ |

**Recommendation for this course:** Use **Java 17 (LTS)** and **Tomcat 10.1.x**. Java 17 is a Long-Term Support release, widely adopted in industry, and fully compatible with modern Spring Boot 3.x (relevant for your future learning path).

---

### 4.3 — Apache Tomcat Setup

**What Tomcat actually is:** Tomcat is a **Servlet Container** (also loosely called an "application server," though technically it's a *web container*, not a full Java EE application server like WildFly or GlassFish, since it doesn't implement EJB, JMS, etc. out of the box). It implements the Servlet, JSP, and WebSocket specifications.

**Two ways to use Tomcat during development:**

**Option A: Standalone Tomcat (Manual Setup)**
1. Download Apache Tomcat 10.1.x from the official Apache Tomcat website (as a ZIP for Windows or tar.gz for Linux/Mac).
2. Extract it anywhere, e.g., `C:\tomcat10` or `/opt/tomcat10`.

**Apache Tomcat Directory Structure (you must understand this too):**

```
apache-tomcat-10.1.x/
│
├── bin/                    (Startup/shutdown scripts)
│   ├── startup.sh / startup.bat
│   ├── shutdown.sh / shutdown.bat
│   └── catalina.sh / catalina.bat   (Core script that actually runs Tomcat)
│
├── conf/                   (Tomcat's OWN configuration — different from your app's web.xml)
│   ├── server.xml          (Defines ports, connectors, hosts — Tomcat-level config)
│   ├── web.xml             (Default/global deployment descriptor, applies to ALL deployed apps)
│   ├── context.xml         (Default context configuration for all apps)
│   └── tomcat-users.xml    (Users/roles for Tomcat Manager app — authentication)
│
├── lib/                    (Tomcat's own core JAR files — Servlet API, JSP API implementation classes)
│
├── logs/                   (Runtime logs — catalina.out is the main log file, ESSENTIAL for debugging)
│
├── webapps/                (Deployment folder — THIS IS WHERE YOUR .war FILES GO)
│   ├── ROOT/                (Default app, served at http://localhost:8080/)
│   ├── manager/             (Built-in web-based deployment manager UI)
│   └── myapp/                (Your deployed application would appear here after deployment, either as myapp.war or an extracted myapp/ folder)
│
├── temp/                    (Temporary files used during runtime)
└── work/                    (VERY IMPORTANT — this is where Tomcat stores its compiled JSP-to-Servlet .java and .class files; we'll revisit this heavily in Module 2 JSP topics)
```

3. Start Tomcat: run `bin/startup.sh` (Linux/Mac) or `bin/startup.bat` (Windows).
4. Visit `http://localhost:8080` — if you see the Tomcat welcome page, it's running correctly.
5. Deploy your app by copying a `.war` file into `webapps/` — Tomcat **auto-detects and auto-deploys** it (extracts the WAR, creates a folder with the same name).

**Option B: IDE-Integrated Tomcat (Recommended for development)**
Most IDEs (Eclipse, IntelliJ IDEA) let you register a Tomcat installation and deploy/run/debug directly from the IDE, with hot-redeployment support. Internally, this still uses the same standalone Tomcat, just automated by the IDE.

I'll assume for hands-on exercises you'll set up either Eclipse (with "Dynamic Web Project" support) or IntelliJ + Maven. Let me know which IDE you're using once we reach actual coding, since project creation steps differ slightly.

---

### 4.4 — Maven Setup and `pom.xml` (Recommended Build Tool for This Course)

**Why Maven over manual JAR management?**
Manually downloading Servlet API JARs, JSTL JARs, MySQL Connector JARs and placing them correctly in `WEB-INF/lib/` is error-prone and doesn't scale. Maven automates **dependency resolution** (downloads the right JAR + its transitive dependencies from a central repository) and **build/packaging** (compiles code, structures the WAR file correctly).

**Complete example `pom.xml` for a Servlet + JSP + JDBC project:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.company</groupId>
    <artifactId>myapp</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!-- Servlet API - provided by Tomcat at runtime, NOT bundled in WAR -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSP API - also provided by Tomcat -->
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.1.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL - actual implementation, must be bundled -->
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>3.0.1</version>
        </dependency>

        <!-- MySQL Connector/J - JDBC driver for MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.4.0</version>
        </dependency>

    </dependencies>

    <build>
        <finalName>myapp</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
            </plugin>
        </plugins>
    </build>

</project>
```

**Line-by-line explanation of the important parts:**

- `<packaging>war</packaging>` — Tells Maven the final build output should be a `.war` file (Web Application Archive), not a `.jar`. This is **mandatory** for any Servlet-based web app deployed to Tomcat.

- `<scope>provided</scope>` (on Servlet API and JSP API) — This is one of the most important and most misunderstood tags for beginners. It means: *"This dependency is required to **compile** my code, but I do NOT want it packaged inside my final WAR file, because the container (Tomcat) already provides this JAR itself at runtime."* If you forget `provided` scope, you risk **classpath conflicts** — your WAR bundles its own Servlet API JAR, which can conflict with Tomcat's own internal Servlet API implementation, causing bizarre `LinkageError` or `ClassCastException` issues at runtime. **JSTL and MySQL Connector, by contrast, use default scope (`compile`)** because Tomcat does NOT provide these — your application must bundle them itself inside `WEB-INF/lib/`.

- `mysql-connector-j` — Note: the artifact name changed from the older `mysql-connector-java` to `mysql-connector-j` in recent versions (post MySQL 8.0.33-ish) due to Oracle's renaming. If you copy old tutorials, you may see the old artifact name — I'll always give you the current correct one.

- `maven-war-plugin` — The Maven plugin responsible for actually assembling the `.war` file, placing `WEB-INF/classes`, `WEB-INF/lib`, and everything from `src/main/webapp/` into the correct WAR structure automatically.

---

### 4.5 — Gradle (Brief Note)

Since Maven is the far more common build tool in traditional Java EE/Servlet projects (and still very dominant in enterprise Spring projects), we will use **Maven throughout this course**. Gradle is more common in Android and some modern Spring Boot projects — if you later need it, the same dependencies apply, just declared in `build.gradle` using Gradle's DSL syntax instead of XML. I won't spend further time on it unless you specifically request a Gradle-based path.

---

### 4.6 — Manual JAR Setup (Non-Maven, for understanding legacy projects)

In older-style projects (no Maven), you'd manually:
1. Download `jakarta.servlet-api-6.0.0.jar`, JSTL JAR, MySQL Connector JAR.
2. Place JSTL and MySQL Connector JARs directly into `WEB-INF/lib/`.
3. Add the Servlet API JAR to your IDE's **Build Path** (compile-time only, NOT copied into `WEB-INF/lib/`, since Tomcat provides it) — in Eclipse this is done via **"Deployment Assembly"** settings, where you explicitly control what gets packaged into the final WAR versus what's just needed for compilation.

**Build Path vs Classpath vs Deployment Assembly — the distinction:**

| Term | Meaning |
|---|---|
| **Classpath** | The complete set of locations (JARs/folders) the JVM searches when resolving classes, at compile-time or runtime |
| **Build Path** (IDE term, mainly Eclipse) | IDE configuration defining what's on the classpath *during development/compilation* inside the IDE |
| **Deployment Assembly** (Eclipse term) | IDE configuration defining what actually gets **packaged into the WAR** when you export/deploy — this can differ from Build Path (e.g., Servlet API is on Build Path for compiling, but excluded from Deployment Assembly since Tomcat provides it) |

This manual approach is what Maven automates for you — which is precisely why we use Maven.

---

### Summary Table — Dependency Setup Checklist

| Component | Version (this course) | Scope |
|---|---|---|
| Java (JDK) | 17 (LTS) | — |
| Apache Tomcat | 10.1.x | — |
| Jakarta Servlet API | 6.0.0 | `provided` |
| Jakarta JSP API | 3.1.1 | `provided` |
| JSTL (implementation) | 3.0.1 | `compile` (default) |
| MySQL Connector/J | 8.4.0 | `compile` (default) |
| Build tool | Maven | — |
| Package namespace | `jakarta.*` (NOT `javax.*`) | — |

