class RemoveDuplicatesFromSortedArray{
    //O(1) space and O(n) time complexity
    public static void main(String[] args) {
        int[] a={2,2,3,4,4,7,7,7,7,6,9};
        int i,j;
        for(i=0,j=i+1;j<a.length;){
            if(a[i]!=a[j]){
                a[++i]=a[j];
                j++;
            }
            else{
                j++;
            }
        }// for n unique terms,i advances n-1 times
        for(int k=0;k<i+1;k++){
            System.out.println(a[k]);
        }
    }
}