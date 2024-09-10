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

public class CompletenessOfBinaryTree {
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

    // Function to check if a binary tree is complete
    public static boolean isCompleteTree(TreeNode root) {
        if (root == null) return true; // An empty tree is considered complete

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        boolean end = false; // This flag is set when we encounter a null node

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            if (current == null) {
                end = true; // After this point, all nodes should be null
            } else {
                if (end) {
                    // If we have encountered a null node before a non-null node, the tree is not complete
                    return false;
                }
                // Add left and right children to the queue
                queue.offer(current.left);
                queue.offer(current.right);
            }
        }

        return true; // If we never find a non-null node after a null, the tree is complete
    }

    public static void main(String[] args) {
        // Example tree:
        //         1
        //        / \
        //       2   3
        //      / \ /
        //     4  5 6

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
        boolean isComplete = isCompleteTree(root);

        System.out.println("Is the tree complete? " + isComplete); // Output: true
    }
}

