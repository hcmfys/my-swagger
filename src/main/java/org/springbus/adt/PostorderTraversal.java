package org.springbus.adt;

import org.springbus.controller.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 后序遍历
 */
public class PostorderTraversal {

    private List<Integer> result = new ArrayList<Integer>();

    /**
     * 迭代实现，使用栈实现，出栈得到节点顺序为根右左，每次向list最开头插入元素
     * @param root
     * @return
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        if(root == null)
            return result;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);   //首先将根节点压栈0
        while(!stack.isEmpty()) {
            TreeNode ele = stack.pop(); //首先出栈的为根节点，其后先出右子节点，后出左子节点
            if(ele.left != null)
                stack.push(ele.left);  //将左子节点压栈
            if(ele.right != null) {
                stack.push(ele.right); //将右子节点压栈
            }
            result.add(0, ele.val); //因为出栈顺序为“根右左”，所以需要每次将元素插入list开头
        }
        return result;
    }
}
