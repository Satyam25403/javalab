import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.ArrayList;
class TreeNode{
    int val;
    TreeNode left,right;
    TreeNode(int val){
        this.val=val;
    }
}
public class IsValidBinarySearchTree {
    static TreeNode constructTree(Integer[] arr){
        if(arr.length==0||arr[0]==null){
            return null;
        }
        TreeNode root=new TreeNode(arr[0]);
        //use a queue to keep track of the nodes while constructing a tree
        Queue<TreeNode> q=new LinkedList<>();
        q.add(root);

        //start from second element
        int i=1;
        while(i<arr.length){
            TreeNode curr=q.poll();
            //contruct left child if available
            if(i<arr.length && arr[i]!=null){
                curr.left=new TreeNode(arr[i]);
                q.add(curr.left);
            }
            i++;
            //construct right child if available
            if(i<arr.length && arr[i]!=null){
                curr.right=new TreeNode(arr[i]);
                q.add(curr.right);
            }
            i++;
        }
        return root;
    }
    
    //if inorder traversal contains whole ascending order elements,it is valid tree
    static void inorder(TreeNode root,ArrayList<Integer> a){
        if(root!=null){
            inorder(root.left,a);
            a.add(root.val);
            inorder(root.right,a);
        }
    }
    static boolean isValidBST(TreeNode root){
        ArrayList<Integer> a=new ArrayList<>();
        inorder(root,a);
        for(int i=0;i<a.size()-1;i++){
            if(a.get(i)>=a.get(i+1)){
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        System.out.println("enter number:");
        int n=sc.nextInt();
        Integer[] arr=new Integer[n];
        System.out.println("enter array:");
        for(int i=0;i<n;i++){
            String in=sc.next();
            if(in.equals("null")){
                arr[i]=null;
            }
            else{
                arr[i]=Integer.parseInt(in);
            }
        }
        TreeNode root=constructTree(arr);
        
        System.out.println("Is Binary search tree: " + isValidBST(root)); 
    }
}
