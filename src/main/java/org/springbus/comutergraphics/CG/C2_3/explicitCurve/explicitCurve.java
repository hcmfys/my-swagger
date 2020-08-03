package org.springbus.comutergraphics.CG.C2_3.explicitCurve;

//本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// explicitCurve.java
// エクシプリシットな曲線描画プログラム（オリジナルのキャンバス上）
//	プログラム２−７

import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.awt.*;//AWTパッケージ利用
import java.applet.Applet;//アプレット利用

public class explicitCurve extends Applet {


	MyCanvas m;//<MyCanvas用の変数
	private double xmin = -6;//描画でのX軸最小値
	private double ymin = -6;//描画でのY軸最小値
	private double xmax = 6;//描画でのX軸最大値
	private double ymax = 6;//描画でのY軸最大値
	private double gDelta = 0.15;//グリッド描画幅

	//MyCanvasクラスのオブジェクト生成
	public void init(){m = new MyCanvas(this);}

	// y = x(x-1)(x+1)　 エクシプリシットな関数例
	private double f(double x){return x*(x-1)*(x+1);}

	public void paint(Graphics g){
		m.setBackground(Color.white);//背景を白色に設定
		m.setColor(Color.black);//前面を黒色に設定
		m.setWindow(-3,3,-3,3);//ユーザ座標系の範囲
		// X軸上のグリッドを描く
		m.drawLine(xmin,0,xmax,0);//X軸描画
		for (int i= (int)xmin; i <= (int)xmax ; i++){
			//X軸上にグリッドと整数文字列を描画
			m.drawLine((double)i,-gDelta,(double)i,gDelta);
			m.drawString(String.valueOf(i),i-gDelta,-0.4);
		}
		// Y軸上のグリッドを描く
		m.drawLine(0,ymin,0,ymax);//Y軸描画
		for (int j = (int)ymin; j <= (int)ymax ; j++){
			//Y軸上にグリッドと整数文字列を描画
			m.drawLine(-gDelta,(double)j,gDelta,(double)j);
			m.drawString(String.valueOf(j),-0.4,j-gDelta);
		}
		// y = x(x-1)(x+1)を描画
		double x = -2.0; // X値の初期化
		double y = f(x); // Y値の初期化
		m.moveTo(x,y);//初期位置に移動
		int step = 80;//Xの値の刻み回数
		double deltax = (2.0 - (-2.0)) / step;//Xの値の刻み幅
		for (int i = 0; i < step ; i++ ){
			x += deltax;//Xを増分
			y = f(x);//Yは関数を評価
			m.lineTo(x,y);//現在位置まで描画
		}
	}
}
