package org.springbus.comg.CG.C3_4.smoothShading;

// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// smoothShading.java (スムーズシェーディングするアプレット）
//	図３−１９（右）

import org.springbus.comg.CG.common.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;

public class smoothShading extends JApplet {

	ObjectWorld ow;//世界
	ObjectNode root;//シーングラフの「根」
	MyCanvas m;//MyCanvasクラス
	double screenx, screeny;//スクリーンのサイズ
	double status = -1.0;//現在のレイトレースの進行状況
	Image rayOutImage;//レイトレースの出力画像
	boolean finished;//レイトレーシングが終了したかどうかのフラグ
	Camera c;//カメラ
	boolean isFirst;//最初のpaint()メソッド呼び出しかどうかのフラグ
	Graphics cg;//paint()メソッドの使うGraphics
	int[] index = {
		0,1,6,	0,6,5,	1,2,7,	1,7,6,
		2,3,8,	2,8,7,	3,4,9,	3,9,8,
		5,6,11,	5,11,10,	6,7,12,	6,12,11,
		7,8,13,	7,13,12,	8,9,14,	8,14,13,
		10,11,16,	10,16,15,	11,12,17,	11,17,16,
		12,13,18,	12,18,17,	13,14,19,	13,19,18,
		15,16,21,	15,21,20,	16,17,22,	16,22,21,
		17,18,23,	17,23,22,	18,19,24,	18,24,23,
	};
	double[] vertex = {
		-0.5590169943749475,-0.30901699437494745,0.7694208842938133,
		-0.2938926261462366,-0.30901699437494745,0.9045084971874736,
		0.0,-0.30901699437494745,0.9510565162951535,
		0.29389262614623685,-0.30901699437494745,0.9045084971874736,
		0.5590169943749477,-0.30901699437494745,0.7694208842938132,
		-0.5805486404630472,-0.15643446504023092,0.7990566526874576,
		-0.30521248238988896,-0.15643446504023092,0.9393474323917528,
		0.0,-0.15643446504023092,0.9876883405951378,
		0.30521248238988913,-0.15643446504023092,0.9393474323917528,
		0.5805486404630474,-0.15643446504023092,0.7990566526874575,
		-0.5877852522924732,0.0,0.8090169943749473,
		-0.3090169943749475,0.0,0.9510565162951535,
		0.0,0.0,1.0,
		0.30901699437494773,0.0,0.9510565162951535,
		0.5877852522924734,0.0,0.8090169943749472,
		-0.5805486404630472,0.15643446504023104,0.7990566526874576,
		-0.3052124823898889,0.15643446504023104,0.9393474323917527,
		0.0,0.15643446504023104,0.9876883405951377,
		0.3052124823898891,0.15643446504023104,0.9393474323917527,
		0.5805486404630473,0.15643446504023104,0.7990566526874575,
		-0.5590169943749475,0.30901699437494756,0.7694208842938133,
		-0.2938926261462366,0.30901699437494756,0.9045084971874736,
		0.0,0.30901699437494756,0.9510565162951535,
		0.29389262614623685,0.30901699437494756,0.9045084971874736,
		0.5590169943749477,0.30901699437494756,0.7694208842938132,
	};
	
	public void init(){
		finished = false; isFirst = true;
		ow = new ObjectWorld();//世界のオブジェクト生成
		ObjectNode universe = ow.createUniverse();//「根」ノードの生成
		ow.setBackgroundColor(0.1,0.1,0.4);//背景色を暗い青に
		m = new MyCanvas(this);// MyCanvasクラスのオブジェクト生成

		c = new Camera(m);//カメラオブジェクトの生成
		screenx = c.getScreenX();//スクリーンのＸサイズ設定
		screeny = c.getScreenY();//スクリーンのＹサイズ設定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();//オフスクリーンバッファの設定
		c.setEyePosition(0,0,2);//視点の設定
		c.setFieldOfView(Math.PI/3);//視野角の設定
		ow.addCamera(c,"CAMERA1");//カメラを世界に追加（アクティブにする）
		// 平行光線の生成
		DirectionalLight light = new DirectionalLight();
		// 平行光線をシーングラフの「根」の直下に追加
		ObjectNode lightNode = universe.addChild(light,"LIGHT");

		// スムーズシェーディングしたい三角形インデックスフェイスセットの定義
		int numFace = 32;
		int numVertex = 25;
		int i, k;
		Index3[] ind = new Index3[numFace];
		Vertex3[] vtx = new Vertex3[numVertex];
		Vector3[] nml = new Vector3[numVertex];
		// 頂点インデックスの設定
		for (i=0, k=0 ; i < numFace ; i++, k += 3)
			ind[i] = new Index3(index[k],index[k+1],index[k+2]);
		// 頂点座標の設定
		for (i=0, k=0 ; i < numVertex ; i++, k += 3)
			 vtx[i] = new Vertex3(vertex[k],vertex[k+1],vertex[k+2]);
		// 頂点法線ベクトルの設定（これがないとスムーズシェーディングできない）
		for (i=0, k=0 ; i < numVertex ; i++, k += 3)
			 nml[i] = new Vector3(vertex[k],vertex[k+1],vertex[k+2]);
		TriangleSet ts1 = new TriangleSet(numFace,ind,
			numVertex,vtx,nml);
		// 三角形インデックスフェイスセットをシーングラフに追加
		ObjectNode tsNode = lightNode.addChild(ts1,"SMOOTHSURFACE");

		// 材質データの定義
		Material mat1 = new Material();
		mat1.setDiffuseColor(0,1,0);//拡散反射光成分の設定
		mat1.setSpecularColor(1,1,1);//鏡面反射光成分の設定
		mat1.setShininess(20.0);//光沢度の設定
		tsNode.setMaterial(mat1);//三角形インデックスフェイスセットに材質付与

		ow.setupRaytrace();//レイトレーシングの前処理
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
				direction.normalize();//レイの方向ベクトルの設定
				// レイオブジェクト生成
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = ow;
				ray.shoot(root.child);//レイトレーシングの開始
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
		if (finished){//レートレーシングが終了したら画像を描画
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
