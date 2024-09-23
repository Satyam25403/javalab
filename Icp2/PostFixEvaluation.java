import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

class PostFixEvaluation{
    private static int applyOperator(String operator, int operand1, int operand2) {
        switch (operator) {
            case "+": return operand1 + operand2;
            case "-": return operand1 - operand2;
            case "*": return operand1 * operand2;
            case "/": return operand1 / operand2;
            default: System.out.println("invalid operactor encountered");return -1;
        }
    }
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter space seperated expression:");
        String s1=sc.nextLine();
        StringTokenizer s = new StringTokenizer(s1);
        String token;
        int a, b;
        
        while (s.hasMoreTokens()) {
            token = s.nextToken();
            if (token.length() == 1 && "+-*/".indexOf(token.charAt(0)) != -1) {
                //an operator encountered
                a = stack.pop();
                b = stack.pop();
                stack.push(applyOperator(token,b,a));
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
        //finally pop the end result stored in stack
        System.out.println(stack.pop());
    }
}
