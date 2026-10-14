public class LeftNodeRight {

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
    Node root = null;

    static Node createTree(int[] arr) {
        // This method will create a binary tree from an array using in-order traversal.

        idx++;
        if (arr[idx] == -1) {
            return null;
        }

        return null;

    }

    public static void main(String[] args) {
        System.out.println("In-Order Tree Creation");

        int[] nodes = { 1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1 };
        Node root = createTree(nodes);
    }
}
