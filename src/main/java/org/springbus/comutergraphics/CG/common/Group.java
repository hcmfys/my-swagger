package org.springbus.comutergraphics.CG.common;

// Group.java
//分组对象的类
//程序3-10

public class Group extends Object3d {

	static int numGroups = 0;
	public Group(){ numGroups++; }// 构造函数
	public int getGroupNumber(){
		return numGroups;
	}

	// ダミーメソッド
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}
