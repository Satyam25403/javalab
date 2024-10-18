
import java.util.*;

class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        if (!stack.isEmpty()) {
            if (stack.peek().equals(minStack.peek())) {
                minStack.pop();
            }
            stack.pop();
        }
    }

    public int top() {
        return stack.isEmpty() ? -1 : stack.peek();
    }

    public int getMin() {
        return minStack.isEmpty() ? -1 : minStack.peek();
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        Scanner sc=new Scanner(System.in);
        List<String> operations=new ArrayList<>();
        List<Integer> elements=new ArrayList<>();

        System.out.println("Enter list of operations (end by typing end):");
        int countOfElements=0;
        while(true){
            String s=sc.next();
            if(s.equals("end")){
                break;
            }
            if(s.equals("push")){
                countOfElements++;
            }
            operations.add(s);
        }
        System.out.println("Enter elements to push");
        for(int i=0;i<countOfElements;i++){
            elements.add(sc.nextInt());
        }
        int index=0;

        System.out.println("\nResult:");
        for(String str:operations){
            switch(str){
                case "push":
                    minStack.push(elements.get(index));
                    index++;
                    break;
                case "pop":
                    minStack.pop();
                    break;
                case "top":
                    System.out.println(minStack.top());
                    break;
                case "getMin":
                    System.out.println(minStack.getMin());
                    break;

            }
        }
        
        
        
    }
}

