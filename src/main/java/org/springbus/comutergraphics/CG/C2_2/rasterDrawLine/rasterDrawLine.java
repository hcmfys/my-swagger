package org.springbus.comutergraphics.CG.C2_2.rasterDrawLine;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// rasterDrawLine.java
// 線画のラスタライズ化プログラム（オリジナルのキャンバス上）
//	プログラム２−６

import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.awt.*;
import java.applet.Applet;

public class rasterDrawLine extends Applet {

	MyCanvas m;//MyCanvasクラスの変数

	//MyCanvasクラスのオブジェクト生成
	public void init(){ m = new MyCanvas(this); }

	public void paint (Graphics g){
		m.setBackground(Color.white);//背景は白色
		m.setColor(Color.black);//前面色は黒色
		m.setWindow(-1,1,-1,1);//ユーザ座標系の範囲

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

		// 線分をラスタライズ化して描画
		m.rasterizeDrawLine(-0.5,-1.0,0.5,1.0); 
		m.rasterizeDrawLine(-1.0,-0.5,1.0,0.5);
		m.rasterizeDrawLine(-0.5,1.0,0.5,-1.0); 
		m.rasterizeDrawLine(-1.0,0.5,1.0,-0.5);
	}
}
