package bankemployeesystem.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Binary tree that represents the bank's employee hierarchy.
 * Nodes are inserted in level order (breadth-first): fill left, then right,
 * then move to the next node in the same level.
 */
public class EmployeeBinaryTree {

    private TreeNode root;

    /**
     * Inserts an employee in the first free position, in level order.
     * A queue visits the nodes level by level, from left to right.
     */
    public void insert(Employee employee) {
        TreeNode newNode = new TreeNode(employee);

        if (root == null) {
            root = newNode; // the first employee becomes the top of the hierarchy
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            if (current.getLeft() == null) {
                current.setLeft(newNode);  // fill left first
                return;
            }
            queue.add(current.getLeft());

            if (current.getRight() == null) {
                current.setRight(newNode); // then right
                return;
            }
            queue.add(current.getRight());
            // both children taken: the loop moves to the next node in level order
        }
    }

    /**
     * Level-order traversal.
     * Returns one list per level: index 0 = root level, index 1 = its children, and so on.
     */
    public List<List<Employee>> levelOrder() {
        List<List<Employee>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int nodesInLevel = queue.size(); // everything in the queue now is the same level
            List<Employee> level = new ArrayList<>();

            for (int i = 0; i < nodesInLevel; i++) {
                TreeNode current = queue.poll();
                level.add(current.getEmployee());
                if (current.getLeft() != null) {
                    queue.add(current.getLeft());
                }
                if (current.getRight() != null) {
                    queue.add(current.getRight());
                }
            }
            levels.add(level);
        }
        return levels;
    }

    /** Height = number of levels in the tree (empty tree = 0, only the root = 1). */
    public int getHeight() {
        return height(root);
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.getLeft()), height(node.getRight())); // recursive
    }

    /** Total number of nodes (employees) in the tree. */
    public int getNodeCount() {
        return count(root);
    }

    private int count(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + count(node.getLeft()) + count(node.getRight()); // recursive
    }
}