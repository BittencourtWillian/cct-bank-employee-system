package bankemployeesystem.model;

/**
 * One node of the employee binary tree.
 * Stores the employee (Name, Manager Type and Department) and links to at most two children.
 */
public class TreeNode {

    private final Employee employee;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}