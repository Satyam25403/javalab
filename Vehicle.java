import java.util.*;
class Automobile{
    String make,Year_Model;
    double mileage,price;
    Automobile(String m,String y,double mge,double p){
        make=m;
        Year_Model=y;
        mileage=mge;
        price=p;
    }
    public String toString(){
        //to print the info
        return "make: "+make+"\nYear Model: "+Year_Model+"\nmileage: "+mileage+"\nprice: "+price;
    }
}
class car extends Automobile{
    car(String m,String y,double mge,double p){
        super(m,y,mge,p);
    }
}
class truck extends Automobile{
    truck(String m,String y,double mge,double p){
        super(m,y,mge,p);
    }
}
class van extends Automobile{
    van(String m,String y,double mge,double p){
        super(m,y,mge,p);
    }
}

public class Vehicle {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("enter 1.car 2.truck 3.van:");
        int choice=sc.nextInt();
        System.out.println("enter make: ");
        String m=sc.next();
        System.out.println("enter year/model(ex:-2019-swift): ");
        String y=sc.next();
        System.out.println("enter mileage: ");
        double mge=sc.nextDouble();
        System.out.println("enter price: ");
        double p=sc.nextDouble();
        int k=Integer.parseInt(y.substring(0, 4));
        if(k<=2000||k>=2030){
            System.out.println("Invalid year/model...");
            sc.close();
            return;
        }
        switch(choice){
            case 1:
                car c=new car(m,y,mge,p);
                System.out.println(c);
                break;
            case 2:
                truck t=new truck(m,y,mge,p);
                System.out.println(t);
                break;
            case 3:
                van v=new van(m,y,mge,p);
                System.out.println(v);
                break;
        }
        sc.close();
    }
}
