class Student{
    String name,address;
    int age;
    Student(){
        name="unknown";
        age=0;
        address="not available";
    }
    void setInfo(String name1,int age1){
        name=name1;
        age=age1;
    }
    void setInfo(String name1,int age1,String address1){
        name=name1;
        age=age1;
        address=address1;
    }
}
public class Array {
    public static void main(String args[]){
        Student[] s=new Student[3];
        s[0]=new Student();
        s[1]=new Student();
        s[2]=new Student();
        s[0].setInfo("john", 10, "vijayawada");
        s[1].setInfo("sam", 20, "vijayawada");
        s[2].setInfo("ram", 30, "vijayawada");
        for(int i=0;i<s.length;i++){
            System.out.println("student "+i+1+"name = "+s[i].name);
            System.out.println("student "+i+1+"age = "+s[i].age);
            System.out.println("student "+i+1+"address = "+s[i].address);
        }
    }
}