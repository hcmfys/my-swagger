package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Color3 クラス
// ３次元頂点のカラー値の保持クラス
//	プログラム３−２２
//		Color3クラスのコンストラクタ，clamp(),isNegativeColor(),
//		scale(),assign(),add(),subtract(),multiply()メソッド

public class Color3 extends MyObject {

	public double r,g,b;

	// コンストラクタ
	public Color3(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("色値が不適切です。");
		this.r = r; this.g = g;	this.b = b;
	}
	public Color3(){ this(0,0,0);} //黒色
	public Color3(Color3 c){ this(c.r,c.g,c.b); }

	// 色値の[0-1]空間へのクランプ
	public void clamp(){
		if (this.r < 0.0) this.r = 0.0;
		else if (this.r > 1.0) this.r = 1.0;
		if (this.g < 0.0) this.g = 0.0;
		else if (this.g > 1.0) this.g = 1.0;
		if (this.b < 0.0) this.b = 0.0;
		else if (this.b > 1.0) this.b = 1.0;
	}

	// 色値のチェック
	public boolean isNegativeColor(){
		if (r < 0 || g < 0 || b < 0) return true;
		return false;
	}

	// 色値のスカラー倍
	public void scale(double alpha){
		if (alpha < 0)
			throw new InternalError("色値を負の値でスカラー倍できません");
		this.r *= alpha; this.g *= alpha; this.b *= alpha;
	}

	// 色値の代入
	public void assign(Color3 c){
		if (c.isNegativeColor())
			throw new InternalError("負の値の色は設定できません");
		this.r = c.r; this.g = c.g; this.b = c.b;
	}

	// 色値の加算
	public void add(Color3 c){
		this.r += c.r; this.g += c.g; this.b += c.b;
	}

	// 色値の引き算
	public void subtract(Color3 c){
		this.r -= c.r; this.g -= c.g; this.b -= c.b;
	}

	// 色値の乗算
	public void multiply(Color3 c){
		this.r *= c.r; this.g *= c.g; this.b *= c.b;
	}

	// 色値の印刷
	public void print(){
		System.out.println("Color3 = ("+r+","+g+","+b+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (r,g,b)=("+r+","+g+","+b+")");
	}
}
