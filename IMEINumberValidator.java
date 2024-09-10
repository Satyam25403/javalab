import java.util.*;
public class IMEINumberValidator {
    public static boolean isValidIMEI(String imei){
        imei=imei.replaceAll("\\D","");
        if(imei.length()!=15){
            return false;
        }
        List<Integer> digits =new ArrayList<>();
        for(int i=0;i<imei.length();i++){
            digits.add(Character.getNumericValue(imei.charAt(i)));
        }
        for(int i=digits.size()-2;i>=0;i-=2){
            int Doubledigit=digits.get(i)*2;
            digits.set(i,Doubledigit>9?Doubledigit-9:Doubledigit);
        }
        int sum=digits.stream().mapToInt(Integer::intValue).sum();
        return sum%10==0;
    }
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        String imei=s.nextLine();
        if(isValidIMEI(imei)){
            System.out.println("valid");
        }
        else{
            System.out.println("invalid");
        }
        s.close();
    }
}
