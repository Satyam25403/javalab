public class PeakElement {
    //own
    public static void main(String[] args) {
        int a[]={1,2,1,3,5,6,4};
        System.out.println(peak(a));

        
    }
    static int peak(int a[]){
        if(a[0]>a[1]){
            return 0;
        }
        for(int i=1;i<=a.length;i++){
            if(isPeak(a[i-1],a[i],a[i+1])){
                return i;
            }
        }
        if(a[a.length-1]>a[a.length-2]){
            return a.length-1;
        }
        return -1;
    }
    static boolean isPeak(int a,int b,int c){
        return (b>a && b>c);
    }
}
