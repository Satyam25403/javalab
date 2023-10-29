import java.util.*;
class Customer{
    String Bankname;
    double depositAmount;
    int noOfMonths;
    public Customer(String bankName1,double deposit1,int num){
        Bankname=bankName1;
        depositAmount=deposit1;
        noOfMonths=num;
    }
    public double calculateInterest(RBI bank){
        //passing objects as parameters to methods
        return bank.calculateInterest(depositAmount,noOfMonths);
    }
}
class RBI{
    public double calculateInterest(double depositAmount,int noOfMonths){
        return 0.0;
    }
}
class SBI extends RBI{
    public double calculateInterest(double depositAmount,int noOfMonths){
        double interestrate=4.0;
        return depositAmount*interestrate/100*noOfMonths+depositAmount;
    }
}
class ICICI extends RBI{
    public double calculateInterest(double depositAmount,int noOfMonths){
        double interestRate=4.5;
        return depositAmount*interestRate/100*noOfMonths+depositAmount;
    }
}
public class Bank {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("enter bank name: ");
        String bank=sc.next();
        System.out.print("enter deposit amount: ");
        double depo=sc.nextDouble();
        System.out.print("number of months: ");
        int months=sc.nextInt();
        Customer c=new Customer(bank,depo,months);
        RBI bank1=new SBI();     //superclass reference can refer to a subclass object
        System.out.println(c.calculateInterest(bank1));//reference of RBI used as object argument
        sc.close();
    }
}
