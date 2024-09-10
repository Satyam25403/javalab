import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
class TreeNode{
    int val;
    TreeNode left,right;
    TreeNode(int val){
        this.val=val;
    }
}
public class GoodNodesInBinaryTree {
    static int countGoodNodes(TreeNode root){
        return dfs(root,Integer.MIN_VALUE);
    }
    static int dfs(TreeNode node,int maxSoFar){
        if(node==null){
            return 0;
        }
        int goodNodes=0;
        if(node.val>=maxSoFar){
            goodNodes=1;        //current node is good
        }
        maxSoFar=Math.max(maxSoFar,node.val);
        goodNodes+=dfs(node.left,maxSoFar);
        goodNodes+=dfs(node.right,maxSoFar);

        return goodNodes;
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
        System.out.println(countGoodNodes(root));
    }
}
