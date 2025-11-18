import java.util.*;
//use of do while loop
public class Vending {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int key,c1,c2,c3,c4;        //count values
        c1=c2=c3=c4=0;
        System.out.println("Your choices are:\n1)Get gum\n2)Get chocolate\n3)Get popcorn\n4)get juice\n5)Display total sold so far\n6)Quit");
        do{
            System.out.println("Enter the key:");
            key=sc.nextInt();
            switch(key){
                case 1:{
                    System.out.println("here is your gum!");
                    c1++;
                    break;
                }
                case 2:{
                    System.out.println("here is your chocolate!");
                    c2++;
                    break;
                }
                case 3:{
                    System.out .println("here is your popcorn!");
                    c3++;
                    break;
                }
                case 4:{
                    System.out.println("here is your juice!");
                    c4++;
                    break;
                }
                case 5:{
                    System.out.println(c1+" items of gum");//all counts declared outside of the loop
                    System.out.println(c2+" items of chocolate");
                    System.out.println(c3+" items of popcorn");
                    System.out.println(c4+" items of juice");
                    break;
                }
                case 6:{
                    System.out.println("terminating thr program!!!");
                    break;
                }
                default:{
                    System.out.println("ERROR:options 1-6 only");
                    break;
                }
            }   
        }
        while(key!=6);
        sc.close();
    }
}
