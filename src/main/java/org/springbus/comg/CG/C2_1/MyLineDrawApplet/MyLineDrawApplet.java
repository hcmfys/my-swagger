package org.springbus.comg.CG.C2_1.MyLineDrawApplet;

// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// MyLineDrawApplet.java
// 線画テスト（オリジナルのキャンバス上）
//	プログラム２−２

import org.springbus.comg.CG.common.MyCanvas;

import java.awt.*;
import java.applet.Applet;

public class MyLineDrawApplet extends Applet {

	MyCanvas m;//MyCanvas用のデータ

	public void init(){ m = new MyCanvas(this); } //MyCanvasオブジェクト生成

	public void paint (Graphics g){
		m.setBackground(new Color(200,200,200));
		m.setColor(Color.black);//前面色は黒色にする
		m.setWindow(-1,1,-1,1);//ユーザ座標系の範囲の設定
		// Ｘ軸を描く
		double[] x1 = { 0.95, 1.0, 0.95 };//矢印の座標値
		double[] y1 = { -0.03, 0, 0.03 };//矢印の座標値
		m.drawLine(-1,0,1,0);//X軸の描画
		m.fillPolygon(x1,y1,3);//矢印の塗りつぶし
		m.drawString("X軸",0.8,-0.1);//X軸という文字列を描画
		// Ｙ軸を描く
		double[] x2 = { -0.03, 0.03, 0.0 };//矢印の座標値
		double[] y2 = { 0.95, 0.95, 1.0 };//矢印の座標値
		m.drawLine(0,-1,0,1);//Y軸の描画
		m.fillPolygon(x2,y2,3);//矢印の塗りつぶし
		m.drawString("Y軸",-0.2,0.9);//Y軸という文字列の描画
		// 線分の描画
		m.drawLine(-1,-1,1,1);//(-1,-1)から(1,1)まで描画
	}
}
