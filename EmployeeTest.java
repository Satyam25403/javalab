import java.util.*;
class Employee{
    Scanner sc=new Scanner(System.in);
    String fname,lname;
    double salary;
    Employee(){
        fname="";lname="";salary=0.0;
    }
    public void setfname(){
        System.out.println("enter the first name of the employee:");
        fname=sc.next();
    }
    public void setlname(){
        System.out.println("enter the last name of the employee:");
        lname=sc.next();
    }
    public void setsalary(){
        System.out.println("enter monthly salary of employee:");
        salary=sc.nextDouble();
        if(salary<0.0)
            salary=0.0;
    }
    public String getfname(){
        return fname;
    }
    public String getlname(){
        return lname;
    }
    public double getsalary(){
        return salary;
    }
}
public class EmployeeTest {
    public static void main(String args[]){
        Employee emp1=new Employee();
        Employee emp2=new Employee();
        emp1.setfname();
        emp2.setlname();
        emp1.setsalary();
        System.out.println("employee yearly salary:"+emp1.getsalary()*12);
        System.out.println("employee yearly salary after 10% raise:"+emp1.getsalary()*12*1.1);
        emp2.setfname();
        emp2.setlname();
        emp2.setsalary();
        System.out.println("employee yearly salary:"+emp2.getsalary()*12);
        System.out.println("employee yearly salary after 10% raise:"+emp2.getsalary()*12*1.1);
    }
}
