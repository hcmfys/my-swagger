package org.springbus.comg.CG.C2_5.imageDrawApplet;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// imageDrawApplet.java
// 画像データの描画例 (アプレット版）
//	プログラム２−１４

import org.springbus.comg.CG.common.MyCanvas;

import java.awt.*;//Java AWTパッケージを利用
import java.applet.Applet;//Java アプレットを利用
import java.net.*;//Java ネットワークパッケージを利用

public class imageDrawApplet extends Applet {

	protected Image image;//画像データの保持用
	protected URL imageURL;//URLの保持用
	protected String imageFile="kid.jpg";//画像データファイル名
	protected MyCanvas m; //MyCanvasクラスのオブジェクト

	public void init(){//アプレットの初期化
		try { // 画像データファイルの置かれているURLを作成
			imageURL = new URL(getDocumentBase(),imageFile);
		}
		catch (MalformedURLException e){ e.printStackTrace();}
		MediaTracker mt = new MediaTracker(this);//メディアトラッカー生成
		image = getImage(imageURL);//画像をゲット
		mt.addImage(image,1);//画像を１番としてメディアトラッカーに追加
		try { // メディアトラッカーで画像がロードされるのを待つ
			//１番目のメディアトラッカーオブジェクトが利用できるまで待つ
			mt.waitForID(1);
		}
		catch (InterruptedException e){ e.printStackTrace(); }
		m = new MyCanvas(this);//MyCanvasクラスのオブジェクト生成
	}

	public void paint(Graphics g){
		m.drawImage(image,-1,1,2.0,2.0,this);//画像描画
	}
}
