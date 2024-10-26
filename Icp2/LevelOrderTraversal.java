import java.util.ArrayList;
import java.util.LinkedList;
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
class LevelOrderTraversal{
    public static List<List<Integer>> levelOrderTraversal(TreeNode root) {
        List<List<Integer>> res=new ArrayList<>();
        if(root==null){
            return res;
        }
        Queue<TreeNode> lev=new LinkedList<>();
        lev.add(root);
        while(!lev.isEmpty()){
            int levelSize=lev.size();
            List<Integer> levelNodes=new ArrayList<>();
            for(int i=0;i<levelSize;i++){
                TreeNode curr=lev.poll();
                if(curr!=null){
                    levelNodes.add(curr.val);
                    if(curr.left!=null){
                        lev.add(curr.left);
                    }
                    if(curr.right!=null){
                        lev.add(curr.right);
                    }
                }
            }
            res.add(levelNodes);
            
        }
        return res;
        
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
        
        List<List<Integer>> res=levelOrderTraversal(root);      
        System.out.println(res);
    }
}