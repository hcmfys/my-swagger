package org.springbus.comutergraphics.CG.common;

// Vector4.java
//由齐次坐标表示的4D矢量类别

public class Vector4 extends MyObject {

	public double x,y,z,w;

	//コンストラクタ
	public Vector4(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Vector4(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}
	public Vector4(){
		x = y = z = w = 0.0;
	}
	public Vector4(Vector4 v){
		this(v.x,v.y,v.z,v.w);
	}

	// ベクトルの正規化　（大きさを1にすること）
	public Vector4 normalize() throws DivideByZeroException {
		double t = x*x + y*y + z*z + w*w;
		t = Math.sqrt(t);
		if (Math.abs(t) < EPSILON)
			throw new DivideByZeroException();
		t = 1.0/t;
		Vector4 u = new Vector4();
		u.x = x * t;
		u.y = y * t;
		u.z = z * t;
		u.w = w * t;
		return u;
	}

	// ベクトルの内積
	public double innerProduct(Vector4 a, Vector4 b){
		double t =
			a.x*b.x + a.y*b.y + a.z*b.z + a.w*b.w;
		return t;
	}

	// ベクトルの印刷
	public void print(){
		System.out.println("Vector4 = ("+x+","+y+","+z+","+w+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z,w)=("+x+","+y+","+z+","+w+")");
	}
}
