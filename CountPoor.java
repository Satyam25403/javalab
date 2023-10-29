import java.util.*;
class Family{
    double income;
    int size;
    Family(double income,int size){
        this.income=income;
        this.size=size;
    }
    public boolean isPoor(double housecost,double foodcost){
        if((housecost+foodcost*this.size)>this.income/2)
            return true;
        return false;
    }
    public void tostring(){
        System.out.println("Income of the family is "+this.income);
        System.out.println("size of family is "+this.size);
    }
}
public class CountPoor {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int count=0;
        System.out.println("enter the number of families:");
        int k=sc.nextInt();
        Family[] fam=new Family[k];
        for(int i=0;i<k;i++){
            System.out.println("enter the income of the family:");
            double income=sc.nextDouble();
            System.out.println("Enter the size of family:");
            int size=sc.nextInt();
            fam[i]=new Family(income,size);
        }
        System.out.println("enter housing cost:");
        double hc=sc.nextDouble();
        System.out.println("enter the food cost:");
        double fc=sc.nextDouble();
        for(int i=0;i<k;i++){
            if(fam[i].isPoor(hc,fc)){
                fam[i].tostring();
                count++;
            }
        }
        System.out.println("poor families:"+count);
    }
}
