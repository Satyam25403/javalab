import java.util.HashMap;
import java.util.Scanner;

public class MajorityElement {
    static void bruteForceSolution(int a[]) {
        // count freq of each element: return which has count> n/2
        // O(n^2) time complexity
        for (int i = 0; i < a.length; i++) {
            int count = 0;
            for (int j = 0; j < a.length; j++) {
                if (a[i] == a[j]) {
                    count++;
                }
            }
            if (count > Math.floorDiv(a.length, 2)) {
                System.out.println("Majority element: " + a[i]);
                return;
            }
        }
        System.out.println("No majority element exists");
    }

    static void levelOneOptimization(int a[]) {
        // maintaining count of each element in hasharray and returning index where
        // count> n/2:
        // O(n log n): if we take ordered map i.e. treemap else in this case is O(n) +O(n) for traversal in map; O(1) extra
        // space
        HashMap<Integer, Integer> map = new HashMap<>(); // element and its count

        for (int i = 0; i < a.length; i++) {
            if (map.containsKey(a[i])) {
                map.put(a[i], map.get(a[i]) + 1);
            } else {
                map.put(a[i], 1);
            }

            // map.put(a[i], map.getOrDefault(a[i], 0) + 1);
            // this is replacement for above if-else block
        }

        int threshold = Math.floorDiv(a.length, 2);
        for (int i : map.keySet()) {
            if (map.get(i) > threshold) { // only one occurance found
                System.out.println("Majority Element in array " + i);
                return;
            }
        }
        System.out.println("No majority element");
    }

    static void mostOptimalSolution(int a[]) {
        // Moore's voting algorithm

        int count = 0, candidate = -1;
        for (int i = 0; i < a.length; i++) {
            if (count == 0) {
                // elements upto here have cancelled each other, i.e. there should exist an
                // element which occurs same no. of times as others occurences combined
                candidate = a[i];
            }

            if (a[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }

        // if there exists a majority element in array, it will be the value stored in
        // majorityElement variable and no one else
        count = 0;
        for (int i : a) { 
            if (candidate == i) {
                count++;
            }
        }
        if (count > a.length / 2) {
            System.out.println("Majority element: " + candidate);
        } else {
            System.out.println("No majority element");
        }

    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int a[] = new int[7];
        System.out.println("Enter array:");
        for (int i = 0; i < 7; i++) {
            a[i] = sc.nextInt();
        }

        bruteForceSolution(a);
        levelOneOptimization(a);
        mostOptimalSolution(a);
    }
}
