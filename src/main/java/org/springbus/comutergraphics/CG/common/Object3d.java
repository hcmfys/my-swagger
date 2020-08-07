package org.springbus.comutergraphics.CG.common;
// Object3d.java
//抽象类
//每个方法在此类的子类中定义一个实体。
//程序3-4

public abstract class Object3d extends MyObject {

	public abstract void draw(Camera c, ObjectNode node);
	public abstract void getNearerIntersection(Ray ray, ObjectNode node);
	public abstract void setNearestIntersection(Ray ray);

}
