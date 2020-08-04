package org.springbus.comutergraphics.CG.common;

// Vertex4.java
//以齐次坐标表示的4D坐标值类别

public class Vertex4 extends MyObject {

	public double x,y,z,w;

	//构造函数
	public Vertex4(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Vertex4(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}
	public Vertex4(){
		x = y = z = w = 0.0;
	}
	public Vertex4(Vertex4 v){
		this(v.x,v.y,v.z,v.w);
	}

	// Vertex 4打印
	public void print(){
		System.out.println("Vertex4 = ("+x+","+y+","+z+","+w+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z,w)=("+x+","+y+","+z+","+w+")");
	}
}
