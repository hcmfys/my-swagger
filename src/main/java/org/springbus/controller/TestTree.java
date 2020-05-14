package org.springbus.controller;

import java.util.LinkedList;
import java.util.List;

public class TestTree {

    public List<TreeNode> generateTrees(int n) {
        if(n == 0)
            return new LinkedList<TreeNode>();
        return generateTrees(1,n);
    }
    public List<TreeNode> generateTrees(int start,int end) {
        List<TreeNode> res = new LinkedList<TreeNode>();
        if(start > end){
            res.add(null);
            return res;
        }
        for(int i = start;i <= end;i++){
            List<TreeNode> subLeftTree = generateTrees(start,i-1);
            List<TreeNode> subRightTree = generateTrees(i+1,end);
            for(TreeNode left : subLeftTree){
                for(TreeNode right : subRightTree){
                    TreeNode node = new TreeNode(i);
                    node.left = left;
                    node.right = right;
                    res.add(node);
                }
            }
        }
        return res;
    }
    public static void main(String[] args) {
      List<TreeNode> t=  new TestTree().generateTrees(9);

        System.out.println(t.size());

        t.stream().forEach( item->{
           // System.out.println("__________________");
            //TreePrintUtil.pirnt(item);

        });
    }


}
