import java.util.*;

// | **Set Implementation** | **Order**       | **Duplicates** | **Performance** | **Notes**               |
// | ---------------------- | --------------- | -------------- | --------------- | ----------------------- |
// | **HashSet**            | No ordering     | ❌ Not allowed  | Fastest         | Uses HashMap internally |
// | **LinkedHashSet**      | Insertion order | ❌ Not allowed  | Medium          | Good when order matters |
// | **TreeSet**            | Sorted order    | ❌ Not allowed  | Slowest         | Uses Red-Black Tree     |

// HashSet is the primary Set implementation interviewers expect mostly
public class SetDemo {

    // --------------------------------------------------
    // 1️⃣ HashSet Demo: No order, Fastest performance
    // --------------------------------------------------
    static void hashSetDemo() {
        Set<String> set = new HashSet<>();

        set.add("Apple");
        set.add("Banana");
        set.add("Mango");
        set.add("Orange");
        set.add("Banana"); // Duplicate ignored

        System.out.println("\nHashSet (No Order, Fast): " + set);

        // Remove
        set.remove("Orange");
        System.out.println("After removing Orange: " + set);

        // Check
        System.out.println("Contains Mango? " + set.contains("Mango"));

        // Traversal
        System.out.println("Iterating HashSet:");
        for (String item : set)
            System.out.println(item);
    }

    // --------------------------------------------------
    // 2️⃣ LinkedHashSet Demo: maintains insertion order, medium performance
    // --------------------------------------------------
    static void linkedHashSetDemo() {
        Set<String> set = new LinkedHashSet<>();

        set.add("Dog");
        set.add("Cat");
        set.add("Horse");
        set.add("Cow");
        set.add("Cat");  // Duplicate ignored

        System.out.println("\nLinkedHashSet (Maintains Insertion Order): " + set);

        // Remove
        set.remove("Horse");
        System.out.println("After removing Horse: " + set);

        System.out.println("Contains Dog? " + set.contains("Dog"));

        // Traversal
        System.out.println("Iterating LinkedHashSet:");
        for (String item : set)
            System.out.println(item);
    }

    // --------------------------------------------------
    // 3️⃣ TreeSet Demo: sorted order of elements, slowest performance
    // --------------------------------------------------
    static void treeSetDemo() {
        TreeSet<String> set = new TreeSet<>();

        set.add("Banana");
        set.add("Apple");
        set.add("Mango");
        set.add("Orange");

        System.out.println("\nTreeSet (Sorted Order): " + set);

        // First & Last elements
        System.out.println("First: " + set.first());
        System.out.println("Last: " + set.last());

        // Remove
        set.remove("Mango");
        System.out.println("After removing Mango: " + set);

        System.out.println("Contains Apple? " + set.contains("Apple"));

        // Traversal
        System.out.println("Iterating TreeSet:");
        for (String item : set)
            System.out.println(item);
    }
}
