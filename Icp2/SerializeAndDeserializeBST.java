import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

public class  SerializeAndDeserializeBST {
    private static final String DELIMITER = ",";

    // Serialize BST to a string
    static String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }

    static void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#").append(DELIMITER);
            return;
        }
        sb.append(node.val).append(DELIMITER);
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    // Deserialize string to BST
    static TreeNode deserialize(String data) {
        String[] values = data.split(DELIMITER);
        int[] index = {0}; // Mutable index for array traversal
        return deserializeHelper(values, index);
    }

    static TreeNode deserializeHelper(String[] values, int[] index) {
        if (index[0] >= values.length || values[index[0]].equals("#")) {
            index[0]++;
            return null;
        }
        TreeNode node = new TreeNode(Integer.parseInt(values[index[0]]));
        index[0]++;
        node.left = deserializeHelper(values, index);
        node.right = deserializeHelper(values, index);
        return node;
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
    

        String serialized = serialize(root);
        System.out.println("Serialized BST: " + serialized);

        TreeNode deserializedRoot = deserialize(serialized);
        System.out.println("In-order traversal of deserialized BST:");
        printInOrder(deserializedRoot);
    }

    private static void printInOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.print(node.val + " ");
        printInOrder(node.right);
    }
}

