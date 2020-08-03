package org.springbus.comg.CG.C4_2.rayApplet;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// rayApplet2.java (レイトレーシングサンプルプログラム：アプレット）
// Webブラウザ版：JavaScriptとの組み合わせ
// appletviewerでは，動きません！

import org.springbus.comg.CG.common.*;

import java.applet.Applet;
import java.awt.*;

public class rayApplet2 extends JApplet {

	final static int COLORLEVEL = 255;//シェーディング計算結果を[0-255]に。
	private static double KIZAMI = 0.1;//アニメーションループでの刻み
	private static int LOOP = 24;//フレーム数
	ObjectWorld ow;//ObjectWorldクラスの変数
	ObjectNode root;//シーングラフの「根」ノード
	MyCanvas m;//MyCanvasクラスの変数
	double screenx, screeny;//カメラ座標系でのスクリーンのサイズ
	double status = -1.0;//現在のレイトレースの進行状況のために使用
	Image rayOutImage;//レイトレースの出力画像保持用
	boolean finished = false;//レイトレースの終了フラグ
	Camera c;//カメラ用の変数
	boolean isFirst = true;//paint()メソッドで最初だけ行う処理用
	Graphics cg;//Graphicsクラスのオブジェクト保持用
	int frame=0;//フレーム番号
	public boolean started = false;//レイトレースの開始をHTMLから受ける

	public void init(){
		started = false;
	}

	// HTMLの<INPUT>タグで"onClick"されたときに実行されるメソッド
	public void FrameId(int index){
		frame = index;//レイトレースしたいフレーム番号を引数で渡す
		started = true;//レイトレース開始OKサイン
		init1(frame);//init1()メソッドの実行
		repaint();//描画開始
	}

	public void init1(int index){
		frame = index;//レイトレースしたいフレーム番号
		ow = new ObjectWorld();//世界のオブジェクト生成
		ObjectNode universe = ow.createUniverse();//「根」ノード生成
		ow.setBackgroundColor(0,1,1);//背景色をシアンに
		c = new Camera();//カメラオブジェクト生成
		c.setScreenResolution(240,240);//スクリーンの解像度設定
		ow.addCamera(c,"CAMERA1");//カメラを世界に追加（アクティブにする）
		PointLight light = new PointLight(0,20,4);//点光源を生成
		light.setIntensity(1.2);//点光源の輝度設定
		// 光源をシーングラフの「根」の直下に追加
		ObjectNode lightNode = universe.addChild(light,"LIGHT");
		// 「蝶」のシーングラフの生成
		Butterfly butterfly = new Butterfly(this,lightNode);
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

		m = new MyCanvas(this);//MyCanvasクラスのオブジェクト生成
		screenx = c.getScreenX();//スクリーンのXサイズ設定
		screeny = c.getScreenY();//スクリーンのYサイズ設定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();//オフスクリーンバッファの設定
		ow.setupRaytrace();//レイトレースの前処理
		root = ow.getRootNode();//「根」ノードを保持
	}
	public void raytrace(){
		// カメラの初期設定
		Vertex3 camOrigin = new Vertex3(0,0,0);
		Vertex3 origin = c.getWorldPosition(camOrigin);
		double currentX = c.screenMinX;
		double currentY = c.screenMaxY;
		Vertex3 camScreen = new Vertex3(currentX,currentY,-c.focalLength);
		Vertex3 screen = c.getWorldPosition(camScreen);
		Vector3 direction = new Vector3();
		int r,g,b;
		for (int j = 0; j < c.resVertical ; j++ ){
			status = -screenx/2 + screenx*(j+1)/c.resVertical;
			paint(cg);

			for (int i = 0; i < c.resHorizontal ; i++ ){
				direction.assign(screen);
				direction.subtract(origin);
				direction.normalize();//レイの方向ベクトル設定
				// 視線レイのオブジェクト生成
				Ray ray = new Ray(origin,direction,i,j);
				ray.objectWorld = ow;
				ray.shoot(root.child);//レイトレーシング開始
				int color = 0xff000000;
				ray.color.clamp();
				r = (int) ((double)COLORLEVEL*ray.color.r);
				g = (int) ((double)COLORLEVEL*ray.color.g);
				b = (int) ((double)COLORLEVEL*ray.color.b);
				color = color |(r << 16)|(g << 8)|b;
				ow.setPixel(j*c.resHorizontal+i,color);//シェーディング結果保持
				ray = null;
				currentX += c.deltaHorizontal;
				camScreen.x = currentX;
				camScreen.y = currentY;
				camScreen.z = -c.focalLength;
				screen = c.getWorldPosition(camScreen);
			}
			currentX = c.screenMinX;
			currentY -= c.deltaVertical;
			camScreen.x = currentX;
			camScreen.y = currentY;
			camScreen.z = -c.focalLength;
			screen = c.getWorldPosition(camScreen);
		}
		finished = true;
		rayOutImage = ow.getRaytracedImage(this);
		paint(cg);
	}

	public void paint(Graphics g){
		m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setViewport(0,1,0.04,1);
		if (finished){//レイトレーシングが終了したら画像を描画
			m.drawImage(rayOutImage,-screenx/2,screeny/2,
				screenx,screeny,this);
		}
		else {
			Font f = m.MyFont(m.getFont().getName(), m.getFont().getStyle(),2.0);
			m.setFont(f);//フォントの設定
			m.setColor(Color.black);
			if (started)
				m.drawString("計算中",-0.3,0.0);
		}
		// 計算中に出す青色の進行バーの描画
		m.setViewport(0,1,0,0.04);
		m.setColor(Color.blue);
		m.fillRect(-screenx/2,-screeny/2,status,screeny/2);
		if (isFirst && !finished && started) {
			cg = g;
			isFirst = false;
			raytrace();
		}
		m.resetViewport();
		m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		m.drawImage(m.getOffScreenImage(),-screenx/2,screeny/2,this);
	}
}
