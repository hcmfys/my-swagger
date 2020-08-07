package org.springbus.comutergraphics.CG.C3_5.materialSamples;

// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// materialSample1.java （材質感のテスト用プログラム：アプレット）
//	図３−２０

import org.springbus.comutergraphics.CG.C3_2.sphdraw.sphdraw;
import org.springbus.comutergraphics.CG.C3_4.smoothShading.smoothShading;
import org.springbus.comutergraphics.CG.common.*;

import java.awt.*;

public class materialSample1 extends JApplet {

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

	public void init(){

		finished = false; isFirst = true;
		ow = new ObjectWorld();//世界のオブジェクト生成
		ObjectNode universe = ow.createUniverse();//「根」ノード作成
		ow.setBackgroundColor(0.8,0.8,0.8);//背景色をグレイに

		m = new MyCanvas(this);// MyCanvasクラスのオブジェクト生成

		c = new Camera(m);//カメラオブジェクト生成
		screenx = c.getScreenX();//スクリーンのＸサイズ設定
		screeny = c.getScreenY();//スクリーンのＹサイズ設定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();//オフスクリーンバッファの設定
		c.setEyePosition(0,0,2);//視点の設定
		c.setFieldOfView(Math.PI/2.5);//視野角度の設定
		ow.addCamera(c,"CAMERA1");//カメラを世界に追加（アクティブにする）
		// 点光源を生成
		PointLight light = new PointLight(-3,3,1);
		light.setIntensity(1.2);//点光源の輝度設定
		// 点光源をシーングラフの「根」の直下に追加
		ObjectNode lightNode = universe.addChild(light,"LIGHT");
		// Groupノードの生成とシーングラフへの追加
		Group group = new Group();
		ObjectNode groupNode = lightNode.addChild(group,"GROUP");

		// 「床」の三角形インデックスフェイスセット定義
		Index3[] fs1 = new Index3[2];
		fs1[0] = new Index3(0,1,2);
		fs1[1] = new Index3(0,2,3);
		Vertex3[] vtx1 = new Vertex3[4];
		double xmin = -10;
		double xmax = 10;
		double zmin = -10;
		double zmax = 10;
		vtx1[0] = new Vertex3(xmin,0,zmin);
		vtx1[1] = new Vertex3(xmin,0,zmax);
		vtx1[2] = new Vertex3(xmax,0,zmax);
		vtx1[3] = new Vertex3(xmax,0,zmin);
		Vertex2[] tx1 = new Vertex2[4];
		tx1[0] = new Vertex2(0,0);
		tx1[1] = new Vertex2(2,0);
		tx1[2] = new Vertex2(2,2);
		tx1[3] = new Vertex2(0,2);
		TriangleSet ts1 = new TriangleSet(2,fs1,4,vtx1,null,4,tx1);
		// 「床」をシーングラフのGroupノードに追加
		ObjectNode tsNode = groupNode.addChild(ts1," PLANE");
		groupNode.translate(0,-1,-1);//「床」の平行移動

		Texture image = new Texture(this,//「床」のテクスチャー生成
			getDocumentBase(),"tile.jpg");
		image.loadImage();//「床」のテクスチャーの読み込み
		image.setRepeat(true,true);//テクスチャーの反復設定
		image.scale(2,2);//テクスチャーのスケーリング

		Cone cone2 = new Cone(3,8);//円すいの生成
		Sphere sphere = new Sphere();//球の生成
		// 球をシーングラフのGroupノードに追加
		ObjectNode sphNode = groupNode.addChild(sphere,"SPHERE");
		// 円すいをシーングラフのGroupノードに追加
		ObjectNode coneNode2 = groupNode.addChild(cone2,"CONE2");
		coneNode2.translate(2,1,-5);//円すいを平行移動
		sphNode.translate(0,1,0);//球を平行移動

		// 材質設定
		Material mat1 = new Material();
		mat1.setDiffuseColor(1,1,1);//拡散反射光成分の設定
		mat1.setSpecularColor(0.5,0.5,0.5);//鏡面反射光成分の設定
		mat1.setShininess(120.0);//光沢度の設定
		mat1.setTexture(image);//テクスチャーの設定
		tsNode.setMaterial(mat1);//「床」に材質を付与

		Material mat3 = new Material();
		mat3.setEmissiveColor(0.2,0.0,0.2);//自己発散光成分の設定
		mat3.setDiffuseColor(1,0,1);//拡散反射光成分の設定
		mat3.setSpecularColor(0.5,0.5,0.5);//鏡面反射光成分の設定
		mat3.setShininess(120.0);//光沢度の設定
		coneNode2.setMaterial(mat3);//円すいに材質を付与

		Material mat2 = new Material();
		mat2.setEmissiveColor(0,0.1,0.3);//自己発散光成分の設定
		mat2.setDiffuseColor(0,0,0);//拡散反射光成分（ここでは黒色）の設定
		sphNode.setMaterial(mat2);//球に材質を付与

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
				direction.normalize();//レイの方向ベクトルの設定
				// レイオブジェクトの生成
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = ow;
				ray.shoot(root.child);//レイトレース開始
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

	public void paint(Graphics g) {
		if (m != null) {
			m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
			m.setWindow(-screenx / 2, screenx / 2, -screeny / 2, screeny / 2);
			m.setViewport(0, 1, 0.05, 1);
			if (finished) {//レイトレーシングが終了したら画像を描画
				m.drawImage(rayOutImage, -screenx / 2, screeny / 2,
						screenx, screeny, this);
			} else {
				Font f = m.MyFont(m.getFont().getName(),
						m.getFont().getStyle(), 2.0);
				m.setFont(f);//フォントの設定
				m.setColor(Color.black);
				m.drawString("計算中", -0.3, 0.0);
			}
			// 計算中を表す青色の進行バーの描画
			m.setViewport(0, 1, 0, 0.05);
			m.setColor(Color.blue);
			m.fillRect(-screenx / 2, -screeny / 2, status, screeny / 2);
			m.resetViewport();
			if (isFirst && !finished) {
				cg = g;
				isFirst = false;
				raytrace();
			}
			m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
			m.drawImage(m.getOffScreenImage(), -screenx / 2, screeny / 2, this);
		}
	}


	public static  void main(String[] args){
		materialSample1 mm=new materialSample1();
		mm.display();
	}

}
