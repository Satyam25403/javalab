import java.util.*;
class Timetable{
    int n;
    String[] p=new String[8];   //periods
    String[] subj=new String[]{"20BS3101A","20ES3102","20CS3303","20CS3304","20CS3305","20ES3151","20CS3352",
                    "20CS3353","20TP3106","20MC3107A","ELITE CLASS"};
}
class Cse1 extends Timetable{
    int[] period_count=new int[]{5,3,8,6,3,3};  //6days....number of periods on each day
    void monday(){
        int n=5;    //periods on monday
        p[0]=subj[1];p[1]=subj[8];p[2]=subj[0];p[3]=subj[9];p[4]=subj[2];
        System.out.println("MONDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
        
    }
    void tuesday(){
        int n=5;    //periods on tuesday
        p[0]=subj[6];p[1]=subj[6];p[2]=subj[6];p[3]=subj[4];p[4]=subj[0];
        System.out.println("TUESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void wednesday(){
        int n=8;    //periods on wednesday
        p[0]=subj[1];p[1]=subj[4];p[2]=subj[9];p[3]=subj[8];p[4]=subj[2];p[5]=subj[3];p[6]=subj[0];p[7]=subj[1];
        System.out.println("WEDNESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void thursday(){
        int n=8;    //periods on thursday
        p[0]=subj[3];p[1]=subj[4];p[2]=subj[0];p[3]=subj[2];p[4]=subj[1];p[5]=subj[10];p[6]=subj[10];p[7]=subj[10];
        System.out.println("THURSDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void friday(){
        int n=5;    //periods on friday
        p[0]=subj[7];p[1]=subj[7];p[2]=subj[7];p[3]=subj[2];p[4]=subj[3];
        System.out.println("FRIDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void saturday(){
        int n=5;    //periods on saturday
        p[0]=subj[5];p[1]=subj[5];p[2]=subj[5];p[3]=subj[3];p[4]=subj[4];
        System.out.println("SATURDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    
}
class Cse2 extends Timetable{
    int[] period_count=new int[]{5,5,6,4,3,5};      //6days....number of periods on each day
    void monday(){
        int n=5;    //periods on monday
        p[0]=subj[2];p[1]=subj[0];p[2]=subj[4];p[3]=subj[3];p[4]=subj[9];
        System.out.println("MONDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void tuesday(){
        int n=5;    //periods on tuesday
        p[0]=subj[3];p[1]=subj[4];p[2]=subj[0];p[3]=subj[1];p[4]=subj[2];
        System.out.println("TUESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void wednesday(){
        int n=8;    //periods on wednesday
        p[0]=subj[6];p[1]=subj[6];p[2]=subj[6];p[3]=subj[8];p[4]=subj[1];p[5]=subj[0];p[6]=subj[2];p[7]=subj[3];
        System.out.println("WEDNESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void thursday(){
        int n=8;    //periods on thursday
        p[0]=subj[7];p[1]=subj[7];p[2]=subj[7];p[3]=subj[4];p[4]=subj[9];p[5]=subj[10];p[6]=subj[10];p[7]=subj[10];
        System.out.println("THURSDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void friday(){
        int n=5;    //periods on friday
        p[0]=subj[5];p[1]=subj[5];p[2]=subj[5];p[3]=subj[8];p[4]=subj[3];
        System.out.println("FRIDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void saturday(){
        int n=5;    //periods on saturday
        p[0]=subj[1];p[1]=subj[4];p[2]=subj[0];p[3]=subj[1];p[4]=subj[2];
        System.out.println("SATURDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
}
class Cse3 extends Timetable{
    int[] period_count=new int[]{5,6,5,4,5,3};      //6days....number of periods on each day
    void monday(){
        int n=5;    //periods on monday
        p[0]=subj[1];p[1]=subj[8];p[2]=subj[0];p[3]=subj[3];p[4]=subj[2];
        System.out.println("MONDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
        
    }
    void tuesday(){
        int n=8;    //periods on tuesday
        p[0]=subj[5];p[1]=subj[5];p[2]=subj[5];p[3]=subj[3];p[4]=subj[4];p[5]=subj[0];p[6]=subj[1];p[7]=subj[2];
        System.out.println("TUESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void wednesday(){
        int n=5;    //periods on wednesday
        p[0]=subj[4];p[1]=subj[2];p[2]=subj[8];p[3]=subj[3];p[4]=subj[1];
        System.out.println("WEDNESDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void thursday(){
        int n=8;    //periods on thursday
        p[0]=subj[6];p[1]=subj[6];p[2]=subj[6];p[3]=subj[9];p[4]=subj[4];p[5]=subj[10];p[6]=subj[10];p[7]=subj[10];
        System.out.println("THURSDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void friday(){
        int n=5;    //periods on friday
        p[0]=subj[3];p[1]=subj[0];p[2]=subj[4];p[3]=subj[9];p[4]=subj[2];
        System.out.println("FRIDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
    void saturday(){
        int n=5;    //periods on saturday
        p[0]=subj[7];p[1]=subj[7];p[2]=subj[7];p[3]=subj[1];p[4]=subj[0];
        System.out.println("SATURDAY SCHEDULE:");
        for(int i=0;i<n;i++){
            System.out.print("| "+p[i]+"\t");
        }
        System.out.println();
    }
}
class attendance{
    Scanner sc=new Scanner(System.in);
    String[] day=new String[]{"mondays","tuedays","wednesday","thursday","friday","saturday"};
    int[] working=new int[6];
    int[] present=new int[6];
    void getinfo(){
        //attendance calculation
        int flag=1;
        while(flag==1){
            System.out.println("input working days");
            int sum=0;
            for(int i=0;i<6;i++){
                System.out.print("enter number of "+day[i]+" :");
                working[i]=sc.nextInt();
                sum+=working[i];
                System.out.println();
            }
            if(sum<=27 && sum>=0){
                //max number of working days in a month
                int count=0;
                System.out.println("input days attended");
                for(int i=0;i<6;i++){
                    System.out.print("enter number of "+day[i]+" :");
                    present[i]=sc.nextInt();
                    count+=present[i];
                    System.out.println();
                }
                if(count<=sum){
                    flag=0;
                }
                else{
                    System.out.println("Invalid number of days attended>>>please re-enter:");
                    flag=1;
                }
            }
            else{
                System.out.println("Invalid number of working days >>>please re-enter:");
                flag=1;
            }
        } 
    }
    void at_calculation(){
        Scanner sc=new Scanner(System.in);
        String sec=new String();
        System.out.println("enter your section(csea,cseb or csec):");
        sec=sc.nextLine();
        if(sec.endsWith("a")){
            Cse1 c=new Cse1();
            int work=0,attend=0;
            for(int i=0;i<6;i++){
                work+=c.period_count[i]*working[i];
                attend+=c.period_count[i]*present[i];
            }
            System.out.println("your attendance percentage this month: "+(double)(attend*100)/work);
        }
        else if(sec.endsWith("b")){
            Cse2 c=new Cse2();
            int work=0,attend=0;
            for(int i=0;i<6;i++){
                work+=c.period_count[i]*working[i];
                attend+=c.period_count[i]*present[i];
            }
            System.out.println("your attendance percentage this month: "+(double)(attend*100)/work);
        }
        else if(sec.endsWith("c")){
            Cse3 c=new Cse3();
            int work=0,attend=0;
            for(int i=0;i<6;i++){
                work+=c.period_count[i]*working[i];
                attend+=c.period_count[i]*present[i];
            }
            System.out.println("your attendance percentage this month: "+(double)(attend*100)/work);
        }
        else{
            System.out.println("invalid section>>>>re-enter");
            at_calculation();
        }
        sc.close();
    }
}
public class project {
    public static void main(String args[]){
        attendance c=new attendance();
        c.getinfo();
        c.at_calculation();
    }
}

