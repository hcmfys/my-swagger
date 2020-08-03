package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Index3 クラス
// 三角形のインデックスを保持するクラス
//	プログラム３−１
//		Index3コンストラクタ

public class Index3 extends MyObject {

	public int v1, v2, v3;//三角形の頂点インデックス

	// コンストラクタ
	public Index3(int v1, int v2, int v3){ 
		if (v1 < 0 || v2 < 0 || v3 < 0) 
			throw new InternalError("インデックスが不適切です");
		this.v1 = v1; this.v2 = v2; this.v3 = v3;
	}
	public Index3(Index3 v){ this(v.v1,v.v2,v.v3); }

	// インデックスの範囲チェック
	public boolean isOutOfBounds(int min, int max){
		if (v1 < min || v1 > max) return true;
		if (v2 < min || v2 > max) return true;
		if (v3 < min || v3 > max) return true;
		return false;
	}

	// インデックスの印刷
	public void print(){
		System.out.println("Index3 = ("+v1+","+v2+","+v3+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (v1,v2,v3)=("+v1+","+v2+","+v3+")");
	}

}
