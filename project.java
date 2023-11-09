import java.util.*;
class Timetable{
    int n;
    String[] p=new String[8];   //periods
    String[] subj=new String[]{"20BS3101A","20ES3102","20CS3303","20CS3304","20CS3305","20ES3151","20CS3352",
                    "20CS3353","20TP3106","20MC3107A","ELITE CLASS"};
}
class Cse1 extends Timetable{
    int[] period_count=new int[]{5,5,8,8,5,5};  //6days....number of periods on each day
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
    int[] period_count=new int[]{5,5,8,8,5,5};      //6days....number of periods on each day
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
    int[] period_count=new int[]{5,8,5,8,5,5};      //6days....number of periods on each day
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
    void getinfo(){
        System.out.println("enter your section(csea,cseb or csec):");
        String sec=sc.nextLine();
        if(sec.endsWith("a")){
            Cse1 c=new Cse1();
        }
        else if(sec.endsWith("b")){
            Cse2 c=new Cse2();
        }
        else if(sec.endsWith("c")){
            Cse3 c=new Cse3();
        }
        System.out.println("input working days");
        
        
    }
}
public class project {
    public static void main(String args[]){
        Cse2 c=new Cse2();
        c.monday();
        c.tuesday();
        c.wednesday();
        c.thursday();
        c.friday();
        c.saturday();
        for(int i=0;i<6;i++){
            System.out.print("| "+c.period_count[i]+"\t");
        }
    }
}
