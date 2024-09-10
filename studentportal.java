
import java.io.*;
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
    String[] day=new String[]{"mondays","tuedays","wednesdays","thursdays","fridays","saturdays"};
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
                    System.out.print("enter number of "+day[i]+" attended :");
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

// class Faculty {  
//     public String name;  
//     public String designation;
//     public String qualification;
//     public String email;
//     Faculty(String n, String d,String q,String e){  
//         name=n;  
//         designation=d;
//         qualification=q;
//         email=e;
//     }  
//     public void display(Faculty o){  

//         System.out.printf(" %-30s  %-30s  %-30s %-30s \n",o.name,o.designation,o.qualification,o.email);
//         System.out.println();  
//     }  
// } 

// class Portal{
//     Faculty[] obj = new Faculty[20];
//     obj[0] = new Faculty("Dr.D.Rajeswara Rao","Professor& HOD","M.Tech,Ph.D.","hodcse@vrsiddhartha.ac.in");  
//     obj[1] = new Faculty("Dr.G.Kranthi kumar","Sr.Assistant Professor","M. Tech, Ph.D.","kranthi@vrsiddhartha.ac.in");  
//     obj[2] = new Faculty("Dr.S.Rajesh","Assistant Professor","M.Tech,(Ph.D)","srajesh@vrsiddhartha.ac.in");  
//     obj[3] = new Faculty("Dr.K.Suvarna Vani","Professor(AI & ML)","M. Tech, Ph.D,PDF","suvarnavanik@vrsiddhartha.ac.in");  
//     obj[4] = new Faculty("Dr.K.L.Sailaja","Assistant Professor","M. Tech, Ph.D","sailajak@vrsiddhartha.ac.in");
//     obj[5] = new Faculty("Dr.J V D.Prasad","Assistant Professor","M. Tech( Ph.D.)","prasadj@vrsiddhartha.ac.in");  
//     obj[6] = new Faculty("Mr.A.Raghu Vira Pratap","Assistant Professor","M. Tech(Ph.D)","pratapadimulam@vrsiddhartha.ac.in");  
//     obj[7] = new Faculty("Dr.Ch.Mukesh","Assistant Professor","MS,(Ph.D)","mukrsh.chinta@vrsiddhartha.ac.in");  
//     obj[8] = new Faculty("Mr.N.Sunny","Assistant Professor","M. Tech (Ph.D)","sunny@vrsiddhartha.ac.in");  
//     obj[9] = new Faculty("Mrs.Ch.Raga Madhuri","Assistant Professor","M. Tech(Ph.D)","chragamadhuri@vrsiddhartha.ac.in");  
//     obj[10] = new Faculty("Mr.Prabu.U","Assistant Professor","M. Tech(Ph.D)","prabu@vrsiddhartha.ac.in");
//     obj[11] = new Faculty("Dr.Ashutosh Stapathy","Assistant Professor","M. Tech, Ph.D","ashutosh@vrsiddhartha.ac.in");
//     obj[12] = new Faculty("Mrs.K.Keerthi","Assistant Professor","M. Tech(Ph.D)","kkeerthi@vrsiddhartha.ac.in");  
//     obj[13] = new Faculty("Ms.P.Yasaswini","Assistant Professor(AI & DS)","M. Tech","yasaswini@vrsiddhartha.ac.in");  
//     obj[14] = new Faculty("Dr.N.Krishna Santosh","Assistant Professor(AI & Ml)","M. Tech, Ph.D.","krishnasantosh@vrsiddhartha.ac.in");  
//     obj[15] = new Faculty("Ms.V.Deepa","Assistant Professor","M. Tech(Ph.D)","deepa@vrsiddhartha.ac.in");
    
//     System.out.printf(" %-30s  %-30s  %-30s  %-30s \n","NAME","DESIGNATION","QUALIFICATION","EMAIL");
//     System.out.println();  
//     for(int i=0;i<16;i++){
//         obj[i].display();  
//     }

// } 
class syllabus{
    String[] subj=new String[]{"20BS3101A.txt","20ES3102.txt","20CS3303.txt","20CS3304.txt","20CS3305.txt","20ES3151.txt","20CS3352.txt",
                    "20CS3353.txt","20TP3106.txt","20MC3107A.txt"};
    void printSyllabus()throws IOException{
        Scanner sc=new Scanner(System.in);
        System.out.println();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<SUBJECTS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println();
        System.out.println("1.20BS3101A D.M.S.                     6.20ES3151  java lab\n");
        System.out.println("2.20ES3102  java programming           7.20CS3352  Digital logic lab\n");
        System.out.println("3.20CS3303  operating system           8.20CS3353  Data structures lab\n");
        System.out.println("4.20CS3304  Digital logic              9.20TP3106  Logic and reasoning\n");
        System.out.println("5.20CS3305  Data structures           10.20MC3107A Environmental studies\n");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.print("select from menu the subject, to display the syllabus:");
        String in=sc.next();
        int op=Character.getNumericValue(in.charAt(0));
        if(op<1 || op>10){
            System.out.println("invalid option :-((  please re-enter");
            printSyllabus();
        }
        String fn=null,s;
        //select the corresponding file name
        for(int i=1;i<=10;i++){
            if(i==op){
                fn=subj[i-1];
            }
        }
        System.out.println();
        System.out.println("Syllabus:");
        BufferedReader bf=new BufferedReader(new FileReader(fn));
        while((s=bf.readLine())!=null){
            System.out.println(s);
        }
        bf.close();
        sc.close();
    }
}

class menu{
    public void display(){
        System.out.println();
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>WELCOME TO VRSEC STUDENT PORTAL<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>>>>>>>>>>>>        DEPARTMENT OF CSE       <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println();
        System.out.println();
        System.out.println("        1.Display timetable                    3.Display faculty information     ");
        System.out.println("        2.Calculate attendance                 4.Display Syllabus                ");
        System.out.println("                                5.Quit                               ");
    }
    public void select(){
        Scanner sc=new Scanner(System.in);
        String sec=new String();
        System.out.println("enter your section(csea,cseb or csec):");
        sec=sc.nextLine();
        if(sec.endsWith("a")){
            Cse1 c=new Cse1();
            c.monday();c.tuesday();c.wednesday();c.thursday();c.friday();c.saturday();
        }
        else if(sec.endsWith("b")){
            Cse2 c=new Cse2();
            c.monday();c.tuesday();c.wednesday();c.thursday();c.friday();c.saturday();
        }
        else if(sec.endsWith("c")){
            Cse3 c=new Cse3();
            c.monday();c.tuesday();c.wednesday();c.thursday();c.friday();c.saturday();
        }
        else{
            sc.close();
            System.out.println("invalid section>>>>re-enter");
            select();
        }
    }

    public void finalcall() throws IOException{
        Scanner s=new Scanner(System.in);
        int choice;
        do{
            display();
            System.out.println("enter your choice:");
            choice=s.nextInt();
            switch(choice){
                case 1:
                    select();
                    break;
                case 2:
                    attendance a=new attendance();
                    a.getinfo();
                    a.at_calculation();
                    break;
                case 3:
                case 4:
                    syllabus p=new syllabus();
                    p.printSyllabus();
                    break;
                case 5:
                    System.out.println("terminating the program:");
                    break;
                default:
            }
        }while(choice!=5);
        s.close();
    }
}
public class studentportal{
    public static void main(String args[])throws IOException{
        menu m=new menu();
        m.finalcall();
    }
}

