//loops, control transfer statements

import java.util.*;
import java.util.stream.Collectors;

public class LoopsAndStreams {
    public static void main(String[] args) {
        int i;

        // for loop
        for (i = 0; i < 100; i++) {
        }

        // while
        while (i < 10) {
        }

        // do while
        do {
        } while (i < 10); // excecute atleast once

        // break: - Exits the nearest enclosing loop immediately.
        // continue: jump to next iteration i.e. skip current iteration
        // in general normal break and continue work on a single loop

        // labelled break and continue :to do their functionality on nested loops also
        here: // labelname of the immediate block
        while (i < 10) {
            while (i < 5) {
                if (i == 4) {
                    continue here;
                }
            }
        }

        // Used to control outer loops from inside nested loops.
        // ✅ Example:
        outer: for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1)
                    continue outer;
                System.out.println(k + " " + j);
            }
        }
        // - continue outer; skips to next iteration of outer loop
        // - break outer; exits outer loop entirely

        // When would you use labelled break/continue?
        // A: In nested loops where inner logic needs to affect outer loop flow—like
        // matrix traversal or early exit from nested search.

        // for each loop: enhanced for loop
        int[] arr = { 1, 2, 3 };
        for (int num : arr) {
            System.out.println(num);
        }
        // - Works with arrays and Iterable types (List, Set, etc.)
        // - No access to index
        // - Can’t modify the collection structure (e.g., remove elements safely)



        // STREAM API:
        // A Stream is a sequence of elements from a source (like a List, Set, or array)
        // that supports aggregate operations such as filtering, mapping, and reducing.
        // Stream API is a pipeline for transforming data.
        // Every stream operation follows this structure:
        // source.stream()
        // .intermediateOperation() //filter(): select elements by apply some condition,
        // map(): transform each element, sorted(), distinct(): remove duplicates
        // .terminalOperation(); //forEach(): some action on each element, collect():
        // gather results into a collection, reduce(): combines element into one result
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.stream()
                .filter(name -> name.startsWith("A"))
                .map(name -> name.toUpperCase())
                .forEach(name -> System.out.println(name)); // prints ALICE

        //we use collectors to gather stream results
        List<String> result = names.stream()
                           .filter(n -> n.length() > 3)
                           .collect(Collectors.toList());
        // Other collectors:
        // - toSet()
        // - joining()
        // - groupingBy()
        // - toMap()

        //- Streams don’t modify the original collection.
        //- Streams are single-use—once consumed, they can’t be reused.

    }
}
