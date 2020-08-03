package org.springbus.cg;


// Vertex4.java
// 同次座標で表現された4次元座標値のクラス

public class Vertex4 extends MyObject {

	public double x,y,z,w;

	//コンストラクタ
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

	// Vertex4の印刷
	public void print(){
		System.out.println("Vertex4 = ("+x+","+y+","+z+","+w+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z,w)=("+x+","+y+","+z+","+w+")");
	}
}