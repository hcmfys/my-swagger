package org.springbus.comg.CG.C3_6.raySample2;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// raySample2.java (テクスチャーマッピングサンプルプログラム：アプレット）
//	プログラム３−３３

import org.springbus.comg.CG.common.*;

import java.applet.Applet;
import java.awt.*;

public class raySample2 extends JApplet {

	ObjectWorld ow;//世界
	ObjectNode root;//シーングラフの「根」
	MyCanvas m;//MyCanvasクラスの変数
	double screenx, screeny;//スクリーンのサイズ
	double status = -1.0;//現在のレイトレースの進行状況
	Image rayOutImage;//レイトレースの出力画像
	boolean finished;//レイトレーシングが終了したかどうかのフラグ
	Camera c;//カメラ
	boolean isFirst;//最初のpaint()メソッド呼び出しかどうかのフラグ
	Graphics cg;//paint()メソッドの使うGraphics

	public void init(){
		finished = false; isFirst = true;
		ow = new ObjectWorld();//世界のオブジェクト生成
		ObjectNode universe = ow.createUniverse();//「根」ノード生成
		ow.setBackgroundColor(0,1,1);//背景色をシアンに
		m = new MyCanvas(this);// MyCanvasクラスのオブジェクト生成
		c = new Camera(m);//カメラオブジェクト生成
		ow.addCamera(c,"CAMERA1");//カメラを世界に追加（アクティブにする）
		screenx = c.getScreenX();//カメラ座標系でのスクリーンのX軸のサイズ
		screeny = c.getScreenY();//カメラ座標系でのスクリーンのY軸のサイズ
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();//オフスクリーンバッファの設定

		DirectionalLight light = new DirectionalLight();//平行光線の生成
		light.setIntensity(1.2);//平行光線の輝度設定
		// 平行光線をシーングラフの「根」の直下に追加
		ObjectNode lightNode = universe.addChild(light,"LIGHT");

		// 「地球」を表現する球を生成
		Sphere earth = new Sphere(2);
		ObjectNode earthNode = lightNode.addChild(earth,"EARTH");
		// 地球を回転させて「日本」が見える位置にする
		earthNode.rotate(1,0,0,-5*Math.PI/180.0);//X軸の周りに少し回転
		earthNode.rotate(0,0,1,5*Math.PI/180.0);//Z軸の周りに少し回転
		earthNode.translate(0,0,-3.5);

		// 「世界地図」（ミラー図法）のテクスチャーの生成
		Texture image = new Texture(this,
			getDocumentBase(),"earth4.jpg");
		image.loadImage();

		// 「地球」用の材質設定
		Material mat1 = new Material();
		mat1.setSpecularColor(1,1,1);
		mat1.setShininess(300.0);
		mat1.setTexture(image);//ここでテクスチャーを設定
		earthNode.setMaterial(mat1);//「地球」に材質を結合

		ow.setupRaytrace();//レイトレースの前処理
		root = ow.getRootNode();//「根」ノードの保持
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
				// 視線レイオブジェクト生成
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = ow;
				ray.shoot(root.child);//レイトレーシング開始
				int color = 0xff000000;
				ray.color.clamp();
				r = (int) ((double) MyObject.COLORLEVEL*ray.color.r);
				g = (int) ((double) MyObject.COLORLEVEL*ray.color.g);
				b = (int) ((double) MyObject.COLORLEVEL*ray.color.b);
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
		m.setViewport(0,1,0.1,1);
		if (finished){//レイトレーシングが終了したら画像を描画
			m.drawImage(rayOutImage,-screenx/2,screeny/2,
				screenx,screeny,this);
		}
		else {
			Font f = m.MyFont(m.getFont().getName(),
				m.getFont().getStyle(),2.0);
			m.setFont(f);//フォントの設定
			m.setColor(Color.black);
			m.drawString("計算中",-0.3,0.0);
		}
		// 計算中を表す青色の進行バーの描画
		m.setViewport(0,1,0,0.1);
		m.setColor(Color.blue);
		m.fillRect(-screenx/2,-screeny/2,status,screeny/2);
		m.resetViewport();
		if (isFirst && !finished) {
			cg = g;
			isFirst = false;
			raytrace();
		}
		m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		m.drawImage(m.getOffScreenImage(),-screenx/2,screeny/2,this);
	}
}
