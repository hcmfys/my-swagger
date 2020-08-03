package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// MyObject.java
//	プログラム３−２

import java.io.*;

public class MyObject extends Object implements Serializable, Cloneable {

	// 仅具有常量定义的类
	public final static double EPSILON = 1.0E-10;//十分小さい正の実数
	public final static double DELTA = 0.01; //1％公差范围框
	public final static double HUGE = 1.0E31;//十分大きい正の実数
	public final static double HUGEREAL = 1.0E30;//十分大きい正の実数
	public final static double LARGE = 1.0E20;//かなり大きい正の実数
	public final static int COLORLEVEL = 255;//カラーのレベル（８ビット）
	public final static double LITTLE = 1.0E-6;//小さい数（レイトレーシングで使用）

}
