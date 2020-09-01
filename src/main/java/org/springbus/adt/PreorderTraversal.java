package org.springbus.adt;

import org.springbus.controller.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 前序遍历
 */
public class PreorderTraversal {

    List<Integer> result = new ArrayList<Integer>();

    /**
     * 迭代实现，维护一个栈，因为入栈顺序按照根右左进行入栈，因此首先将根出栈，然后出栈左子节点，
     * 最后出栈右子节点。
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        if(root == null)
            return result;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            if(node.right != null)
                stack.push(node.right);
            if(node.left != null)
                stack.push(node.left);
        }
        return result;
    }
}
