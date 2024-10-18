import java.util.*;
class ValidParantheses  {
    public static boolean isValid(String s) {
        Stack<Character> st=new Stack<>();
        char[] c=s.toCharArray();
        for(int i=0;i<c.length;i++){
            if(c[i]=='('){
                st.push(')');
            }
            else if(c[i]=='{'){
                st.push('}');
            }
            else if(c[i]=='['){
                st.push(']');
            }

            else{
                if(st.isEmpty()||st.pop()!=c[i]){
                    return false;
                }
            }
            
        }if(st.isEmpty()){
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String s=sc.next();
        System.out.println(isValid(s));
    }
}
