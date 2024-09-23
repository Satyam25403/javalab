import java.util.Scanner;

class NextHeighestTemperature{
    //own
    public static void main(String[] args) {
        //take input array

        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int ans[]=new int[n];
        int a[]=new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        boolean found;
        
        //iterate over outer loop
        for(int i=0;i<a.length;i++){
            //set count=0 for every member in the array and count number of steps needed to counter a higher number
            int count=0;found=false;
            for(int j=i+1;j<a.length;j++){
                count++;
                if(a[j]>a[i]){
                    found=true;
                    break;
                }
            }
            if(found){
                ans[i]=count;
            }
            else{
                ans[i]=0;
            }
        }
        for(int i:ans){
            System.out.println(i);
        }
    }
}
