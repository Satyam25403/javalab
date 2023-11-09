import java.io.*;
class Invoice {
    BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
    String part_num,part_des;
    int num_of_items;
    double amount,price_of_items;
    Invoice(){
        part_num="100";part_des="Charger";num_of_items=0;price_of_items=1000;
    }
    //getters
    public String get_part_num()throws IOException{
        System.out.println("enter the part number: ");
        part_num=b.readLine();
        return part_num;
    }
    public String get_part_des()throws IOException{
        System.out.println("enter the part description: ");
        part_des=b.readLine();
        return part_des;
    }
    public int get_num_of_items()throws IOException{
        System.out.println("enter the number of items: ");
        num_of_items=Integer.parseInt(b.readLine());
        return num_of_items;
    }
    public double get_price_of_items()throws IOException{
        System.out.println("enter price of items: ");
        price_of_items=Double.parseDouble(b.readLine());
        return price_of_items;
    }
    //setters
    public String set_part_num(String num){
        part_num=num;
        return part_num;
    }
    public String set_part_des(String des){
        part_des=des;
        return part_des;
    }
    public int set_num_of_items(int num) {
        num_of_items=num;
        return num_of_items;
    }
    public double set_price_of_items(double price){
        price_of_items=price;
        return price_of_items;
    }
    public double getInvoiceAmount(){
        amount=price_of_items*num_of_items;
        amount=(amount>0)?amount:0;
        return amount;
    }
    public void displayInfo(){
        System.out.println("Part number: "+part_num+"\npart description: "+part_des+"\nNumber of items: "+num_of_items);
        System.out.println("price of items: "+price_of_items+"\nAmount: "+amount);
    }
}
public class InvoiceTest{
    public static void main(String args[])throws IOException{
        Invoice i1=new Invoice();
        Invoice i2=new Invoice();
        i1.get_part_num();
        i2.get_part_des();
        i1.get_num_of_items();
        i1.get_price_of_items();
        i1.getInvoiceAmount();
        i1.displayInfo();
    }
}
