import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
        next=null;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

public class SortedListToBST {
    public static TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }

        // Find the middle element of the linked list
        ListNode mid = findMiddle(head);

        // Create the root of the BST
        TreeNode root = new TreeNode(mid.val);

        // Recursively build left and right subtrees
        if (head != mid) {
            root.left = sortedListToBST(head);
        }
        root.right = sortedListToBST(mid.next);

        return root;
    }

    static ListNode findMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = null;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        // Disconnect the left half from the middle
        if (prev != null) {
            prev.next = null;
        }

        return slow;
    }

    public static void levelOrderTraversal(TreeNode root){
        if(root==null){
            return;
        }
        Queue<TreeNode> q=new LinkedList<>();
        q.add(root);

        while(!q.isEmpty()){
            TreeNode curr=q.poll();
            System.out.print(curr.toString());

            if(curr.left!=null){
                q.add(curr.left);
            }
            if(curr.right!=null){
                q.add(curr.right);
            }

        }
    }

    public static void main(String[] args) {
        // Example sorted linked list: -10 -> -3 -> 0 -> 5 -> 9
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter number of elements:");
        int n=sc.nextInt();
        System.out.println("Enter head node:");
        ListNode head=new ListNode(sc.nextInt());
        ListNode temp=head;
        System.out.println("Enter rest of nodes:");
        for(int i=0;i<n-1;i++){
            ListNode node=new ListNode(sc.nextInt());
            temp.next=node;
            temp=temp.next;
        }
        TreeNode root =sortedListToBST(head);
        System.out.println("Height-balanced BST (levelorder traversal):");
        levelOrderTraversal(root);
    }
}
