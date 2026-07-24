# MODULE 1 — SPRING FUNDAMENTALS
## Topic 5: Bean, Bean Scope, and Bean Lifecycle (Complete Depth)

---

## STEP 1: CONCEPT

### Why This Topic Exists

You've been using the word "bean" for four topics now informally. It's time to formalize it completely — what it *precisely* is, how many copies of it exist in memory, how long it lives, and what hooks you can plug into its birth and death. This matters immensely in real applications: using the wrong scope is a classic source of subtle, hard-to-diagnose bugs (e.g., shared mutable state across users in a web app).

### Formal Definition

> A **bean** is an object that is instantiated, assembled, and managed by the Spring IoC container. The container decides *when* it's created, *how many instances* exist, *how it's wired*, and *when it's destroyed*.

Critically: **not** every Java object in a Spring application is a bean. Only objects the container has a `BeanDefinition` for (via `@Component`, `@Bean`, or XML) are beans. A `new Order()` you create manually inside a method is just a plain Java object — Spring has no idea it exists and manages nothing about it.

### Real-World Analogy

Think of an apartment building (the container):
- **Beans** are *registered tenants* — the building manager (container) knows their names, has keys to their unit, tracks move-in/move-out
- A **plain Java object** you `new` up yourself is like a guest visiting a tenant — the building manager has no record of them, no control over their comings and goings

### Bean Scope — Why It Exists

**The Problem:** When the container creates a bean, how many instances should exist? One shared instance for the whole application? A fresh instance every time someone asks for it? One per HTTP request? Different applications need different answers — this is what **scope** controls.

### The Six Bean Scopes

| Scope | Instances Created | Available In |
|---|---|---|
| `singleton` | **Exactly one** per Spring container (default) | Always |
| `prototype` | **A new instance every time** the bean is requested | Always |
| `request` | One instance per **HTTP request** | Web-aware contexts only |
| `session` | One instance per **HTTP session** (per logged-in user) | Web-aware contexts only |
| `application` | One instance per `ServletContext` (effectively singleton at the web-app level) | Web-aware contexts only |
| `websocket` | One instance per WebSocket session | Web-aware contexts only |

### Deep Dive: `singleton` (Default, Most Used)

```java
@Component // implicitly @Scope("singleton")
class ConfigurationHolder { }
```

**Critical clarification (frequently misunderstood):** Spring's "singleton" is **not** the classic Gang-of-Four Singleton pattern (which guarantees exactly one instance **per JVM**, usually via a private constructor + static instance). Spring's singleton means **one instance per Spring `ApplicationContainer`** — if you somehow spin up two containers in the same JVM, you'd get two separate "singleton" instances. In 99% of real apps there's only one container, so the distinction is academic — but it's a genuine, commonly-asked interview trap.

**Why default to singleton?** Most services (business logic classes) are **stateless** — they don't hold per-request mutable data, just orchestrate logic using their injected dependencies. Sharing one instance across the whole app is efficient (no repeated construction cost) and safe *precisely because* they hold no mutable state.

**⚠️ The #1 singleton bug:** If you give a singleton bean **mutable instance state**, that state is **shared across every single user and every single request** in your application — a serious concurrency/data-leak bug.

```java
@Component
class BadCounterService { // DANGEROUS
    private int count = 0; // mutable instance state on a SINGLETON

    public void increment() { count++; } // shared across ALL requests/users!
}
```
If two users hit this concurrently, they corrupt each other's data. This is a genuinely common real-world bug pattern — remember it.

### Deep Dive: `prototype`

```java
@Component
@Scope("prototype")
class ShoppingCart { // makes sense to be prototype — each user needs their OWN cart
    private List<String> items = new ArrayList<>();
}
```

**Key internal detail (frequently tested):** For `prototype` beans, the container's job **ends at creation and handing it over**. Unlike singleton, the container does **not** manage the full lifecycle — specifically, `@PreDestroy` is **never called** by the container for prototype beans. You are responsible for any cleanup yourself. This surprises many developers.

**When to use `prototype`:** Objects that hold **per-use, stateful data** — e.g., a `ShoppingCart`, a stateful builder, a per-task worker object.

### Deep Dive: Web Scopes (`request`, `session`, `application`)

These require a **web-aware `ApplicationContext`** (present automatically in any Spring Boot web application).

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestAuditLogger {
    private final String requestId = UUID.randomUUID().toString();
}
```

**Why `proxyMode` matters (important internal detail):** Imagine a `singleton`-scoped `OrderController` needs to inject a `request`-scoped `RequestAuditLogger`. The controller is created **once**, at startup — but a *new* `RequestAuditLogger` must exist **per HTTP request**, which doesn't exist yet at startup! Spring solves this with a **CGLIB proxy**: the controller actually holds a reference to a lightweight proxy object, and every method call on that proxy is transparently routed to **whichever real `RequestAuditLogger` instance belongs to the current request thread**, resolved at call-time. This is a concrete, practical example of AOP-style proxying solving a real architectural problem — not just an academic AOP demo.

### Bean Lifecycle — The Complete Picture

We touched this briefly in the IoC container topic (Steps 5–9 of that startup sequence). Now we formalize every hook available to you as a developer.

```
1. Bean instantiated (constructor called via reflection)
        │
2. Dependencies injected (constructor/setter/field DI)
        │
3. BeanPostProcessor.postProcessBeforeInitialization()  ← framework hook, runs on ALL beans
        │
4. @PostConstruct method invoked                          ← YOUR hook #1
        │
5. InitializingBean.afterPropertiesSet() invoked           ← YOUR hook #2 (interface-based)
        │
6. Custom init-method (if specified via @Bean(initMethod=...)) ← YOUR hook #3
        │
7. BeanPostProcessor.postProcessAfterInitialization()    ← framework hook (AOP proxies often wrap HERE)
        │
8. ***** BEAN IS FULLY READY AND IN USE *****
        │
   [ ... application runs, bean serves its purpose ... ]
        │
9. Container shutdown triggered (context.close() / JVM shutdown hook)
        │
10. @PreDestroy method invoked                            ← YOUR hook #4
        │
11. DisposableBean.destroy() invoked                        ← YOUR hook #5 (interface-based)
        │
12. Custom destroy-method (if specified) invoked            ← YOUR hook #6
        │
13. Bean removed from container, eligible for GC
```

**Important nuance:** Steps 9–13 (destruction callbacks) apply **only to `singleton`-scoped beans** managed fully by the container. As stated above, `prototype` beans do **not** get destruction callbacks — the container hands them off and washes its hands of them after creation.

---

## STEP 6: SYNTAX — Every Lifecycle Annotation/Interface

### `@PostConstruct`

- **Package:** `jakarta.annotation.PostConstruct` (was `javax.annotation.PostConstruct` pre–Spring Boot 3 / Jakarta migration — **note this explicitly, since it's a real, common version-migration gotcha**)
- **Applies to:** any method, no parameters, any visibility, must return `void`
- **Runs:** once, right after dependency injection completes
- **Typical use:** validate injected config, warm caches, open initial connections

```java
@Component
class DatabaseConnectionPool {
    @Value("${db.pool.size:10}")
    private int poolSize;

    @PostConstruct
    public void init() {
        System.out.println("Initializing pool with size: " + poolSize);
        // establish initial connections here
    }
}
```

### `@PreDestroy`

- **Package:** `jakarta.annotation.PreDestroy`
- **Runs:** once, right before the singleton bean is destroyed (container shutdown)
- **Typical use:** release resources — close connections, flush buffers, stop threads

```java
@Component
class DatabaseConnectionPool {
    @PreDestroy
    public void cleanup() {
        System.out.println("Closing all pooled connections...");
    }
}
```

### `InitializingBean` Interface (Older, Interface-Based Alternative)

```java
@Component
class LegacyStyleBean implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Bean fully configured — running setup logic");
    }
}
```
**Downside vs `@PostConstruct`:** Couples your class directly to a Spring interface (`InitializingBean`) — your class is no longer a "pure" POJO. `@PostConstruct` is preferred today since it's just an annotation, not an inherited contract.

### `DisposableBean` Interface

```java
@Component
class LegacyStyleBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("Cleaning up before shutdown");
    }
}
```

### `@Bean(initMethod=..., destroyMethod=...)` — For Third-Party Classes You Can't Annotate

Sometimes you're wiring a class from an external library — you can't add `@PostConstruct` to code you don't own. Java Config solves this:

```java
public class ThirdPartyCache { // imagine this is from an external jar, you cannot edit it
    public void warmUp() { System.out.println("Cache warming up..."); }
    public void shutdown() { System.out.println("Cache shutting down..."); }
}

@Configuration
class AppConfig {
    @Bean(initMethod = "warmUp", destroyMethod = "shutdown")
    public ThirdPartyCache thirdPartyCache() {
        return new ThirdPartyCache();
    }
}
```

### `@Scope` Annotation — Full Syntax

```java
@Component
@Scope("prototype") // string literal, OR use the constant:
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ShoppingCart { }
```

---

## STEP 7: CODE EXAMPLE — Full Lifecycle Demonstrated Together

```java
@Component
class ReportGenerator implements InitializingBean, DisposableBean {

    public ReportGenerator() {
        System.out.println("1. Constructor called");
    }

    @Autowired
    public void injectSomething(SomeDependency dep) {
        System.out.println("2. Setter injection (if any) happens around here");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("3. @PostConstruct executed");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. InitializingBean.afterPropertiesSet() executed");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("5. @PreDestroy executed");
    }

    @Override
    public void destroy() {
        System.out.println("6. DisposableBean.destroy() executed");
    }
}
```

**Actual console output order when this bean is created and the context is closed:**
```
1. Constructor called
2. Setter injection (if any) happens around here
3. @PostConstruct executed
4. InitializingBean.afterPropertiesSet() executed
... [ app runs ] ...
5. @PreDestroy executed
6. DisposableBean.destroy() executed
```

**Key takeaway to memorize:** `@PostConstruct` always fires **before** `InitializingBean.afterPropertiesSet()`, which fires **before** any custom `initMethod`. Symmetrically on shutdown: `@PreDestroy` → `DisposableBean.destroy()` → custom `destroyMethod`. This exact ordering is a legitimate, commonly-asked interview/viva detail.

---

## STEP 8: EXECUTION FLOW — Prototype vs Singleton, Side-by-Side

```java
@Component
class SingletonBean { }

@Component
@Scope("prototype")
class PrototypeBean { }

// In main:
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

SingletonBean s1 = ctx.getBean(SingletonBean.class);
SingletonBean s2 = ctx.getBean(SingletonBean.class);
System.out.println(s1 == s2); // TRUE — same instance

PrototypeBean p1 = ctx.getBean(PrototypeBean.class);
PrototypeBean p2 = ctx.getBean(PrototypeBean.class);
System.out.println(p1 == p2); // FALSE — different instances, new object each getBean() call
```

**Internally what's different:** for `SingletonBean`, the container creates it **once at `refresh()` time** (context startup) and stores it in the singleton cache — every `getBean()` call afterward just returns the cached reference. For `PrototypeBean`, the container stores only the **`BeanDefinition`** (the blueprint) — every single `getBean()` call triggers the **entire creation process again**: instantiation → DI → post-processors → init callbacks, fresh, every time.

---

## STEP 9: COMMON ERRORS

**Singleton bean with mutable shared state → data corruption/race conditions**
- Not a thrown exception — a **silent logical bug**, often the worst kind. Symptom: users seeing each other's data intermittently under load.
- Fix: make singleton beans stateless, or move mutable state to `prototype`/`request` scope, or use thread-safe structures deliberately if shared state is truly required.

**Injecting a `prototype` bean into a `singleton` bean naively**
```java
@Component
class SingletonService {
    @Autowired
    private PrototypeBean prototypeBean; // DANGER: injected ONCE at singleton creation!
}
```
- **Cause:** Since `SingletonService` is created once, its `@Autowired` field is populated **once** — meaning it holds the **same single `PrototypeBean` instance forever**, defeating the entire purpose of `prototype` scope!
- **Fix:** Use `ObjectFactory<PrototypeBean>`, `Provider<PrototypeBean>`, or a scoped proxy (`proxyMode = ScopedProxyMode.TARGET_CLASS`) to get a *fresh* prototype instance on each actual use.

**`@PreDestroy` never firing on a bean you expected**
- **Cause:** almost always — the bean is `prototype`-scoped. Reiterate: **prototype beans do not receive destruction callbacks** from the container.

---

## STEP 10: HANDS-ON PRACTICE

**5 Beginner Exercises:**
1. Create a `@Component` and print a message in a `@PostConstruct` method.
2. Add a `@PreDestroy` method and confirm it runs when you call `context.close()`.
3. Explicitly set `@Scope("prototype")` on a bean and prove (via `==` comparison) that two `getBean()` calls return different instances.
4. Explain in your own words why Spring's singleton is different from the Gang-of-Four Singleton pattern.
5. List the 6 bean scopes and state which ones require a web context.

**5 Intermediate Exercises:**
1. Build the "BadCounterService" example, then simulate 2 concurrent calls (using 2 threads) and observe the corrupted count.
2. Fix the above by converting it to `prototype` scope, or alternatively by removing the mutable state entirely — implement both fixes and compare.
3. Demonstrate the "prototype injected into singleton" bug directly — inject a prototype bean into a singleton via plain `@Autowired` and prove (via logging in the prototype's constructor) that it's only constructed once.
4. Fix exercise 3's bug using `ObjectFactory<T>` and confirm a new instance is created on each explicit `.getObject()` call.
5. Implement both `@PostConstruct`/`@PreDestroy` AND `InitializingBean`/`DisposableBean` on the same class and print the console log to confirm the exact firing order.

**5 Advanced Exercises:**
1. Create a `request`-scoped bean in a Spring Boot web app (we haven't hit web yet, but attempt this once we reach Module 3 — flag it as a "revisit" exercise) with `proxyMode = ScopedProxyMode.TARGET_CLASS`, and explain what the proxy is doing.
2. Explain, architecturally, why `proxyMode` is required when injecting a narrower-scoped bean into a wider-scoped one, but not the other way around.
3. Design a scenario where you'd deliberately want shared mutable state in a singleton, and explain how you'd make it thread-safe (hint: `AtomicInteger`, synchronized blocks, or `ConcurrentHashMap`).
4. Explain why third-party library classes typically need `@Bean(initMethod=..., destroyMethod=...)` instead of `@PostConstruct`/`@PreDestroy`.
5. If you manually called `ctx.getBean(PrototypeBean.class)` 1000 times in a loop, what happens to memory over time, and whose responsibility is cleanup?

**1 Mini Project:**
Build a `TaskExecutor` service (singleton) that hands out `Task` objects (prototype-scoped, each with a unique auto-generated ID via constructor logic). Prove via logging that each `Task` retrieved is a distinct instance, while `TaskExecutor` itself remains a single shared instance throughout.

**1 Enterprise Assignment:**
Design a `UserSessionManager` for a banking app: research and implement `session`-scoped beans (using a simple embedded Tomcat/Boot web app — we'll formalize the web setup in Module 3, so treat this as a forward-looking assignment to revisit) that store per-user session data safely, contrasted explicitly against a singleton anti-pattern implementation, with a written explanation of why the singleton version would be a serious production bug.

---

## STEP 11: VIVA PREPARATION

**15 Beginner:**
1. What is the default bean scope in Spring?
2. Name all 6 bean scopes.
3. Which scopes require a web-aware context?
4. What package does `@PostConstruct` come from in modern Spring Boot 3?
5. What package did `@PostConstruct` come from before Jakarta migration?
6. True/False: `@PreDestroy` is called for prototype beans.
7. What interface provides `afterPropertiesSet()`?
8. What interface provides `destroy()`?
9. Which annotation would you add to a `@Bean` method to specify a custom init method for a third-party class?
10. Is Spring's singleton the same as the Gang-of-Four Singleton pattern?
11. What does `==` comparison between two `getBean()` calls on a singleton bean return?
12. What does `==` comparison between two `getBean()` calls on a prototype bean return?
13. Name one legitimate use case for `prototype` scope.
14. What object type helps you fetch a *fresh* prototype bean from inside a singleton?
15. What does the container do with a prototype `BeanDefinition` versus a singleton one at startup?

**10 Intermediate:**
1. Explain the complete lifecycle order from constructor call to `@PreDestroy`, including framework `BeanPostProcessor` hooks.
2. Why doesn't the container call destruction callbacks for prototype beans — what does this imply about container responsibility?
3. Describe, step by step, why injecting a `prototype` bean into a `singleton` bean via plain `@Autowired` is a bug, using the container's creation timing.
4. Explain what a scoped proxy (`ScopedProxyMode.TARGET_CLASS`) actually does at runtime when a request-scoped bean is injected into a singleton controller.
5. Why is `@PostConstruct` generally preferred over implementing `InitializingBean`?
6. Why is `@Bean(initMethod=...)` necessary for third-party classes instead of just adding `@PostConstruct` to them?
7. What's the practical difference between `application` scope and `singleton` scope?
8. Give a concrete example of a bug that singleton scope with mutable state would cause in a multi-user web application.
9. In what order do `@PostConstruct` and `InitializingBean.afterPropertiesSet()` fire relative to each other, if a class uses both?
10. Why is thread safety a bigger concern for singleton-scoped beans than prototype-scoped beans?

**5 Scenario-Based:**
1. Your web app shows User B's shopping cart data to User A intermittently under load. Diagnose the likely root cause using bean scope knowledge.
2. You inject a `prototype` bean into a `singleton` service expecting a fresh instance each time you call a method, but logging shows the same instance every time. Explain why, and give two fixes.
3. A third-party JMS connection class needs to open a connection on startup and close it on shutdown, but you can't modify its source code. What's your approach?
4. Your `@PreDestroy` method never executes even though you added it correctly. What's the first scope-related question you should ask?
5. You need per-logged-in-user state that persists across multiple requests in a web app. Which scope fits, and why not singleton or prototype?

**5 Debugging Questions:**
1. Given two objects retrieved via `getBean()` returning `false` for `==`, what scope do you suspect, and how would you confirm it?
2. Your app has a memory leak that grows with traffic. Explain how the wrong bean scope choice for a heavy object could cause this.
3. You see duplicate resource-initialization log lines every time a certain bean is used — what scope misconfiguration would explain this?

**5 Coding Questions:**
1. Write a `@Component` demonstrating full lifecycle logging using both annotation-based and interface-based hooks.
2. Write a `prototype`-scoped bean and prove via code that two retrievals are different instances.
3. Fix a given snippet (I'll provide one) where a prototype bean is incorrectly injected into a singleton, using `ObjectFactory`.
4. Write `@Bean(initMethod=..., destroyMethod=...)` config for a hypothetical third-party `CacheManager` class.
5. Write code demonstrating a thread-safety bug in a singleton bean, then fix it using `AtomicInteger`.

---

## STEP 14: QUICK REVISION — Cheat Sheet

| Concept | One-Line Summary |
|---|---|
| Bean | Object whose lifecycle is fully managed by the Spring container |
| `singleton` (default) | One instance per container; must be stateless/thread-safe |
| `prototype` | New instance every `getBean()` call; **no destruction callbacks** |
| `request`/`session`/`application`/`websocket` | Web-only scopes, tied to HTTP request/session lifetimes |
| Lifecycle order (init) | Constructor → DI → `postProcessBeforeInitialization` → `@PostConstruct` → `afterPropertiesSet()` → custom init → `postProcessAfterInitialization` |
| Lifecycle order (destroy) | `@PreDestroy` → `destroy()` → custom destroy method (singleton only!) |
| Prototype-in-singleton bug | Injected only once at singleton creation — use `ObjectFactory`/scoped proxy for fresh instances |
| Spring singleton ≠ GoF Singleton | Spring's is "one per container," not "one per JVM" |

---

## Where We Are

✅ Topic 1: What is Spring
✅ Topic 2: Spring Modules & Architecture
✅ Topic 3: IoC — Concept + Internals
✅ Topic 4: Dependency Injection — Types, Syntax, Code
✅ Topic 5: **Bean, Bean Scope, Bean Lifecycle**

⏭️ **Next: `BeanFactory` vs `ApplicationContext`** — a focused, comparison-driven deep dive (you've seen both mentioned repeatedly; now we nail every distinction with code and a comparison table), followed by **Configuration approaches: XML vs Java vs Annotation** — including why XML is now legacy and Java/annotation config won.

This topic had a genuinely dangerous real-world bug pattern in it (singleton + mutable state). **Before continuing — can you explain, in your own words, exactly why a `prototype` bean injected via plain `@Autowired` into a `singleton` bean only ever gives you one instance, even though prototype scope is supposed to create a new instance every time?** This is one of the most-asked Spring interview questions — make sure it's solid. Say "next" when ready, or ask me to clarify.