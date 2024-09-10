
class next_high_temp{
    //own
    public static void main(String[] args) {
        //take input array

        int a[]={73,74,75,71,69,72,76,73};
        boolean found;
        int ans[]=new int[a.length];
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
