import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

public class FlipEquivalentBinaryTrees  {
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
    public static void levelOrderTraversal(TreeNode root){
        if(root==null){
            return;
        }
        Queue<TreeNode> q=new LinkedList<>();
        q.add(root);

        while(!q.isEmpty()){
            TreeNode curr=q.poll();
            System.out.print(curr.val+" ");

            if(curr.left!=null){
                q.add(curr.left);
            }
            if(curr.right!=null){
                q.add(curr.right);
            }

        }
    }

    // Function to check if a binary tree is complete
    public static boolean areFlipEquivalent(TreeNode root1,TreeNode root2) {
        if(root1==null && root2==null){
            return true;
        }
        //if 1.)one is null and other is not null      2.)root values dont match
        if(!(root1!=null && root2!=null) || (root1.val!=root2.val)){
            return false;
        }
        return (areFlipEquivalent(root1.left, root2.left)&&areFlipEquivalent(root1.right, root2.right))||(areFlipEquivalent(root1.left, root2.right)&&areFlipEquivalent(root1.right, root2.left));
    }
    public static Integer[] arrayInput(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter a number:");
        int val=sc.nextInt();
        Integer[] arr=new Integer[val];
        System.out.println("enter array:");
        for(int i=0;i<val;i++){
            String in=sc.next();
            if(in.equals("null")){
                arr[i]=null;
            }
            else{
                arr[i]=Integer.parseInt(in);
            }
        }
        return arr;
    }

    public static void main(String[] args) {

        Integer[] arr1=arrayInput();
        Integer[] arr2=arrayInput();
        
        TreeNode root1=constructTree(arr1);TreeNode root2=constructTree(arr2);
        System.out.println("Trees are flip equivalent:"+areFlipEquivalent(root1, root2));

    }
}

