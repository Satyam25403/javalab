import java.util.*;

// | **List Implementation** | **Key Features / Use Case**       |
// | ----------------------- | --------------------------------- |
// | **ArrayList**           | Fast access, better for searching |
// | **LinkedList**          | Fast insertion/deletion           |
// | **Vector**              | Legacy, synchronized              |
// | **Stack**               | LIFO operations                   |

// ArrayList alone covers "list implementation" for interviews mostly
public class ListDemo {

    // -----------------------------------------
    // 1️⃣ ArrayList Demo
    // -----------------------------------------
    static void arrayListDemo() {
        List<String> list = new ArrayList<>();

        list.add("Apple");
        list.add("Banana");
        list.add("Mango");
        list.add("Orange");

        System.out.println("\nArrayList: " + list);

        // Access
        System.out.println("Element at index 1: " + list.get(1));

        // Update
        list.set(2, "Grapes");
        System.out.println("After update: " + list);

        // Remove
        list.remove("Orange");
        System.out.println("After removal: " + list);

        // Traversal
        System.out.println("Iterating ArrayList:");
        for (String item : list)
            System.out.println(item);
    }

    // -----------------------------------------
    // 2️⃣ LinkedList Demo
    // -----------------------------------------
    static void linkedListDemo() {
        LinkedList<String> list = new LinkedList<>();

        list.add("Red");
        list.add("Green");
        list.add("Blue");
        list.add("Yellow");

        System.out.println("\nLinkedList (Doubly LinkedList): " + list);

        // Access
        System.out.println("First element: " + list.get(0));

        // Update
        list.set(1, "Black");
        System.out.println("After update: " + list);

        // Remove
        list.removeLast();                  //only available in LinkedList class not available in List interface
        System.out.println("After removeLast(): " + list);
        list.remove(1);
        System.out.println("After removal of element at 1st index: " + list);

        // Traversal
        System.out.println("Iterating LinkedList:");
        for (String color : list)
            System.out.println(color);
    }

    // -----------------------------------------
    // 4️⃣ Stack Demo
    // -----------------------------------------
    static void stackDemo() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        System.out.println("\nStack (LIFO): " + stack);

        System.out.println("Top element: " + stack.peek());

        System.out.println("Popped: " + stack.pop());
        System.out.println("Stack after pop: " + stack);

        // Traversal
        System.out.println("Iterating Stack:");
        for (String ch : stack)
            System.out.println(ch);
    }

    // -----------------------------------------
    // 3️⃣ Vector Demo
    // -----------------------------------------
    static void vectorDemo() {
        List<String> vec = new Vector<>();

        vec.add("Pen");
        vec.add("Pencil");
        vec.add("Eraser");

        System.out.println("\nVector (Legacy, Thread-Safe): " + vec);

        // Update
        vec.set(1, "Marker");

        // Remove
        vec.remove(0);

        System.out.println("After updates: " + vec);

        // Traversal
        System.out.println("Iterating Vector:");
        for (String item : vec)
            System.out.println(item);
    }
}
