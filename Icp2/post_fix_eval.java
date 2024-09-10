import java.util.Stack;
import java.util.StringTokenizer;

class post_fix_eval {
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
        StringTokenizer s = new StringTokenizer("10 6 9 3 + -11 * / * 17 + 5 +");
        String token;
        int a, b;
        
        while (s.hasMoreTokens()) {
            token = s.nextToken();
            if (token.length() == 1 && "+-*/".indexOf(token.charAt(0)) != -1) {
        
                a = stack.pop();
                b = stack.pop();
                stack.push(applyOperator(token,b,a));
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
        System.out.println(stack.pop());
    }
}
