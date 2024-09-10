// Definition for a binary tree node.

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    
    TreeNode() {}
    
    TreeNode(int val) { 
        this.val = val; 
    }
    
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class MaxDepthOfBinaryTree {
    
    // Function to return the maximum depth of the binary tree
    static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0; // Base case: if the node is null, depth is 0
        } else {
            // Recursively find the depth of the left and right subtrees
            int leftDepth = maxDepth(root.left);
            int rightDepth = maxDepth(root.right);
            
            // Return the greater of the two depths, plus 1 for the current node
            return Math.max(leftDepth, rightDepth) + 1;
        }
    }
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
    
    // Main function for testing the code
    public static void main(String[] args) {
        // Example tree:
        //         3
        //        / \
        //       9   20
        //           / \
        //          15  7
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

        int maxDepth = maxDepth(root);
        
        System.out.println("Maximum Depth of the Tree: " + maxDepth); // Output: 3
    }
}

