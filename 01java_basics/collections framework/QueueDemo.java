import java.util.*;

// | **Queue Implementation** | **Behavior**                             | **Use Case**              |
// | ------------------------ | ---------------------------------------- | ------------------------- |
// | **PriorityQueue**        | Min-Heap (smallest element served first) | Scheduling, CPU tasks     |
// | **ArrayDeque**           | Double-ended queue, fastest              | Stack + Queue replacement |
// | **LinkedList (Queue)**   | Classic FIFO                             | Simple queuing            |

// ➡ PriorityQueue is the main queue DS.
// ArrayDeque is a faster modern alternative to Stack + Queue.

public class QueueDemo {

    // --------------------------------------------------
    // 1️⃣ PriorityQueue Demo
    // --------------------------------------------------
    static void priorityQueueDemo() {
        Queue<Integer> pq = new PriorityQueue<>();

        pq.add(30);
        pq.add(10);
        pq.add(40);
        pq.add(20);

        System.out.println("\nPriorityQueue (Min-Heap): " + pq);

        System.out.println("Head element (peek): " + pq.peek());  // Smallest value

        pq.remove();  // removes head (smallest)
        System.out.println("After remove(): " + pq);

        // Traversal
        System.out.println("Iterating PriorityQueue:");
        for (int x : pq)
            System.out.println(x);
    }

    // --------------------------------------------------
    // 2️⃣ ArrayDeque Demo
    // --------------------------------------------------
    static void arrayDequeDemo() {
        ArrayDeque<String> dq = new ArrayDeque<>();

        dq.add("A");
        dq.add("B");
        dq.add("C");
        dq.add("D");

        System.out.println("\nArrayDeque (Double-Ended Queue): " + dq);

        // Insert at both ends
        dq.addFirst("X");
        dq.addLast("Y");

        System.out.println("After addFirst & addLast: " + dq);

        // Access
        System.out.println("First: " + dq.getFirst());
        System.out.println("Last: " + dq.getLast());

        // Removal
        dq.removeFirst();
        dq.removeLast();
        System.out.println("After removeFirst & removeLast: " + dq);

        // Traversal
        System.out.println("Iterating ArrayDeque:");
        for (String s : dq)
            System.out.println(s);
    }

    // --------------------------------------------------
    // 3️⃣ LinkedList Demo (Used as Queue)
    // --------------------------------------------------
    static void linkedListQueueDemo() {
        Queue<String> queue = new LinkedList<>();

        queue.add("One");
        queue.add("Two");
        queue.add("Three");
        queue.add("Four");

        System.out.println("\nLinkedList as Queue (FIFO): " + queue);

        // Peek
        System.out.println("Peek: " + queue.peek());

        // Poll
        System.out.println("Poll (remove head): " + queue.poll());
        System.out.println("After poll(): " + queue);

        // Add
        queue.offer("Five");
        System.out.println("After offer(): " + queue);

        // Traversal
        System.out.println("Iterating LinkedList Queue:");
        for (String s : queue)
            System.out.println(s);
    }
}
