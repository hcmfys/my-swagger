package org.springbus.comg.CG.C3_2.sphdraw;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// sphdraw.java (球の線画表示プログラム：アプレット）
//	プログラム３−１９

import org.springbus.comg.CG.common.*;

import java.awt.*;

public class sphdraw extends JApplet{

	ObjectWorld ow; //ObjectWorldクラスの変数
	MyCanvas m; // MyCanvasクラスの変数
	double screenx, screeny;// スクリーンサイズ

	public   void init(){

		// ObjectWorldクラスのオブジェクト生成
		ow = new ObjectWorld(); 
		// 世界（ユニバース）の生成
		ObjectNode universe = ow.createUniverse();
		// MyCanvasクラスのオブジェクト生成
		m = new MyCanvas(this);
		// Cameraクラスのオブジェクト生成
		Camera cam = new Camera(m);
		// スクリーンサイズの設定
		screenx = cam.getScreenX(); screeny = cam.getScreenY();
		// ウィンドウの設定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		// オフスクリーンバッファの設定
		m.setOffScreenBuffer();
		// カメラの位置の設定
		cam.setEyePosition(0,0,2); 
		// カメラの回転角度設定（X軸の周りに0.3ラジアン回す）
		cam.rotate(1,0,0,0.3);
		// カメラを世界に追加
		ow.addCamera(cam,"CAMERA1");
		// 球の定義
		Sphere sph = new Sphere(1);
		// 球をシーングラフに追加
		ObjectNode sphNode = universe.addChild(sph,"Sphere");

	}

  public void paint(Graphics g) {
    if (m != null) {
      // オフスクリーンを設定
      m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
      // オフスクリーンで球を描画
      ow.drawWorld();
      // フロントスクリーンに変更
      m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
      // オフスクリーン画像をフロントスクリーンに描画
      m.drawImage(m.getOffScreenImage(), -screenx / 2, screeny / 2, this);
    }
	}

	public  static void  main(String[] args) {
		new sphdraw();
	}
}
