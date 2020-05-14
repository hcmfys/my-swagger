package org.springbus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Full {

  void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  public TreeNode createNode(TreeNode root, int n) {
    if (root == null) {
      return null;
    }
    int i = root.val;
    if (i > n) {
      if (root.left == null) {
        root.left = new TreeNode();
        root.left.val = n;
        return root.left;
      } else {
        return createNode(root.left, n);
      }
    } else {
      if (root.right == null) {
        root.right = new TreeNode();
        root.right.val = n;
        return root.right;
      }
      return createNode(root.right, n);
    }
  }

  List<TreeNode> nodeList = new ArrayList<>();
  HashMap<Integer,List<TreeNode> > nodeMap=new HashMap<>();

  boolean isSame(TreeNode node1, TreeNode node2) {
    if (node1 == null && node2   == null) {
      return true;
    }
    if (node2 == null || node1 == null) {
      return false;
    }

    if (node1.val != node2.val) {
      return false;
    }

      boolean ok1 = isSame(node1.left, node2.left);
      boolean ok2 = isSame(node1.right, node2.right);
      return ok1 && ok2;

  }

  boolean isTheSame( TreeNode node) {
    if(nodeMap.get(node.val)==null) {
      nodeMap.put(node.val,new ArrayList<>());
      nodeMap.get(node.val).add(node);
      return  false;
    }

   List<TreeNode> nodes= nodeMap.get(node.val);
    for( TreeNode n : nodes) {

      if(isSame(n,node)) {
        return  true;
      }
    }
    nodeMap.get(node.val).add(node);
    return false;

  }

  public void exploer(int[] arr, int l, int r) {
    if (l == r) {
      TreeNode rootNode = null;
      for (int i = 0; i <= r; i++) {
        //System.out.print(arr[i] + " ");
        if (i == 0) {
          rootNode = new TreeNode();
          rootNode.val = arr[i];
        } else {
          TreeNode nextNode = createNode(rootNode, arr[i]);

        }
      }
      if (rootNode != null) {
        if(!isTheSame(rootNode))
        nodeList.add(rootNode);
      }
      //System.out.println("");
    } else {
      for (int i = l; i <= r; i++) {
        swap(arr, i, l);
        exploer(arr, l+ 1, r);
        swap(arr, i, l);
      }
    }
  }

  public List<TreeNode> generateTrees(int n) {

    nodeList.clear();
    int a[] = new int[n];
    for (int i = 1; i <= n; i++) {
      a[i - 1] = i;
    }
    exploer(a, 0, a.length-1 );
    return nodeList;
  }

  public static void main(String[] args) {
    int a[] = new int[] {1, 2, 3};
    Full f = new Full();
   // f.exploer(a, 0, a.length - 1);
    List<TreeNode> t = f.generateTrees(9);
    System.out.println(t.size());

    t.stream().forEach( item->{
     // System.out.println("__________________");
      //TreePrintUtil.pirnt(item);

    });
  }
}
