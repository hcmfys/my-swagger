package org.springbus.cg;

// MyObject.java
//程序3-2

import java.io.*;

public class MyObject extends Object implements Serializable, Cloneable {

	// 定数定義だけのクラス
	final static double EPSILON = 1.0E-10;//十分小さい正の実数
	final static double DELTA = 0.01; //1%の許容誤差バウンディングボックス
	final static double HUGE = 1.0E31;//十分大きい正の実数
	final static double HUGEREAL = 1.0E30;//十分大きい正の実数
	final static double LARGE = 1.0E20;//かなり大きい正の実数
	final static int COLORLEVEL = 255;//カラーのレベル（８ビット）
	final static double LITTLE = 1.0E-6;//小さい数（レイトレーシングで使用）

}
