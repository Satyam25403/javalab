import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// | Implementation        | Use Case                 |
// | --------------------- | ------------------------ |
// | **HashMap**           | Fast, no order           |
// | **LinkedHashMap**     | Maintain insertion order |
// | **TreeMap**           | Sorted order             |
// | **ConcurrentHashMap** | Thread-safe              |

// HashMap alone covers the essential Map DS.

public class MapDemo {

    // ----------------------------------------
    // 1️⃣ Creating a Map (using HashMap implementation): No order, fast
    // ----------------------------------------
    static void hashMapDemo() {
        Map<String, Integer> map = new HashMap<>();

        // ----------------------------------------
        // 2️⃣ Adding key-value pairs
        // ----------------------------------------
        map.put("Apple", 100);
        map.put("Banana", 40);
        map.put("Mango", 80);
        map.put("Orange", 60);

        System.out.println("Initial Map: " + map);

        // ----------------------------------------
        // 3️⃣ Updating a value (same key)
        // ----------------------------------------
        map.put("Apple", 120); // Overwrites old value
        System.out.println("After Updating Apple: " + map);

        // ----------------------------------------
        // 4️⃣ Accessing value using key
        // ----------------------------------------
        System.out.println("Price of Mango: " + map.get("Mango"));

        // ----------------------------------------
        // 5️⃣ Checking if key/value exists
        // ----------------------------------------
        System.out.println("Contains key 'Banana'? " + map.containsKey("Banana"));
        System.out.println("Contains value 60? " + map.containsValue(60));

        // ----------------------------------------
        // 6️⃣ Removing a key
        // ----------------------------------------
        map.remove("Orange");
        System.out.println("After Removing Orange: " + map);

        // ----------------------------------------
        // 7️⃣ Traversal Methods
        // ----------------------------------------

        // A) Iterating over keys
        System.out.println("\nIterating over keys:");
        for (String key : map.keySet()) {
            System.out.println(key);
        }

        // B) Iterating over values
        System.out.println("\nIterating over values:");
        for (Integer value : map.values()) {
            System.out.println(value);
        }

        // C) Iterating over key-value pairs (Most Used)
        System.out.println("\nIterating over entries:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // D) Using forEach (Java 8+)
        System.out.println("\nUsing forEach:");
        map.forEach((key, value) -> System.out.println(key + " costs " + value));

        // ----------------------------------------
        // 8️⃣ Getting Size
        // ----------------------------------------
        System.out.println("\nSize of map: " + map.size());

        // ----------------------------------------
        // 9️⃣ Checking if empty
        // ----------------------------------------
        System.out.println("Is map empty? " + map.isEmpty());

        // ----------------------------------------
        // 🔟 Clearing the map
        // ----------------------------------------
        map.clear();
        System.out.println("After clear(): " + map);
    }
    
    // ----------------------------------------
    // 2. Creating a Map (using LinkedHashMap implementation): insertion order preserved, moderate speed
    // ----------------------------------------
    static void linkedHashMapDemo() {
        Map<String, Integer> map = new LinkedHashMap<>();

        map.put("Apple", 100);
        map.put("Banana", 40);
        map.put("Mango", 80);
        map.put("Orange", 60);

        System.out.println("\nLinkedHashMap (Insertion Order Preserved): " + map);

        System.out.println(map.get("Mango"));

        System.out.println("Iterating LinkedHashMap:");
        for (Map.Entry<String, Integer> e : map.entrySet())
            System.out.println(e.getKey() + " → " + e.getValue());
    }

    // ----------------------------------------
    // 3. Creating a Map (using TreeMap implementation): sorted by key, slower
    // ----------------------------------------
    static void treeMapDemo() {
        Map<String, Integer> map = new TreeMap<>();

        map.put("Mango", 80);
        map.put("Banana", 40);
        map.put("Apple", 100);
        map.put("Orange", 60);

        System.out.println("\nTreeMap (Sorted by Key): " + map);

        System.out.println("Iterating TreeMap:");
        for (Map.Entry<String, Integer> e : map.entrySet())
            System.out.println(e.getKey() + " → " + e.getValue());
    }
    



    
    static void hashTableDemo() {
        Map<String, Integer> map = new Hashtable<>();

        map.put("Apple", 100);
        map.put("Banana", 40);
        map.put("Mango", 80);
        map.put("Orange", 60);

        System.out.println("\nHashtable (Thread-Safe, No null keys/values): " + map);

        System.out.println("Iterating Hashtable:");
        for (Map.Entry<String, Integer> e : map.entrySet())
            System.out.println(e.getKey() + " → " + e.getValue());

        // map.put(null, 10); // ❌ Throws NullPointerException
        // map.put("Apple", null); // ❌ Throws NullPointerException

    }

    static void concurrentHashMapDemo() {
        Map<String, Integer> map = new ConcurrentHashMap<>();

        map.put("Apple", 100);
        map.put("Banana", 40);
        map.put("Mango", 80);
        map.put("Orange", 60);

        System.out.println("\nConcurrentHashMap (Thread-Safe, High Performance): " + map);

        System.out.println("Iterating ConcurrentHashMap:");
        for (Map.Entry<String, Integer> e : map.entrySet())
            System.out.println(e.getKey() + " → " + e.getValue());
    }

}
