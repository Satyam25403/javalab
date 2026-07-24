# MODULE 1 — SPRING FUNDAMENTALS
## Topic 4: Dependency Injection (DI) — Full Depth with Code

We now move from *principle* (IoC) to *mechanism* (DI). This is where theory becomes syntax you'll type every day.

---

## STEP 1: CONCEPT

### Why This Exists

IoC told you *what* should happen: control of object creation is inverted to a container. **Dependency Injection is the specific technique Spring uses to *deliver* dependencies into your objects.** There are, historically, other ways to achieve IoC (e.g., the **Service Locator pattern**, where a class actively *asks* a registry for what it needs). Spring chose DI as its primary mechanism because it keeps classes even more passive and decoupled than Service Locator does.

### DI vs Service Locator (Important Distinction, Common Interview Question)

```java
// SERVICE LOCATOR (class actively fetches its dependency)
class OrderService {
    private PaymentGateway gateway = ServiceLocator.getService(PaymentGateway.class);
    // Problem: OrderService still KNOWS about the locator/registry — still coupled to a lookup mechanism
}

// DEPENDENCY INJECTION (dependency is handed to the class — it does nothing to fetch it)
class OrderService {
    private final PaymentGateway gateway;
    OrderService(PaymentGateway gateway) { this.gateway = gateway; } // purely passive
}
```

With DI, `OrderService` has **zero knowledge** that Spring, or any container, even exists. It's a pure POJO. This is a strictly cleaner separation, which is why DI is Spring's core mechanism.

### Real-World Analogy

- **Service Locator** = You walk into the restaurant's storage room yourself and grab ingredients off the shelf. You still need to know *where the storage room is* and *how to find things in it*.
- **Dependency Injection** = A waiter brings the ingredients to your station, already prepared, without you asking or knowing where they came from. You just cook.

### Types of Dependency Injection in Spring

There are **three** injection mechanisms:

#### 1. Constructor Injection (Recommended, Default Best Practice)

```java
@Component
class OrderService {
    private final PaymentGateway paymentGateway; // can be `final` — immutable!

    @Autowired // optional since Spring 4.3 if there's only ONE constructor
    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

**Why this is preferred:**
- Fields can be `final` → **immutability**, thread-safety by design
- **Mandatory dependencies are explicit** — you cannot construct an incomplete object; if `PaymentGateway` isn't available, the object literally cannot exist
- **Best testability** — in a plain unit test (no Spring at all), you can do `new OrderService(mockGateway)` directly
- Prevents circular dependencies from silently compiling — they fail fast and loud at startup (a feature, not a bug, as we discussed last topic)

#### 2. Setter Injection

```java
@Component
class OrderService {
    private PaymentGateway paymentGateway; // cannot be final

    @Autowired
    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

**When to use:** For **optional** dependencies — ones your class can function without, or that might be reconfigured after construction. Rare in modern practice, but still valid for optional collaborators.

#### 3. Field Injection (Discouraged, But You'll See It Everywhere in Legacy Code)

```java
@Component
class OrderService {
    @Autowired
    private PaymentGateway paymentGateway; // injected directly via reflection on the field
}
```

**Why it's discouraged (memorize these reasons — very common interview question):**
- Field **cannot be `final`** → no immutability
- **Cannot be unit tested without Spring or reflection tricks** — there's no constructor to pass a mock into; you'd need `ReflectionTestUtils` or similar hacks
- **Hides dependencies** — a class can silently accumulate 15 `@Autowired` fields with no constructor signaling "this class has gotten too big" (a code smell warning that constructor injection surfaces naturally — if your constructor has 10 parameters, it screams "refactor me")
- Dependency Injection framework becomes a *hard requirement* just to instantiate the object at all

### Comparison Table

| Aspect | Constructor Injection | Setter Injection | Field Injection |
|---|---|---|---|
| Immutability (`final`) | ✅ Yes | ❌ No | ❌ No |
| Mandatory dependency enforcement | ✅ Yes | ❌ No (optional by nature) | ❌ No |
| Testability without Spring | ✅ Excellent | ⚠️ OK | ❌ Poor |
| Detects circular dependencies early | ✅ Yes (fails fast) | ⚠️ Can mask them | ⚠️ Can mask them |
| Surfaces "too many dependencies" code smell | ✅ Yes | ⚠️ Partially | ❌ No |
| Recommended by Spring team | ✅ **Yes — default choice** | For optional deps only | ❌ Discouraged |

### Advantages of DI (General)

- Loose coupling, testability, flexibility — same benefits as IoC generally, now made concrete
- Configuration of *which implementation* is used lives outside the business class entirely

### Disadvantages

- Slight indirection overhead in tracing "where did this value come from"
- Overuse of field injection (common in real-world legacy code) creates hard-to-test classes — this is a very real, very common production problem you'll encounter

### When to Use Which

- **Constructor injection: always, by default**, for all mandatory dependencies
- **Setter injection:** only for genuinely optional dependencies with sensible defaults
- **Field injection:** avoid in production code; acceptable only in throwaway test code or extremely trivial demos

### Best Practices

- Never mix all three injection types in one class — pick constructor injection and stay consistent
- Keep constructors small — if you need 8+ dependencies, it's a signal your class is doing too much (violates Single Responsibility Principle) and should be split
- Use `@RequiredArgsConstructor` (Lombok) to reduce constructor boilerplate in real projects (we'll touch Lombok when we hit project setup)

---

## STEP 6: SYNTAX — Full Annotation Breakdown

### `@Autowired`

- **Package:** `org.springframework.beans.factory.annotation.Autowired`
- **Applicable to:** constructors, setters, fields, even arbitrary methods
- **Behavior:** tells Spring "resolve and inject a matching bean here"
- **Matching strategy:** by **type** first; if multiple beans of that type exist, falls back to matching by **bean name** against the parameter/field name
- **`required` attribute:** `@Autowired(required = false)` — won't throw an exception if no matching bean exists (field stays `null` instead); use sparingly, as it hides configuration problems
- **Since Spring 4.3:** if a class has **exactly one constructor**, `@Autowired` is implicit — you may omit it

### `@Qualifier`

- **Package:** `org.springframework.beans.factory.annotation.Qualifier`
- **Purpose:** disambiguates when multiple beans of the same type exist
- **Usage:**
```java
public interface PaymentGateway {}

@Component("stripeGateway")
class StripePaymentGateway implements PaymentGateway {}

@Component("paypalGateway")
class PaypalPaymentGateway implements PaymentGateway {}

@Component
class OrderService {
    private final PaymentGateway paymentGateway;

    @Autowired
    public OrderService(@Qualifier("stripeGateway") PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

### `@Primary`

- **Package:** `org.springframework.context.annotation.Primary`
- **Purpose:** marks one bean as the **default choice** when multiple candidates exist, so you don't need `@Qualifier` at every injection point
```java
@Component
@Primary
class StripePaymentGateway implements PaymentGateway {}

@Component
class PaypalPaymentGateway implements PaymentGateway {}

// Now @Autowired PaymentGateway gateway; anywhere in the app
// resolves to StripePaymentGateway automatically, unless overridden with @Qualifier
```
**Rule:** `@Qualifier` at the injection point always wins over `@Primary` if both are present — explicit beats default.

### `@Value`

- **Package:** `org.springframework.beans.factory.annotation.Value`
- **Purpose:** injects literal values or externalized properties (not beans) — from `application.properties`, environment variables, or SpEL expressions
```java
@Component
class EmailService {
    @Value("${email.smtp.host}") // pulls from application.properties
    private String smtpHost;

    @Value("#{2 * 10}") // SpEL expression, evaluates to 20
    private int retryLimit;

    @Value("${email.timeout:5000}") // default value 5000 if property missing
    private int timeout;
}
```

### `@Lazy`

- **Package:** `org.springframework.context.annotation.Lazy`
- **Purpose:** defers bean creation until first use, instead of eager startup creation (overriding `ApplicationContext`'s default eager behavior for that specific bean)
```java
@Component
@Lazy
class HeavyReportGenerator { /* expensive to construct — only create when actually needed */ }
```
- **Common use:** breaking circular dependencies as a last-resort workaround, or deferring genuinely expensive-to-construct beans

---

## STEP 7: CODE EXAMPLES — Small to Enterprise

### Very Small Example

```java
interface Greeter {
    String greet();
}

@Component
class EnglishGreeter implements Greeter {
    public String greet() { return "Hello!"; }
}

@Component
class GreetingPrinter {
    private final Greeter greeter;

    @Autowired
    public GreetingPrinter(Greeter greeter) {
        this.greeter = greeter;
    }

    public void print() {
        System.out.println(greeter.greet());
    }
}
```
**Line-by-line:**
- `interface Greeter` — the abstraction `GreetingPrinter` will depend on (not a concrete class — key DI principle)
- `@Component` on `EnglishGreeter` — registers it as a Spring-managed bean during component scanning
- `@Autowired` constructor in `GreetingPrinter` — tells Spring "when creating this bean, find a `Greeter` bean and pass it here"
- `print()` — pure business logic, has zero Spring imports inside its body

### Medium Example — Multiple Implementations + Qualifier

```java
public interface DiscountStrategy {
    double applyDiscount(double price);
}

@Component("seasonalDiscount")
class SeasonalDiscount implements DiscountStrategy {
    public double applyDiscount(double price) { return price * 0.9; }
}

@Component("loyaltyDiscount")
class LoyaltyDiscount implements DiscountStrategy {
    public double applyDiscount(double price) { return price * 0.85; }
}

@Component
class CheckoutService {
    private final DiscountStrategy discountStrategy;

    @Autowired
    public CheckoutService(@Qualifier("loyaltyDiscount") DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double checkout(double price) {
        return discountStrategy.applyDiscount(price);
    }
}
```
**Explanation:** Two beans implement the same interface. Without `@Qualifier`, Spring would throw `NoUniqueBeanDefinitionException` (recall last topic's error list). `@Qualifier("loyaltyDiscount")` explicitly tells Spring which named bean to inject — resolving the ambiguity deterministically.

### Advanced Example — `@Value`, `@Primary`, and Setter Injection Combined

```java
public interface NotificationChannel {
    void send(String message);
}

@Component
@Primary
class EmailChannel implements NotificationChannel {
    @Value("${notification.email.from:no-reply@bank.com}")
    private String fromAddress;

    public void send(String message) {
        System.out.println("Email from " + fromAddress + ": " + message);
    }
}

@Component
class SmsChannel implements NotificationChannel {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

@Component
class AlertService {
    private final NotificationChannel primaryChannel; // constructor-injected, mandatory
    private NotificationChannel backupChannel;          // setter-injected, optional

    @Autowired
    public AlertService(NotificationChannel primaryChannel) { // resolves to EmailChannel via @Primary
        this.primaryChannel = primaryChannel;
    }

    @Autowired(required = false)
    public void setBackupChannel(@Qualifier("smsChannel") NotificationChannel backupChannel) {
        this.backupChannel = backupChannel;
    }

    public void alert(String message) {
        primaryChannel.send(message);
        if (backupChannel != null) backupChannel.send(message);
    }
}
```
**Explanation:**
- `EmailChannel` is `@Primary` → automatically chosen for the constructor injection point (mandatory dependency, no `@Qualifier` needed)
- `backupChannel` is setter-injected and marked `required = false` — genuinely optional; app still works fine if this setter never fires (though here it will, since `smsChannel` bean exists)
- This demonstrates *appropriately* mixing constructor (mandatory) + setter (optional) injection — a legitimate pattern, unlike mixing constructor + field injection carelessly

---

## STEP 9: COMMON ERRORS (DI-Specific)

**`NoUniqueBeanDefinitionException: expected single matching bean but found 2`**
- Cause: multiple beans of the same type, no `@Qualifier`/`@Primary` to disambiguate
- Fix: add `@Qualifier` at injection point, or mark one bean `@Primary`

**`UnsatisfiedDependencyException`**
- Cause: Spring found the target bean to inject into, but couldn't resolve one of its required constructor/setter arguments
- Fix: check that the dependency's own bean exists and is itself properly annotated/scanned

**`IllegalArgumentException: Could not resolve placeholder 'xyz' in value "${xyz}"`**
- Cause: `@Value("${xyz}")` references a property key that doesn't exist in any loaded property source
- Fix: verify `application.properties` has the key, correct spelling, correct profile is active (profiles come later)

---

## STEP 10: HANDS-ON PRACTICE

**5 Beginner Exercises:**
1. Rewrite the "Very Small Example" above using field injection, then explain in a comment why it's worse.
2. Create two implementations of an interface `Logger` (`ConsoleLogger`, `FileLogger`) and wire one via constructor injection.
3. Add `@Primary` to one of your `Logger` implementations and observe which one gets injected by default.
4. Use `@Value` to inject a hardcoded string into a bean and print it.
5. Deliberately create two unqualified beans of the same interface type and trigger `NoUniqueBeanDefinitionException` — read the actual stack trace.

**5 Intermediate Exercises:**
1. Build the `CheckoutService` example above, but add a third `DiscountStrategy` implementation and switch which one is injected using only `@Qualifier` (no code changes to `CheckoutService` needed except the qualifier string).
2. Combine `@Primary` and `@Qualifier` in the same app — prove that `@Qualifier` overrides `@Primary` at the injection point.
3. Create a setter-injected **optional** dependency using `@Autowired(required = false)` and verify your app still starts if that bean doesn't exist.
4. Use `@Value("${property:default}")` syntax to demonstrate default value fallback when a property is missing.
5. Refactor a field-injected class into constructor injection and write a plain JUnit test (no Spring context) that constructs it directly with a hand-made fake implementation.

**5 Advanced Exercises:**
1. Design a `PaymentProcessor` with 3 different `PaymentGateway` implementations, selected at runtime based on a `@Value`-injected configuration string (hint: you'll need a `Map<String, PaymentGateway>` injected — this is a preview of "collection injection," which we'll formalize if you attempt it).
2. Deliberately create a constructor-injection circular dependency between two beans and observe the exact exception and stack trace.
3. Break that same circular dependency using `@Lazy` on one side and explain *why* it works structurally (what changes about the internal creation order).
4. Explain, with your own code sample, a scenario where field injection would pass a code review at a company enforcing "constructor injection only" — is there ever a legitimate exception? (This is a genuine architecture discussion question.)
5. Write a class with 9 constructor-injected dependencies. Refactor it into 2–3 smaller classes with fewer dependencies each, preserving behavior. Explain your refactor.

**1 Mini Project:**
A `ShippingCostCalculator` app: interface `ShippingStrategy` with `StandardShipping`, `ExpressShipping`, `InternationalShipping` implementations. `OrderProcessor` depends on it via constructor injection, with the specific strategy chosen via `@Qualifier`, and a configurable "free shipping threshold" injected via `@Value`.

**1 Enterprise Assignment:**
Design (and implement) a `NotificationDispatcher` service for a banking app that must support Email, SMS, and Push notifications, where:
- Each channel is a `@Component` bean
- The "primary" channel is configurable via `@Value` + `@Primary`-equivalent custom logic (hint: you may need to research `Map<String, NotificationChannel>` injection to fully solve this — attempt it, we'll formalize the collection-injection pattern together if you get stuck)
- All wiring must use constructor injection only

---

## STEP 11: VIVA PREPARATION

**15 Beginner:**
1. Define Dependency Injection in one sentence.
2. Name the three types of DI in Spring.
3. Which type of DI allows `final` fields?
4. What does `@Autowired` do?
5. What does `@Qualifier` solve?
6. What does `@Primary` do?
7. What does `@Value` inject — beans or literal values?
8. True/False: Field injection is Spring's recommended default.
9. Since which Spring version is `@Autowired` optional on a single constructor?
10. What exception occurs when two beans match a required type with no disambiguation?
11. What's the difference between Service Locator and DI?
12. Can setter injection dependencies be `final`?
13. What does `required = false` do on `@Autowired`?
14. Give the SpEL syntax to inject a property with a default value.
15. What annotation would you add to defer a bean's creation until first use?

**10 Intermediate:**
1. Why does constructor injection make circular dependencies fail fast, while setter/field injection can mask them?
2. Explain why field injection is hard to unit test outside a Spring context.
3. If both `@Primary` and `@Qualifier` are present for the same injection point, which wins, and why architecturally does that make sense?
4. Why is it considered a code smell to have 10+ constructor parameters?
5. What happens internally, step by step, if you use `@Autowired(required = false)` and no matching bean exists?
6. Explain, using the container startup sequence from the previous topic, exactly *when* DI happens relative to bean instantiation and `BeanPostProcessor` execution.
7. Why can't Spring resolve `@Value("${missing.key}")` at compile time — what makes this fundamentally a runtime concern?
8. Give a real scenario where setter injection (optional dependency) is architecturally the *correct* choice over constructor injection.
9. How does `@Qualifier`'s string value relate to a bean's registered name — is it always the same as `@Component("name")`?
10. Why does the Spring team's own documentation recommend constructor injection as the default, citing "immutability" and "fail-fast" — connect both terms to specific code behaviors you've now seen.

**5 Scenario-Based:**
1. Your app has 3 `PaymentGateway` beans and injection fails with `NoUniqueBeanDefinitionException` — walk through your exact diagnostic and fix process.
2. A teammate submits a PR using field injection everywhere. What do you say in code review, and why (cite specific technical reasons, not just "best practice")?
3. You need a bean that's expensive to construct (loads a large ML model) but is only used in 1% of requests. What annotation helps, and why?
4. Two of your beans depend on each other via constructor injection. What's your first move to diagnose, and what are two possible fixes?
5. A `@Value` injection throws `IllegalArgumentException: Could not resolve placeholder`. List 3 possible root causes.

**5 Debugging Questions:**
1. Given a stack trace with `UnsatisfiedDependencyException`, what specific bean and constructor parameter would you look for first?
2. How would you use logging/debugging to confirm exactly which implementation got injected when `@Primary` is in play?
3. Your test uses `new OrderService(...)` directly (no Spring context) but a field-injected dependency inside is `null` at runtime — explain precisely why, using what you know about field injection's mechanism.

**5 Coding Questions:**
1. Write a class with two constructor-injected mandatory dependencies and one setter-injected optional dependency.
2. Given two implementations of an interface, write the minimal code to make one the default without touching injection points.
3. Write a bean that injects a property value with a default fallback of `10`.
4. Fix this broken code (I'll give you a snippet with an ambiguous `@Autowired` — try predicting the fix from the pattern you've learned).
5. Convert a given field-injected class (I'll provide one) into proper constructor injection.

---

## STEP 14: QUICK REVISION — Cheat Sheet for This Topic

| Concept | One-Line Summary |
|---|---|
| DI | The mechanism Spring uses to implement IoC — dependencies handed to objects, not fetched by them |
| Constructor Injection | Default/best choice — immutable, mandatory, most testable |
| Setter Injection | For optional dependencies only |
| Field Injection | Discouraged — poor testability, hides dependencies, no immutability |
| `@Autowired` | Marks injection point; type-matching, falls back to name |
| `@Qualifier` | Disambiguates between multiple beans of same type by name |
| `@Primary` | Sets a default bean when multiple candidates exist (loses to `@Qualifier` if both present) |
| `@Value` | Injects literal values / properties / SpEL expressions, not beans |
| `@Lazy` | Defers bean creation until first actual use |

---

## Where We Are

✅ Topic 1: What is Spring
✅ Topic 2: Spring Modules & Architecture
✅ Topic 3: IoC — Concept + Internals
✅ Topic 4: **Dependency Injection — Types, Syntax, Code**

⏭️ **Next: Bean, Bean Scope, and Bean Lifecycle** — we'll formalize exactly what a "bean" is (syntax-level), the 6 bean scopes (`singleton`, `prototype`, `request`, `session`, `application`, `websocket`), and the *complete* lifecycle callback system (`@PostConstruct`, `@PreDestroy`, `InitializingBean`, `DisposableBean`) with working code for each.

This was a heavy, code-dense topic. **Before continuing — can you explain, without looking, why constructor injection is preferred over field injection, citing at least 3 distinct technical reasons (not just "it's best practice")?** Try a few viva questions above, then say "next" when ready.