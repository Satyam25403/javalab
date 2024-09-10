import java.util.*;
class Faculty {  
     public String name;  
     public String designation;
     public String qualification;
     public String email;
    Faculty(String n, String d,String q,String e){  
          name=n;  
          designation=d;
          qualification=q;
          email=e;
    }  
    public void display(){  
          System.out.printf(" %-30s  %-30s  %-30s %-30s \n",name,designation,qualification,email);
          System.out.println();  
    }  
} 

class Portal{
    Scanner sc=new Scanner(System.in);
    Faculty[] obj = new Faculty[20]; 
    obj[0] = new Faculty("Dr.D.Rajeswara Rao","Professor& HOD","M.Tech,Ph.D.","hodcse@vrsiddhartha.ac.in");  
    obj[1] = new Faculty("Dr.G.Kranthi kumar","Sr.Assistant Professor","M. Tech, Ph.D.","kranthi@vrsiddhartha.ac.in");  
    obj[2] = new Faculty("Dr.S.Rajesh","Assistant Professor","M.Tech,(Ph.D)","srajesh@vrsiddhartha.ac.in");  
    obj[3] = new Faculty("Dr.K.Suvarna Vani","Professor(AI & ML)","M. Tech, Ph.D,PDF","suvarnavanik@vrsiddhartha.ac.in");  
    obj[4] = new Faculty("Dr.K.L.Sailaja","Assistant Professor","M. Tech, Ph.D","sailajak@vrsiddhartha.ac.in");
    obj[5] = new Faculty("Dr.J V D.Prasad","Assistant Professor","M. Tech( Ph.D.)","prasadj@vrsiddhartha.ac.in");  
    obj[6] = new Faculty("Mr.A.Raghu Vira Pratap","Assistant Professor","M. Tech(Ph.D)","pratapadimulam@vrsiddhartha.ac.in");  
    obj[7] = new Faculty("Dr.Ch.Mukesh","Assistant Professor","MS,(Ph.D)","mukrsh.chinta@vrsiddhartha.ac.in");  
    obj[8] = new Faculty("Mr.N.Sunny","Assistant Professor","M. Tech (Ph.D)","sunny@vrsiddhartha.ac.in");  
    obj[9] = new Faculty("Mrs.Ch.Raga Madhuri","Assistant Professor","M. Tech(Ph.D)","chragamadhuri@vrsiddhartha.ac.in");  
    obj[10] = new Faculty("Mr.Prabu.U","Assistant Professor","M. Tech(Ph.D)","prabu@vrsiddhartha.ac.in");
    obj[11] = new Faculty("Dr.Ashutosh Stapathy","Assistant Professor","M. Tech, Ph.D","ashutosh@vrsiddhartha.ac.in");
    obj[12] = new Faculty("Mrs.K.Keerthi","Assistant Professor","M. Tech(Ph.D)","kkeerthi@vrsiddhartha.ac.in");  
    obj[13] = new Faculty("Ms.P.Yasaswini","Assistant Professor(AI & DS)","M. Tech","yasaswini@vrsiddhartha.ac.in");  
    obj[14] = new Faculty("Dr.N.Krishna Santosh","Assistant Professor(AI & Ml)","M. Tech, Ph.D.","krishnasantosh@vrsiddhartha.ac.in");  
    obj[15] = new Faculty("Ms.V.Deepa","Assistant Professor","M. Tech(Ph.D)","deepa@vrsiddhartha.ac.in");
    
    System.out.printf(" %-30s  %-30s  %-30s  %-30s \n","NAME","DESIGNATION","QUALIFICATION","EMAIL");
    System.out.println();  
    for(int i=0;i<16;i++){
        obj[i].display();  
    }

} 


