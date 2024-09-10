import java.io.*;
import java.util.*;
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
        int op=sc.nextInt();
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

