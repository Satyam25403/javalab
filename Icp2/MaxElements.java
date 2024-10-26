import java.util.Scanner;
import java.util.*;

public class MaxElements {
    private static Stack<Integer> mainStack = new Stack<>();
    private static Stack<Integer> maxStack = new Stack<>();
    
    public static void main(String[] args) {
        ArrayList<Integer> a=new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int numberOfQueries = scanner.nextInt();

        for (int i = 0; i < numberOfQueries; i++) {
            int queryType = scanner.nextInt();
            switch (queryType) {
                case 1:
                    int value = scanner.nextInt();
                    push(value);
                    break;
                case 2:
                    pop();
                    break;
                case 3:
                    a.add(getMax());
                    break;
            }
        }
        scanner.close();
        for(int i:a){
            System.out.println(i);
        }
        
    }

    private static void push(int x) {
        mainStack.push(x);
        if (maxStack.isEmpty() || x >= maxStack.peek()) {
            maxStack.push(x);
        }
    }

    private static void pop() {
        if (!mainStack.isEmpty()) {
            int popped = mainStack.pop();
            if (popped == maxStack.peek()) {
                maxStack.pop();
            }
        }
    }

    private static int getMax() {
        if (!maxStack.isEmpty()) {
            return maxStack.peek();
        } else {
            return -1;
        }
    }
}
