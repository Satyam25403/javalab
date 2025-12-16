import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//List: order preservation, allows duplicates, indexed access, change size dynamically

// Common List Implementations in Java:
// | **List Implementation** | **Key Features / Use Case**       |
// | ----------------------- | --------------------------------- |
// | **ArrayList**           | Fast access, better for searching |
// | **LinkedList**          | Fast insertion/deletion           |
// | **Vector**              | Legacy, synchronized              |
// | **Stack**               | LIFO operations                   |

class StringLengthComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {              //int returntype: negative for same order of elements as in args(1st object comes first), positive for reverse order of args, 0 for no change
        return s1.length() - s2.length();       //ascending order based on length
    }
}
// ArrayList alone covers "list implementation" for interviews mostly
public class ListDemo {

    // -----------------------------------------
    // 1️⃣ ArrayList Demo: just a dynamic array, resize factor: 1.5x
    // -----------------------------------------
    static void arrayListDemo() {
        //O(n) for add/remove in worst case due to resizing and shifting elements, amortized O(1) for add at end
        //O(1) for access by index

        List<String> list = new ArrayList<>();
        // List<String> listWithCapacity = new ArrayList<>(100); //if we know initial
        // capacity, so that resize operation overhead is reduced
        // ArrayList<String> list = new ArrayList<>(); //can also use ArrayList
        // reference directly to use arraylist specific methods

        // Arrays.asList()... returns fixed size(cant add or remove elements) list
        // backed by array
        String[] arr = { "Apple", "Banana" };
        List<String> listFromArray = Arrays.asList(arr);

        // int[] arr2={1,2,3};
        // List<Integer> listFromArray2 = Arrays.asList(arr2); //won't work as expected
        // because of autoboxing, will treat entire arr2 as single element
        Integer[] arr3 = { 1, 2, 3 };
        List<Integer> listFromArray3 = Arrays.asList(arr3); // works fine

        // new list from another list(maybe from fixed size list as above)
        List<String> newList = new ArrayList<>(listFromArray); // can now add/remove elements in newList

        // add all elements from another collection into this list
        List<String> anotherList = new ArrayList<>();
        anotherList.add("Mango");
        anotherList.add("Orange");
        newList.addAll(anotherList);

        // // list that cant be even modified nor added/removed elements: from java9
        // List<String> unmodifiableList = List.of("A", "B", "C");
        // System.out.println(unmodifiableList); // [A, B, C]
        // // Try modifying
        // unmodifiableList.add("D"); // throws UnsupportedOperationException
        // unmodifiableList.remove("A"); // throws UnsupportedOperationException
        // unmodifiableList.set(0, "Z"); // throws UnsupportedOperationException

        // creating array from list
        list.toArray(); // returns Object[] array which may need casting
        String[] array = list.toArray(new String[0]); // preferred way
        //Integer[] intArray = listFromArray3.toArray(new Integer[0]);
        // Even though new String[0] has zero length:
        // - Java sees it’s a String[], so it knows the correct type.
        // - Internally, it allocates a new array of size list.size() and returns it.

        //sorting a list using Collections utility class: comparator is the 2nd argument used to define custom sorting order
        Collections.sort(newList); //ascending
        Collections.sort(newList, Collections.reverseOrder()); //descending
        System.out.println("Sorted List: " + newList);
        list.sort(null);        //sort using List's default sort method (Java 8+), null means natural order...generally argument is comparator used to define custom sorting
        //example: comparator to sort based on string length
        list.sort(new StringLengthComparator());
        //or use lambda expression
        list.sort((s1, s2) -> s1.length() - s2.length());


        // add elements: worst case O(n) to resize and copy elements to new array
        list.add("Apple");
        list.add("Banana");
        list.add("Mango");
        list.add("Orange");
        list.add(0, "Pineapple"); // add at specific index

        System.out.println("\nArrayList: " + list); // prints because of toString() override

        System.out.println(list.isEmpty() ? "List is empty" : "List is not empty" );

        // size
        System.out.println(list.size() + " elements in the ArrayList");
        // capacity
        // System.out.println("Capacity: " + ((ArrayList<String>) list).capacity());
        // //no direct method to get capacity...

        // Access: index based
        System.out.println("Element at index 1: " + list.get(1));

        // check existence
        System.out.println(list.contains("Banana"));

        // Update
        list.set(2, "Grapes");
        System.out.println("After update: " + list);

        // Remove(remove first occurance): O(n) because of shifting elements, but after
        // removal the size is
        // reduced, capacity remains same: i.e. same resized capacity
        list.remove("Orange");
        System.out.println("After removal: " + list);
        list.remove(0); // here index based removal...for integer array if u want to remove by value use
        // list.remove(Integer.valueOf(3)); to avoid confusion with index asthis makes
        // sure the argument is treated as object not index: removes element 3 and not
        // element at index 3
        System.out.println("After removal at index 0: " + list);
        //list1.removeAll(anotherList); //remove all elements present in another collection from this list

        // to reduce capacity: i.e. shrink to fit current size
        ((ArrayList<String>) list).trimToSize();

        // insert at specific index
        list.add(1, "Kiwi");
        System.out.println("After inserting Kiwi at index 1: " + list);

        // Traversal
        System.out.println("Iterating ArrayList:");
        for (String item : list)
            System.out.println(item);

        list.clear(); // remove all elements from the list

    }

    // -----------------------------------------
    // 2️⃣ LinkedList Demo: stores doubly linked list internally, stores elements as nodes with data and pointers to next and previous nodes
    // -----------------------------------------
    static void linkedListDemo() {
        //Better for frequent insertions and deletions compared to ArrayList
        //slower random access(get(index)) as it requires traversal from begining to desired index
        //memory overhead due to storing pointers in addition to data
        LinkedList<String> list = new LinkedList<>();

        list.add("Red");
        list.add("Green");
        list.add("Blue");
        list.add("Yellow");

        list.addFirst("Purple");
        list.addLast("Orange");
        System.out.println("\nLinkedList (Doubly LinkedList): " + list);

        // Access: sequential access, O(n)
        System.out.println("First element: " + list.get(0));
        System.out.println(list.getFirst());
        System.out.println(list.getLast());

        // Update
        list.set(1, "Black");
        System.out.println("After update: " + list);


        // Remove
        list.removeLast(); // only available in LinkedList class not available in List interface
        System.out.println("After removeLast(): " + list);
        list.removeFirst(); // only available in LinkedList class not available in List interface
        System.out.println("After removeFirst(): " + list);
        list.remove(1);
        System.out.println("After removal of element at 1st index: " + list);
        list.remove("Red");             //if list is of integers and u want to remove by value use list.remove(Integer.valueOf(3)); to avoid confusion with index
        //and pass the arg as value to delete not as index
        System.out.println("After removal of Red: " + list);
        list.removeIf((x)-> x.length()>10); //remove elements based on condition using predicate (condition: lambda expression here)

        // Remove all elements present in another collection
        LinkedList<String> animals = new LinkedList<>(Arrays.asList("Tiger","Lion","Elephant","Giraffe"));
        LinkedList<String> animalsToRemove= new LinkedList<>(Arrays.asList("Tiger","Elephant"));
        animals.removeAll(animalsToRemove); //remove all elements present in animalsToRemove from animals list

        // Traversal
        System.out.println("Iterating LinkedList:");
        for (String color : list)
            System.out.println(color);

        list.clear(); //remove all elements from the list
    }

    // -----------------------------------------
    // 4️⃣ Stack Demo:extends Vector, LIFO structure hence can cause overhead since extended from Vector
    // -----------------------------------------
    static void stackDemo() {
        //Recommended way for implementing stack is using Deque interface with ArrayDeque class
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

        System.out.println("Is stack empty? " + stack.isEmpty());
        System.out.println("Stack size: " + stack.size());

        System.out.println("Search for B in stack, position from top (1-based index): " + stack.search("B"));

        //you can add at some index using add() and remove(object/element at an index) method inherited from Vector/List, but not recommended as it violates LIFO principle
        stack.clear(); //remove all elements from the stack


        //linkedlist used as stack where u dont want overhead of vector's synchronized methods
        LinkedList<Integer> llStack = new LinkedList<>();
        llStack.addLast(2); //push
        llStack.removeLast(); //pop
        llStack.getLast(); //peek
        llStack.isEmpty();
    }

    // -----------------------------------------
    // 3️⃣ Vector Demo: only use when thread-safety is required
    // -----------------------------------------
    static void vectorDemo() {
        //no data corruption in multithreaded env as methods are synchronized, but may have performance overhead due to synchronization
        //when current capacity exceeded, it doubles the capacity (i.e. resize factor is 2x) or resizing capacity can be specified in constructor
        //allows random access like ArrayList in O(1) time
        List<String> vec = new Vector<>();
        //Vector<String> vec = new Vector<>(initial: 50, increment: 10); //initial capacity 50, resize by 10 elements more when exceeded
        //Vector<String> vec = new Vector<>(collection); //vetcor from another collection

        vec.add("Pen");
        vec.add("Pencil");
        vec.add("Eraser");
        vec.add(1,"Sharpener");

        System.out.println("\nVector (Legacy, Thread-Safe:synchronized(may have performance overhead)): " + vec);

        // Update
        vec.set(1, "Marker");

        // Remove
        vec.remove(0);
        //vec.remove(Integer.valueOf(3)); //if vector is of integers and u want to remove by value use this to avoid confusion of integer value with that of index

        System.out.println("After updates: " + vec);

        // Traversal
        System.out.println("Iterating Vector:");
        for (String item : vec)
            System.out.println(item);
    }

    // -----------------------------------------
    // 5️⃣ CopyOnWriteArrayList Demo: thread-safe variant of ArrayList
    // -----------------------------------------
    static void copyOnWriteArrayListDemo() {
        //thread-safe variant of ArrayList where all mutative(write) operations (add, set, remove and so on) are implemented by making a fresh copy of the underlying array instead of modifying the existing list
        //suitable for scenarios where reads are more frequent than writes
        //Read operations do not require locking and can proceed concurrently with write operations: hence will be fast and direct
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();

        cowList.add("One");
        cowList.add("Two");
        cowList.add("Three");

        System.out.println("\nCopyOnWriteArrayList (Thread-Safe for concurrent reads/writes): " + cowList);

        // Update
        cowList.set(1, "Two Updated");

        // Remove
        cowList.remove("Three");

        System.out.println("After updates: " + cowList);

        // Traversal
        System.out.println("Iterating CopyOnWriteArrayList:");
        for (String item : cowList)
            System.out.println(item);
    }
}

