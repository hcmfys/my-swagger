package org.springbus.comutergraphics.CG.C3_6.boxTextureMap;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// boxTextureMap.java（箱の各面へのテクスチャーマッピングの例）
//	図３−３２

import org.springbus.comutergraphics.CG.common.*;

import java.awt.*;

public class boxTextureMap extends JApplet {

	ObjectWorld ow;//世界＋カメラ
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
		ow = new ObjectWorld();
		ObjectNode universe = ow.createUniverse();
		ow.setBackgroundColor(0,1,1);
		m = new MyCanvas(this);
		c = new Camera(m);
		ow.addCamera(c,"CAMERA1");
		screenx = c.getScreenX();
		screeny = c.getScreenY();
		c.setEyePosition(0,0,4);
		c.setFieldOfView(Math.PI/2.8);
		c.rotate(1,0,0,0.5);
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		m.setOffScreenBuffer();

		PointLight light = new PointLight(-3,5,4);
		light.setIntensity(1.2);
		ObjectNode lightNode = universe.addChild(light,"LIGHT");
		DirectionalLight light2 = new DirectionalLight();
		light.setIntensity(1.0);
		ObjectNode lightNode2 = lightNode.addChild(light2,"LIGHT2");

		Group group = new Group();
		ObjectNode groupNode = lightNode2.addChild(group,"GROUP");

		Index3[] fs1 = new Index3[2];
		fs1[0] = new Index3(0,1,2);
		fs1[1] = new Index3(0,2,3);
		Vertex3[] vtx1 = new Vertex3[4];
		vtx1[0] = new Vertex3(-4,0,-4);
		vtx1[1] = new Vertex3(-4,0,4);
		vtx1[2] = new Vertex3(4,0,4);
		vtx1[3] = new Vertex3(4,0,-4);
		Vertex2[] tx1 = new Vertex2[4];
		tx1[0] = new Vertex2(0,0);
		tx1[1] = new Vertex2(2,0);
		tx1[2] = new Vertex2(2,2);
		tx1[3] = new Vertex2(0,2);
		TriangleSet ts1 = new TriangleSet(2,fs1,4,vtx1,null,4,tx1);
		ts1.print();
		ObjectNode tsNode = groupNode.addChild(ts1," PLANE");
		tsNode.translate(0,-1,0);

		Index3[] fs2 = new Index3[12];
		fs2[0] = new Index3(0,1,4);
		fs2[1] = new Index3(1,5,4);
		fs2[2] = new Index3(1,2,5);
		fs2[3] = new Index3(2,6,5);
		fs2[4] = new Index3(2,3,6);
		fs2[5] = new Index3(3,7,6);
		fs2[6] = new Index3(3,0,7);
		fs2[7] = new Index3(0,4,7);
		fs2[8] = new Index3(4,5,6);
		fs2[9] = new Index3(4,6,7);
		fs2[10] = new Index3(0,3,1);
		fs2[11] = new Index3(3,2,1);
		Vertex3[] vtx2 = new Vertex3[8];
		vtx2[0] = new Vertex3(1,0,1);
		vtx2[1] = new Vertex3(1,0,-1);
		vtx2[2] = new Vertex3(-1,0,-1);
		vtx2[3] = new Vertex3(-1,0,1);
		vtx2[4] = new Vertex3(1,2,1);
		vtx2[5] = new Vertex3(1,2,-1);
		vtx2[6] = new Vertex3(-1,2,-1);
		vtx2[7] = new Vertex3(-1,2,1);
		Index3[] fs3 = new Index3[12];
		fs3[0] = new Index3(0,1,3);
		fs3[1] = new Index3(1,2,3);
		fs3[2] = new Index3(0,1,3);
		fs3[3] = new Index3(1,2,3);
		fs3[4] = new Index3(0,1,3);
		fs3[5] = new Index3(1,2,3);
		fs3[6] = new Index3(0,1,3);
		fs3[7] = new Index3(1,2,3);
		fs3[8] = new Index3(0,1,2);
		fs3[9] = new Index3(0,2,3);
		fs3[10] = new Index3(0,1,3);
		fs3[11] = new Index3(1,2,3);
		Vertex2[] tx3 = new Vertex2[4];
		tx3[0] = new Vertex2(0,0);
		tx3[1] = new Vertex2(1,0);
		tx3[2] = new Vertex2(1,1);
		tx3[3] = new Vertex2(0,1);
		TriangleSet ts2 = new TriangleSet(12,fs2,8,vtx2,fs3,4,tx3);
		ObjectNode fsNode = groupNode.addChild(ts2,"CUBE");
		double angle = 45*Math.PI/180.0;
		fsNode.rotate(0,1,0,angle);
		fsNode.translate(0,-1,0);

		Texture mandrill = new Texture(this,  getDocumentBase(),"mandrill.jpg");
		mandrill.loadImage();
		Material mat2 = new Material();
		mat2.setEmissiveColor(0.1,0.1,0.1);
		mat2.setDiffuseColor(1,1,1);
		mat2.setSpecularColor(0.5,0.5,0.5);
		mat2.setShininess(120.0);
		mat2.setTexture(mandrill);
		fsNode.setMaterial(mat2);

		Texture image = new Texture(this,
			getDocumentBase(),"brick.jpg");
		image.loadImage();
		image.setRepeat(true,true);		
		Material mat3 = new Material();
		mat3.setEmissiveColor(0.3,0.3,0.3);
		mat3.setDiffuseColor(1,1,0);
		mat3.setSpecularColor(0.5,0.5,0.5);
		mat3.setShininess(120.0);
		mat3.setTexture(image);
		tsNode.setMaterial(mat3);

		ow.setupRaytrace();
		root = ow.getRootNode();
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
				direction.normalize();
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = ow;
				ray.shoot(root.child);
				int color = 0xff000000;
				ray.color.clamp();
				r = (int) ((double) MyObject.COLORLEVEL*ray.color.r);
				g = (int) ((double) MyObject.COLORLEVEL*ray.color.g);
				b = (int) ((double) MyObject.COLORLEVEL*ray.color.b);
				color = color |(r << 16)|(g << 8)|b;
				ow.setPixel(j*c.resHorizontal+i,color);
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
		if (finished){
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
