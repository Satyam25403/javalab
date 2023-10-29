public class Max {
    static int max(int a,int b){
        return a>b?a:b;
    }
    static double max(double a,double b){
        return a>b?a:b;
    }
    static double max(double a,double b,double c){
        return max(a,b)>c?max(a,b):c;
    }
    public static void main(String args[]){
        int max1=max(2,6);
        double max2=max(6.4,6.1);
        double max3=max(6.8,8.6,3.78);
        System.out.println(max1);
        System.out.println(max2);
        System.out.println(max3);
    }
}
