package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Vertexr2 クラス
// 2次元テクスチャー座標の保持クラス

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

	// テクスチャー座標の印刷
	public void print(){
		System.out.println("Vertex2 = ("+x+","+y+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (x,y,z)=("+x+","+y+")");
	}
}
