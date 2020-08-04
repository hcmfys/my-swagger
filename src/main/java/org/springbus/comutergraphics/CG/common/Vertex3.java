package org.springbus.comutergraphics.CG.common;


// Vertexr3类
// 3D顶点坐标的保持类
//程序3-3
// Vertex3类的构造方法

public class Vertex3 extends MyObject {

	public double x,y,z;

	//构造函数
	public Vertex3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vertex3(){
		x = y = z = 0.0;
	}
	public Vertex3(Vertex3 v){
		this(v.x,v.y,v.z);
	}

	// 两个顶点位置之间的欧式距离
	public static double distance(Vertex3 a, Vertex3 b){
		double t = (a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)+(a.z-b.z)*(a.z-b.z);
		return Math.sqrt(t);
	}

	//加算
	public void add(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
	public void add(Vertex3 v){
		add(v.x,v.y,v.z);
	}

	//打印顶点坐标
	public void print(){
		System.out.println("Vertex3 = ("+x+","+y+","+z+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z)=("+x+","+y+","+z+")");
	}
}
