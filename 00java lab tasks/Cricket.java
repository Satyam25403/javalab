import java.util.Scanner;
class IPLcricket{
    int m,n,o,p,q,r;
    IPLcricket(int m1,int n1,int o1,int p1,int q1,int r1){
        m=m1;n=n1;o=o1;p=p1;q=q1;r=r1;
        //in order: total,won,lost
    }
    void played_matches(){
        System.out.println("total matches played by two teams: "+(m+p));
    }
    void win(){
        System.out.println("total won matches won by both the teams: "+(n+q));
    }
    void loss(){
        System.out.println("total won matches lost by both the teams: "+(o+r));
    }
}
class Sunrisers extends IPLcricket{
    Sunrisers(int m1,int n1,int o1,int p1,int q1,int r1){
        super(m1,n1,o1,p1,q1,r1);
    }
    void display(){
        System.out.println("hyderabad sunrisers team summary: ");
        System.out.println("played matches= "+m);
        if(n+o==m){
            System.out.println("won: "+n);
            System.out.println("lost: "+o);
        }
        else{
            System.out.println("invalid summary");
        }
    }
}
class Csk extends IPLcricket{
    Csk(int m1,int n1,int o1,int p1,int q1,int r1){
        super(m1,n1,o1,p1,q1,r1);
    }
    void display(){
        System.out.println("csk sunrisers team summary: ");
        System.out.println("played matches= "+p);
        if(q+r==p){
            System.out.println("won: "+q);
            System.out.println("lost: "+r);
        }
        else{
            System.out.println("invalid summary");
        }
    }
}
public class Cricket {
    public static void main(String args[]){
        Scanner a=new Scanner(System.in);
        System.out.println("enter numbers each in different line:");
        int m=a.nextInt();
        int n=a.nextInt();
        int o=a.nextInt();
        int p=a.nextInt();
        int q=a.nextInt();
        int r=a.nextInt();
        IPLcricket i=new IPLcricket(m, n, o, p, q, r);
        i.played_matches();
        i.win();
        i.loss();
        Sunrisers ob2=new Sunrisers(m, n, o, p, q, r);
        Csk ob3=new Csk(m, n, o, p, q, r);
        ob2.display();
        ob3.display();
        a.close();
    }
}
