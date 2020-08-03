package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// DivideByZeroException.java
// ゼロ割り算例外処理クラス

public class DivideByZeroException extends ArithmeticException {

	// コンストラクタ
	public DivideByZeroException(){
		super("ゼロ割り算発生");
	}
	public DivideByZeroException(String s){
		super(s);
	}
}
