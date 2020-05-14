package org.springbus.controller;

import javafx.beans.property.StringProperty;

public class TreeNode {

  int val;

  TreeNode left;
  TreeNode right;

  public TreeNode(int i) {
    this.val=i;
  }

  public TreeNode() {

  }

  public int getVal() {
    return val;
  }

  public void setVal(int val) {
    this.val = val;
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

  public String toString() {
    return "[" + val + "]";
  }
}
