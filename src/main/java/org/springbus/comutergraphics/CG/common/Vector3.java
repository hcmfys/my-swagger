package org.springbus.comutergraphics.CG.common;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Vector3 クラス
// 3次元ベクトルのクラス

public class Vector3 extends MyObject {

	public double x,y,z;

	//コンストラクタ
	public Vector3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3(){x = y = z = 0.0;}
	public Vector3(Vector3 v){this(v.x,v.y,v.z);}
	public Vector3(Vertex3 v){this(v.x,v.y,v.z);}

	// ゼロベクトルかどうか
	public boolean isZeroVector(){
		if (Math.abs(x) < EPSILON &&
		    Math.abs(y) < EPSILON &&
		    Math.abs(z) < EPSILON) return true;
		return false;
	}

	//　ベクトルの大きさ
	public double size() {
		double t = x*x + y*y + z*z;
		t = Math.sqrt(t);
		return t;
	}

	// ベクトルの大きさの２乗
	public double size2() {
		double t = x*x + y*y + z*z;
		return t;
	}

	// ベクトルの正規化　（大きさを1にすること）
	public void normalize() throws DivideByZeroException {
		double t = size();
	 	if (Math.abs(t) < EPSILON) throw new DivideByZeroException();
		t = 1.0/t;
		x *= t;
		y *= t;
		z *= t;
	}

	// ベクトルの外積
	public static Vector3 crossProduct(Vector3 a, Vector3 b){
		Vector3 c = new Vector3();
		c.x = a.y*b.z - b.y*a.z;
		c.y = a.z*b.x - b.z*a.x;
		c.z = a.x*b.y - b.x*a.y;
		return c;
	}
	public Vector3 crossProduct(Vector3 b){
		Vector3 c = new Vector3();
		c.x = this.y*b.z - b.y*this.z;
		c.y = this.z*b.x - b.z*this.x;
		c.z = this.x*b.y - b.x*this.y;
		return c;
	}

	// ベクトルの内積
	public double innerProduct(Vector3 a){
		double t = this.x*a.x + this.y*a.y + this.z*a.z;
		return t;
	}
	public double innerProduct(double x, double y, double z){
		double t = this.x*x + this.y*y + this.z*z;
		return t;
	}
	public double innerProduct(Vertex3 a){
		double t = this.x*a.x + this.y*a.y + this.z*a.z;
		return t;
	}
	public static double innerProduct(Vector3 a, Vector3 b){
		double t = a.x*b.x+a.y*b.y+a.z*b.z;
		return t;
	}

	// ベクトルのスカラー倍
	public void scale(double t){
		this.x *= t;
		this.y *= t;
		this.z *= t;
	}

	// ベクトルの足し算
	public void add(Vector3 v){
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	public void add(Vertex3 v){
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	// ベクトルの引き算
	public void subtract(Vector3 v){
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	public void subtract(Vertex3 v){
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}

	// ベクトルの代入
	public void assign(Vector3 v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	public void assign(Vertex3 v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	// ベクトルの印刷
	public void print(){
		System.out.println("Vector3 = ("+x+","+y+","+z+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z)=("+x+","+y+","+z+")");
	}

}
