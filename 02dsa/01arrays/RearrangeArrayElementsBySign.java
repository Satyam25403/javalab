//given an array with equal number of positives and negatives, rearrange the elements by sign... i.e. alternatie positives and negatives

import java.util.ArrayList;
import java.util.Scanner;

public class RearrangeArrayElementsBySign {
    static void bruteForceSolution(int a[]) {
        // O(n)+O(n/2) time complexity, O(n) extra space
        int[] pos=new int[a.length/2];int posIdx=0;
        int[] neg=new int[a.length/2];int negIdx=0;
        for (int i = 0; i < a.length; i++) {
            if(a[i]>0){
                pos[posIdx]=a[i];
                posIdx++;
            }else{
                neg[negIdx]=a[i];
                negIdx++;
            }
        }
        //arrange alternately
        for(int i=0;i<a.length/2;i++){
            a[2*i]=pos[i];
            a[2*i+1]=neg[i];
        }

        for(int num:a){
            System.out.print(num+" ");
        }
    }

    static void levelOneOptimization(int a[]) {
        // 
        int[] ans=new int[a.length];
        int posIdx=0,negIdx=1;
        for (int i = 0; i < a.length; i++) {
            if(a[i]>0){
                ans[posIdx]=a[i];
                posIdx+=2;
            }else{
                ans[negIdx]=a[i];
                negIdx+=2;
            }
        }

        for(int num:ans){
            System.out.print(num+" ");
        }
    }

    

    static void secondVarietySolution(int[] a){
        //in this we are not sure that positive and negative elemnets are equal in number: in this case, we fallback to bruteforce solution
        //collect positives and negatives in their order...arrange alternatively till one group exhausts, join remaining elements at last
        //O(n)+O(min{pos,neg})+O(leftovers)=>O(n)+O(n)=>O(2n) time complexity
        //O(n) extra space
        ArrayList<Integer> pos=new ArrayList<>();
        ArrayList<Integer> neg=new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            if(a[i]>0){
                pos.add(a[i]);
            }else{
                neg.add(a[i]);
            }
        }
        if(pos.size()>neg.size()){
            //this ensures 2*neg.size() number of elements were filled
            for(int i=0;i<neg.size();i++){
                a[2*i]=pos.get(i);
                a[2*i+1]=neg.get(i);
            }

            int index=neg.size()*2;
            //add remaining elements from pos arrlist
            for(int i=neg.size();i<pos.size();i++){
                a[index]=pos.get(i);
                index++;
            }
        }else{
            //this ensures 2*pos.size() number of elements were filled
            for(int i=0;i<pos.size();i++){
                a[2*i]=pos.get(i);
                a[2*i+1]=neg.get(i);
            }

            int index=pos.size()*2;
            //add remaining elements from neg arrlist
            for(int i=pos.size();i<neg.size();i++){
                a[index]=neg.get(i);
                index++;
            }
        }
        for(int num:a){
            System.out.print(num+" ");
        }

    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int a[] = new int[7];
        System.out.println("Enter array:");
        for (int i = 0; i < 7; i++) {
            a[i] = sc.nextInt();
        }

        bruteForceSolution(a);
        levelOneOptimization(a);
        secondVarietySolution(a);
    }
}
