package org.springbus.comutergraphics.CG.C1_1.lineDrawApplet;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// lineDrawApplet.java		<- // はコメントを表す
// 線画の簡単な例
//	プログラム１−１

import org.springbus.comutergraphics.CG.common.JApplet;

import java.awt.*;

public class lineDrawApplet extends JApplet {//lineDrawAppletクラスはAppletクラスを継承

	public void paint(Graphics g){//描画用のpaint()メソッドの定義
		Dimension d = getSize();//アプレットのサイズ取得
		g.drawLine(0,0,d.width,d.height);//(0,0)から(d.width,d.height)まで描画
	}

	 public  static void  main(String[] args) {
		new lineDrawApplet();
	 }


	@Override
	public void init() {

	}
}
