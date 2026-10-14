public class Diameter {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    static class TreeInfo {
        int diameter;
        int height;

        public TreeInfo(int diameter, int height) {
            this.diameter = diameter;
            this.height = height;
        }
    }

    static class BinaryTree {
        static int idx = -1;

        public Node createTree(int[] nodes) {
            idx++;

            if (nodes[idx] == -1) {
                return null;
            }

            Node newNode = new Node(nodes[idx]);
            newNode.left = createTree(nodes);
            newNode.right = createTree(nodes);
            return newNode;
        }

        public TreeInfo diameterInfo(Node root) {
            if (root == null) {
                return new TreeInfo(0, 0);
            }

            // find the info of the left subtree ( contains diameter and height )
            TreeInfo leftNodeInfo = diameterInfo(root.left);

            // find the info of the right subtree ( contains diameter and height )
            TreeInfo rightNodeInfo = diameterInfo(root.right);

            // Get the height of the node/tree.
            int NodeHeight = Math.max(leftNodeInfo.height, rightNodeInfo.height) + 1;

            // calculate diameter of the node/tree.
            int leftDiameter = leftNodeInfo.diameter;
            int rightDiameter = rightNodeInfo.diameter;

        }

    }

    public static void main(String[] args) {
        System.out.println("Finding the diameter of Binary Tree");

        int nodes[] = { 1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1 };

        System.out.println("Tree Creation");
        BinaryTree tree = new BinaryTree();
        Node root = tree.createTree(nodes);

        System.out.println("Diameter of the tree is: " + tree.diameter(root));
    }
}
