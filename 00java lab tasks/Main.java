class Degree{
    void getDegree(){
        System.out.println("I got a degree");
    }
}
class underGraduate extends Degree{
    void getDegree(){
        System.out.println("i am undergraduate");
    }
}
class postGraduate extends Degree{
    void getDegree(){
        System.out.println("i am postgraduate");
    }
}
public class Main {
    public static void main(String args[]){
        Degree d=new Degree();
        underGraduate u=new underGraduate();
        postGraduate p=new postGraduate();
        d.getDegree();
        u.getDegree();
        p.getDegree();
    }
}
