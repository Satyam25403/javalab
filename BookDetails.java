import java.util.*;
class Book{
    String name,author;
    void setData(String a,String b){
        name=a;
        author=b;
    }
    void display(){
        System.out.println("Name : "+name);
        System.out.println("Author : "+author);
    }
}
class McGrawHill extends Book{
    int price;
    void price(int p){
        this.price=p;
    }
    void display(){
        System.out.println("McGrawHill: "+price);
    }
}
class Oxford extends Book{
    int price;
    void price(int p){
        this.price=p;
    }
    void display(){
        System.out.println("Oxford: "+price);
    }
}
public class BookDetails {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        Book ob=new Book();
        McGrawHill m=new McGrawHill();
        Oxford o=new Oxford();
        System.out.println("Enter book name: ");
        String n=sc.nextLine();
        System.out.println("Enter author name: ");
        String a=sc.nextLine();
        System.out.println("Enter McGrawHill price: ");
        int p1=sc.nextInt();
        System.out.println("Enter oxford price: ");
        int p2=sc.nextInt();
        ob.setData(n,a);
        ob.display();
        m.price(p1);
        m.display();
        o.price(p2);
        o.display();
        sc.close();
    }
}
