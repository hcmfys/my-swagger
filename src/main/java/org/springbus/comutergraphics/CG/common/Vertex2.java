package org.springbus.comutergraphics.CG.common;

// Vertexr2类
//保持2D纹理坐标的类

public class Vertex2 extends MyObject {

	public double x,y;

	//コンストラクタ
	public Vertex2(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Vertex2(){
		x = y = 0.0;
	}
	public Vertex2(Vertex2 v){
		this(v.x,v.y);
	}

	//打印纹理坐标
	public void print(){
		System.out.println("Vertex2 = ("+x+","+y+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z)=("+x+","+y+")");
	}
}
