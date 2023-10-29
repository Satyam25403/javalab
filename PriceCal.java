import java.util.Scanner;
class Customer{
    int customernum,qty;
    String customername;
    double price,discount,total,netprice;
    void caldiscount(){
        total=price*qty;
        if(total>=50000){
            discount=total*0.25;
        }
        else if((total>=25000)&&(total<50000)){
            discount=total*0.15;
        }
        else
            discount=total*0.1;
        netprice=total-discount;
    }
    void input(){
        Scanner sc=new Scanner(System.in);;
        System.out.println("enter customer name:");
        customername=sc.nextLine();
        System.out.println("enter customer number:");
        customernum=sc.nextInt();
        System.out.println("enter the price:");
        price=sc.nextDouble();
        System.out.println("enter qty:");
        qty=sc.nextInt();
        sc.close();
    }
    void show(){
        System.out.println("Customer name is:"+customername);
        System.out.println("customer number is:"+customernum);
        System.out.println("total price:"+total);
        System.out.println("discount alloted is:"+discount);
        System.out.println("net price to be paid"+netprice);
    }
}
public class PriceCal {
    public static void main(String args[]){
        Customer c1=new Customer();
        c1.input();
        c1.caldiscount();
        System.out.println("-----------------");
        c1.show();
    }
}
