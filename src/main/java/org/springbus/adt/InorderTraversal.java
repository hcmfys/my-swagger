package org.springbus.adt;

import org.springbus.controller.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 中序遍历：
 */
public class InorderTraversal {

    private List<Integer> result = new ArrayList<Integer>();

    /**
     * 迭代实现，首先依次将左子节点全部加入栈中，所以第一个while循环后栈顶元素对应一个子树的最
     * 左子节点，然后将该元素出栈加入list，并判断该元素的遍历该节点的右子树。
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        if(root == null)
            return result;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        do {
            //依次将左节点均加入栈中
            while(root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                root = stack.pop();
                result.add(root.val);
                root = root.right;
            }
        } while(!stack.isEmpty() || root != null);
        return result;
    }
}
