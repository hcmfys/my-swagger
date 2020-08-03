package org.springbus.comutergraphics.CG.C2_3.parametricCurve;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// circleParam.java
// パラメトリックな曲線描画プログラム（オリジナルのキャンバス上）
//	プログラム２−９

import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.awt.*;
import java.applet.Applet;

public class circleParam extends Applet {

	MyCanvas m;//MyCanvasクラスの変数
	private double xmin = -1.2;
	private double ymin = -1.2;
	private double xmax = 1.2;
	private double ymax = 1.2;

	public void init(){
		m = new MyCanvas(this);//MyCanvasクラスのオブジェクト生成
	}

	public void paint (Graphics g){
		m.setBackground(new Color(230,230,230));
		m.setColor(Color.black);
		m.setWindow(xmin,xmax,ymin,ymax);

		// Ｘ軸を描く
		double[] x1 = { xmax-0.05, xmax, xmax-0.05 };
		double[] y1 = { -0.03, 0, 0.03 };
		m.fillPolygon(x1,y1,3);
		m.drawString("X軸",1.05,-0.15);
		m.drawLine(xmin,0,xmax,0);

		// Ｙ軸を描く
		double[] x2 = { -0.03, 0.03, 0.0 };
		double[] y2 = { ymax-0.05, ymax-0.05, ymax };
		m.fillPolygon(x2,y2,3);
		m.drawString("Y軸",-0.2,1.05);
		m.drawLine(0,ymin,0,ymax);

		// 円を描画
		double t = 0.0;
		double x = Math.cos(t); // X値の初期化
		double y = Math.sin(t); // Y値の初期化
		double x0 = x, y0 = y;
		m.moveTo(x,y);
		int step = 100;
		double deltat = 2 * Math.PI / step;
		for (int i = 0; i < step ; i++ ){
			t += deltat;
			x = Math.cos(t);
			y = Math.sin(t);
			m.lineTo(x,y);
		}
		m.lineTo(x0,y0);//最後の点から最初の点を結ぶ
	}
}
