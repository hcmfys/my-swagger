package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Vertexr3 クラス
// 3次元頂点座標の保持クラス
//	プログラム３−３
//		Vertex3クラスのコンストラクタ

public class Vertex3 extends MyObject {

	public double x,y,z;

	//コンストラクタ
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

	// ２つの頂点位置のユークリッド距離
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

	// 頂点座標の印刷
	public void print(){
		System.out.println("Vertex3 = ("+x+","+y+","+z+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z)=("+x+","+y+","+z+")");
	}
}
