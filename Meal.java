
import java.util.*;
public class Meal {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.println("enter the meal cost:");
        double mealCost=sc.nextDouble();
        System.out.println("enter the tax percentage:");
        int taxPercent=sc.nextInt();
        System.out.println("enter the tip percentage:");
        int tipPercentage=sc.nextInt();
        double tip=mealCost*tipPercentage*0.01;
        double tax=mealCost*taxPercent*0.01;
        double total=mealCost+tip+tax;
        int total1=(int)total;      //type conversion
        System.out.println("total cost of the meal:"+total1);
        sc.close();
    }
}
