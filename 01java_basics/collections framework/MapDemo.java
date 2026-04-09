import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
//Map does not extend Collection interface(it is a seperate branch in Collections Framework)
//Map: key-value pairs, unique keys(not even more number of nulls, only one null allowed)
// | Implementation        | Use Case                 |
// | --------------------- | ------------------------ |
// | **HashMap**           | Fast, no order           |
// | **LinkedHashMap**     | Maintain insertion order |
// | **TreeMap**           | Sorted order(Natural order) |
// | **ConcurrentHashMap** | Thread-safe              |

// HashMap alone covers the essential Map DS.

public class MapDemo {
    //Internal structure of HashMap contains: Key, value, Hashfunction(convert a key into an index(bucket location)):collision handling using chaining, 
    //Bucket(Array of LinkedLists)...Balanced Binary Search Tree for high collision scenarios(when collisions exceed a threshold(ex: at certain bucket, 
    //linked list size becomes >8)...treefication occurs Java 8+) red black tree specifically

    // ----------------------------------------
    // HashMap implementation: No order, fast
    // ----------------------------------------
    static void hashMapDemo() {
        //load factor=0.75 (default), initial capacity=16 (default)
        //when size exceeds capacity*load factor, rehashing occurs(doubling the capacity and redistributing of existing entries)
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Apple", 100);
        map.put("Banana", 40);
        map.put("Mango", 80);
        map.put("Orange", 60);
        //NOTE: if key is an object of custom class and we want to use that as key in map, we must override equals() and hashCode() methods of Object class in that custom class to make objects having same content as equal keys
        //because HashMap uses these methods to check for key equality and to determine the bucket location respectively. otherwise even two different objects created with same values will be treated as different keys

        System.out.println("Initial Map: " + map);

        map.put("Apple", 120); // Overwrites old value
        System.out.println("After Updating Apple: " + map);

        System.out.println("Price of Mango: " + map.get("Mango"));

        System.out.println("Contains key 'Banana'? " + map.containsKey("Banana"));
        System.out.println("Contains value 60? " + map.containsValue(60));

        map.remove("Orange");
        System.out.println("After Removing Orange: " + map);

        // Iterating over keys(.keySet() returns a set)
        System.out.println("\nIterating over keys:");
        for (String key : map.keySet()) {
            System.out.println(key);
        }

        // Iterating over values(values may or maynot be unique hence returns a Collection)
        System.out.println("\nIterating over values:");
        for (Integer value : map.values()) {
            System.out.println(value);
        }

        // Iterating over key-value pairs (Most Used)
        System.out.println("\nIterating over entries:");
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // D) Using forEach (Java 8+)
        System.out.println("\nUsing forEach:");
        map.forEach((key, value) -> System.out.println(key + " costs " + value));

        System.out.println("\nSize of map: " + map.size());

        System.out.println("Is map empty? " + map.isEmpty());

        map.clear();
        System.out.println("After clear(): " + map);
    }
    
    // ----------------------------------------
    // 2.LinkedHashMap implementation : insertion order preserved, moderate speed
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
