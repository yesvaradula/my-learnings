
import java.util.*;

class NodeLeftRight {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    static int idx = -1;

    static Node createTree(int[] arr) {
        idx++;
        if (arr[idx] == -1) {
            return null;
        }

        Node newNode = new Node(arr[idx]);
        newNode.left = createTree(arr);
        newNode.right = createTree(arr);

        return newNode;
    }

    static void preOrder(Node node) {
        if (node == null) {
            return;
        }

        System.out.print(node.data + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    static void inOrder(Node root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);

        System.out.print(root.data + " ");
        inOrder(root.right);
    }

    static void postOrder(Node root) {
        if (root == null) {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);

        System.out.print(root.data + " ");
    }

    static void levelOrder(Node root) {
        if (root == null) {
            return;
        }

        Queue<Node> q = new LinkedList<>();
        q.add(root);
        q.add(null);

        while (!q.isEmpty()) {
            Node currentNode = q.poll();
            if (currentNode == null) {
                System.out.println();
                if (!q.isEmpty()) {
                    q.add(null);
                }
            } else {
                System.out.print(currentNode.data + " ");
                if (currentNode.left != null) {
                    q.add(currentNode.left);
                }

                if (currentNode.right != null) {
                    q.add(currentNode.right);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Tree Creation");

        int[] arr = { 1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1 };

        Node root = createTree(arr);

        System.out.print("Printing Pre Order Traversal : ");
        preOrder(root);
        System.out.println();

        System.out.print("Printing InOrder Traversal : ");
        inOrder(root);
        System.out.println();

        System.out.print("Printing Post Order Traversal : ");
        postOrder(root);
        System.out.println();
    }
}