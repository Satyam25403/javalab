import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
class TreeNode{
    int val;
    TreeNode left,right;
    TreeNode(int val){
        this.val=val;
    }
    TreeNode(int val,TreeNode left,TreeNode right){
        this.val=val;
        this.left=left;
        this.right=right;
    }
}
public class ConstructTreeFromTraversals {
    static TreeNode buildTree(int[] preorder,int[] inorder){
        return buildTreeHelper(preorder,inorder,0,0,inorder.length-1);
    }
    static TreeNode buildTreeHelper(int[] preorder,int[] inorder,int preStart,int inStart,int inEnd){
        if(preStart>preorder.length-1 || inStart>inEnd){
            return null;
        }
        TreeNode root=new TreeNode(preorder[preStart]);

        //find root in inorder traversal to split tree
        int inIndex=0;
        for(int i=inStart;i<=inEnd;i++){
            if(inorder[i]==root.val){
                inIndex=i;
                break;
            }
        }

        //recursively build left and right subtrees
        root.left=buildTreeHelper(preorder,inorder,preStart+1,inStart,inIndex-1);
        root.right=buildTreeHelper(preorder,inorder,preStart+(inIndex-inStart)+1,inIndex+1,inEnd);
        return root;
    }
    public static void levelOrderTraversal(TreeNode root){
        if(root==null){
            return;
        }
        Queue<TreeNode> q=new LinkedList<>();
        q.add(root);
        String res="";

        while(!q.isEmpty()){
            TreeNode curr=q.poll();
            if(curr!=null){
                res+=curr.val+",";
                q.add(curr.left);
                q.add(curr.right);
            }
            else{
                res+="null,";
            }
        }
        System.out.println(res);
    }
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("enter number:");
        int n=sc.nextInt();
        int[] preorder=new int[n];
        int[] inorder=new int[n];
        System.out.println("enter preorder array:");
        for(int i=0;i<n;i++){
            preorder[i]=sc.nextInt();
        }
        System.out.println("enter inorder array:");
        for(int i=0;i<n;i++){
            inorder[i]=sc.nextInt();
        }
        TreeNode root=buildTree(preorder, inorder);
        levelOrderTraversal(root);
    }
}
