package org.springbus.comg.CG.C4_2.rayApplet;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// drawButterfly.java (「蝶」の線画表示：おまけアプレット）
//	プログラム４−５

import org.springbus.comg.CG.common.*;

import java.applet.Applet;
import java.awt.*;

public class drawButterfly extends JApplet {

	boolean isFirst = true;//init()メソッドが終了したかどうか
	final static int COLORLEVEL = 255;//シェーディング計算結果を[0-255]に。
	private static double KIZAMI = 0.1;//アニメーションループでの刻み
	private static int LOOP = 24;//フレーム数
	ObjectWorld ow;//ObjectWorldクラスの変数
	MyCanvas m;//MyCanvasクラスの変数
	double screenx, screeny;//カメラ座標系でのスクリーンのサイズ
	Camera c;//カメラ用の変数
	int frame=0;//フレーム番号
	Butterfly butterfly;//「蝶」クラスの変数

	public void init(){
		try {
			String s = getParameter("frame");//HTMLに"frame"が指定されていたら取得
			if (s != null)	frame = Integer.valueOf(s).intValue();
		}
		catch (NumberFormatException e){ e.printStackTrace();}
		ow = new ObjectWorld();//世界のオブジェクト生成
		ObjectNode universe = ow.createUniverse();//「根」ノード生成
		ow.setBackgroundColor(0,1,1);//背景色をシアンに

		m = new MyCanvas(this);//MyCanvasクラスのオブジェクト生成

		c = new Camera(m);//カメラ生成（注：引数にMyCanvasオブジェクトを指定）
		ow.addCamera(c,"CAMERA1");//カメラを世界に追加（アクティブにする）

		PointLight light = new PointLight(0,20,4);//点光源を生成
		light.setIntensity(1.2);//点光源の輝度設定
		// 光源をシーングラフの「根」の直下に追加
		ObjectNode lightNode = universe.addChild(light,"LIGHT");
		// 「蝶」のシーングラフの生成
		butterfly = new Butterfly(this,lightNode);
		// 「地面」用の四角形を生成（2個の三角形インデックスフェイスセット）
		Index3[] groundIndex = new Index3[2];
		groundIndex[0] = new Index3(0,1,2);
		groundIndex[1] = new Index3(0,2,3);
		Vertex3[] groundVertex = new Vertex3[4];
		groundVertex[0] = new Vertex3(-100,-10,100);
		groundVertex[1] = new Vertex3(100,-10,100);
		groundVertex[2] = new Vertex3(100,-10,-100);
		groundVertex[3] = new Vertex3(-100,-10,-100);
		TriangleSet ground = new TriangleSet(2,groundIndex,4,groundVertex);

		// 「地面」用の材質設定
		Material matGround = new Material();
		matGround.setDiffuseColor(0.4,0.9,0.3);
		ObjectNode groundNode = lightNode.addChild(ground,"GROUND");					groundNode.setMaterial(matGround);

		screenx = c.getScreenX();//スクリーンのXサイズ設定
		screeny = c.getScreenY();//スクリーンのYサイズ設定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();//オフスクリーンバッファの設定

	}

	public void paint(Graphics g){
		// オフスクリーンを設定
		m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setColor(Color.black);
		m.fillRect(-screenx/2,-screeny/2,screenx/2,screeny/2);
		m.setColor(Color.white);

		// カメラの初期化とフレーム番号に応じた向きを設定
		c.reset();
		c.setEyePosition(0,0,26+frame*KIZAMI);
		c.rotate(0,1,0,0.1*frame*KIZAMI);
		c.rotate(1,0,0,Math.PI/2-0.5*frame*KIZAMI);
		double u = 4 * Math.PI * frame / LOOP;
		double t1 = 10 * KIZAMI * Math.cos(u);
		double t2 = -t1;

		// 「蝶」の左翅と右翅の位置をフレーム番号に従い設定
		butterfly.butterfly.reset();
		butterfly.leftWing.reset();
		butterfly.rightWing.reset();
		butterfly.rightWing.rotate(0,0,1,t2);
		butterfly.leftWing.rotate(0,0,1,t1);
		butterfly.butterfly.translate(0,0.1*KIZAMI*frame,0);

		// オフスクリーンで球を描画 
		ow.drawWorld();
		// フロントスクリーンに変更
		m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		// オフスクリーン画像をフロントスクリーンに描画
		m.drawImage(m.getOffScreenImage(),-screenx/2,screeny/2,this);
	}
}
