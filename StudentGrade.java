import java.util.*;
class Student{
    double mean(int []arr,int p){
        int sum=0;
        for(int i=0;i<arr.length;i++){
            sum+=arr[i];
        }
        double rmean=sum/p;
        return rmean;
    }
    double variance(int []arr,int p,double d){
        double vsum=0;
        for(int i=0;i<p;i++){
            vsum+=((arr[i]-d))*((arr[i]-d));
        }
        double v=vsum/p;
        return Math.sqrt(v);
    }
    int[] stat=new int[10];
    void freq(int[] arr,int p){
        for(int i=0;i<p;i++){
            if(arr[i]>=0 && arr[i]<10){
                stat[0]++;
            }
            else if(arr[i]>=10 && arr[i]<=19){
                stat[1]++;
            }
            else if(arr[i]>=20 && arr[i]<=29){
                stat[2]++;
            }
            else if(arr[i]>=30 && arr[i]<=39){
                stat[3]++;
            }
            else if(arr[i]>=40 && arr[i]<=49){
                stat[4]++;
            }
            else if(arr[i]>=50 && arr[i]<=59){
                stat[5]++;
            }
            else if(arr[i]>=60 && arr[i]<=69){
                stat[6]++;
            }
            else if(arr[i]>=70 && arr[i]<=79){
                stat[7]++;
            }
            else if(arr[i]>=80 && arr[i]<=89){
                stat[8]++;
            }
            else if(arr[i]>=90 && arr[i]<=100){
                stat[9]++;
            }
        }
    }
}



public class StudentGrade {
    public static void main(String args[]){
        Scanner a=new Scanner(System.in);
        System.out.print("enter number of students to be entered:");
        int p=a.nextInt();
        int[] arr=new int[100];
        for(int i=0;i<p;i++){
            System.out.println("enter student grade"+i+":");
            arr[i]=a.nextInt();
        }
        Student gd=new Student();
        double d=gd.mean(arr,p);
        System.out.println("the mean is: "+d);
        double vd=gd.variance(arr,p,d);
        System.out.println("standard dev: "+vd);
        gd.freq(arr,p);
        System.out.println("table:\n");
        for(int i=0;i<10;i++){
            System.out.println("percentage of scores in range "+i*10+" to "+i*10+9+" is "+(100*((double)gd.stat[i]/p)));
        }
        a.close();


    }
    
}

