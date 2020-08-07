package org.springbus.comutergraphics.CG.common;

// ObjectWorld.java（用于构建对象世界）
//程序3-12


import java.awt.*;
import java.awt.image.*;

public class ObjectWorld extends MyObject {

	Camera activeCamera = null; //当前正在使用的相机
	CameraList header= null; //摄像机列表的开始指针
	CameraList tailer = null; //指向摄像机列表末尾的指针
	ObjectNode root = null; //场景图的“根”节点
	int [] pixel = null; //用于存储阴影结果的像素数组
	MemoryImageSource mis = null; //用于存储图像的内存
	Image img = null; //通过阴影生成的图像
	Color3 background = new Color3(0,0,0); // 黒

	//世界之巅是一个Group对象。
	public ObjectNode createUniverse(){
		Group g = new Group();
		ObjectNode node = new ObjectNode(g,"Universe");
		root = node;
		Material material = new Material();
		root.setMaterial(material);
		return node;
	}
	public ObjectNode getRootNode() { return root;}

	// 设置背景色
	public void setBackgroundColor(Color3 c){
		if (c.isNegativeColor())
			throw new InternalError("背景颜色不正确。");
		this.background = c;
	}
	public void setBackgroundColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("背景颜色不正确。");
		this.background.r = r;
		this.background.g = g;
		this.background.b = b;
	}
	public Color3 getBackgroundColor(){
		return background;
	}

	//遍历世界并在每个节点处设置累积矩阵（及其逆矩阵）。
	//同时传播材料。
	//但假设root没有兄弟姐妹
	public void accumulateWorldMatrix(){
		ObjectNode p = root;
		if (p == null) return;
		int level = 0;
		p.acm = new Matrix4(p.mat);
		try {
			p.inv = p.mat.inverse();
		}
		catch (SingularMatrixException e){}
		level++;
		traverseMatrix4(p.child,level,p.material);
	}

	//根据世界的递归遍历设置累积矩阵（及其逆矩阵）。
	//同时传播物料数据。
	public void traverseMatrix4(ObjectNode node, int level, Material m)
		throws SingularMatrixException {
		if (node == null) return;
		if (node.material == null) node.dummyMaterial = m;
		if (node.parent != null)
			node.acm = node.mat.multiplyMatrix4(node.parent.acm);
		try {
			node.inv = node.acm.inverse();
		}
		catch (SingularMatrixException e){}
		// 子供のセット
		if (node.child != null){
			level++;
			traverseMatrix4(node.child, level, node.dummyMaterial);
		}
		// 兄弟集
		if (node.next != null) //只有兄弟姐妹的材料
			traverseMatrix4(node.next, level,
				node.parent.dummyMaterial);
	}

	// 世界の線画表示
	public void drawWorld(){
		if (root == null) return;
		Camera c = activeCamera;
		if (c == null){
			c = activeCamera = new Camera();
			this.addCamera(c,"DEFAULT_CAMERA");
		}
		// シーングラフの前処理
		accumulateWorldMatrix();
		//シーングラフの線画表示
		draw(c,root.child);
	}

	// 世界递归导线和线图显示
	public void draw(Camera c, ObjectNode node){
		if (node == null) return;
		if (node.isShapeNode())	node.element.draw(c,node);
		// 兄弟の表示
		if (node.next != null) draw(c, node.next);
		// 子供の表示
		if (node.child != null)	draw(c,node.child);
	}

	// 射线迹
	public Camera setupRaytrace(){
		// 世界にオブジェクトがなければなにもしない
		if (root == null) return null;
		// アクティブなカメラの設定
		Camera c = activeCamera;
		if (c == null){
			c = activeCamera = new Camera();
			this.addCamera(c,"DEFAULT_CAMERA");
		}
		// 画像メモリのアロケーション
		pixel = new int[c.resHorizontal * c.resVertical];
		// シーングラフの前処理
		accumulateWorldMatrix();
		return c;
	}

	// シェーディング結果の画素値の取得と設定
	public int[] getPixel(){ return pixel; }
	public void setPixel(int index, int value){ pixel[index] = value;}

	// レイトレーシング開始メソッド
	public void startRaytrace(){
		Camera c = setupRaytrace();//前処理
		if (c == null) return;

		// カメラの初期設定
		Vertex3 camOrigin = new Vertex3(0,0,0);//カメラの始点は原点
		Vertex3 origin = c.getWorldPosition(camOrigin);//始点を世界座標に変換
		double currentX = c.screenMinX;//スクリーンの左端
		double currentY = c.screenMaxY;//スクリーンの上端
		// レイトレーシングの開始スクリーン位置の設定
		Vertex3 camScreen = new Vertex3(currentX,currentY,-c.focalLength);
		// 開始位置を世界座標に変換
		Vertex3 screen = c.getWorldPosition(camScreen);
		Vector3 direction = new Vector3();//レイの方向ベクトル
		int r,g,b;//シェーディング結果の色値の保持

		for (int j = 0; j < c.resVertical ; j++ ){//スクリーンの縦方向

			for (int i = 0; i < c.resHorizontal ; i++ ){//スクリーンの横方向

				// レイの方向ベクトルの設定
				direction.assign(screen);
				direction.subtract(origin);
				direction.normalize();//正規化
				// レイオブジェクトの生成
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = this;//ObjectWorldオブジェクトの保持
				ray.shoot(root.child);//レイトレーシング開始！

				int color = 0xff000000;//色のアルファ値は不透明で初期化
				ray.color.clamp();//シェーディング結果の色値を正規化[0-1]
				r = (int) ((double)COLORLEVEL*ray.color.r);//赤色成分
				g = (int) ((double)COLORLEVEL*ray.color.g);//緑色成分
				b = (int) ((double)COLORLEVEL*ray.color.b);//青色成分
				color = color |(r << 16)|(g << 8)|b;//3原色をひとつにまとめる
				pixel[j*c.resHorizontal+i] = color;//色値を画素配列にコピー

				currentX += c.deltaHorizontal;//横方向を横デルタ幅だけ増やす
				camScreen.x = currentX;
				camScreen.y = currentY;
				camScreen.z = -c.focalLength;//Z値は常に焦点距離だけ負の位置
				screen = c.getWorldPosition(camScreen);//スクリーン位置の再設定
			}
			currentX = c.screenMinX;
			currentY -= c.deltaVertical;//縦方向を縦デルタ幅だけ減らす
			camScreen.x = currentX;
			camScreen.y = currentY;
			camScreen.z = -c.focalLength;//Z値は常に焦点距離だけ負の位置
			screen = c.getWorldPosition(camScreen);//スクリーン位置の再設定
		}
	}

	// レイトレース結果の画像をゲット
	public Image getRaytracedImage(Component component){
		if (activeCamera == null) return null;
		int width = activeCamera.resHorizontal;
		int height = activeCamera.resVertical;
		MemoryImageSource mis = new MemoryImageSource(
			width,height,pixel,0,width);
		return component.createImage(mis);
	}

	//向世界添加相机。
	//相机将自动启动。
	public void addCamera(Camera o, String s){
		if (o == null) throw new NullPointerException();
		CameraList newElement = new CameraList(o,s);
		if (tailer == null){
			tailer = newElement;
			header = newElement;
		}
		else {
			tailer.next = newElement;
			tailer = newElement;
		}
		activeCamera = o;
	}

	// 寻找相机
	private Camera findCamera(Camera cam){
		CameraList p = header;
		while (p != null){
			if (p.camera.equals(cam)) return p.camera;
			p = p.next;
		}
		return null;
	}

	//是否在世界中定义了指定的摄像机
	private boolean isDefinedCamera(Camera cam){
		CameraList p = header;
		while (p != null){
			if (p.camera.equals(cam)) return true;
			p = p.next;
		}
		return false;
	}

	//激活世界中已经定义的摄像头
	//绘图计算是由活动摄像机在屏幕上完成的。
	public void setCameraActive(Camera cam){
		if (isDefinedCamera(cam)) activeCamera = cam;
	}

	//遍历世界并打印
	public void printWorld(){
		ObjectNode p = root;
		if (p == null) return;
		int level = 0;
		System.out.println("Node: "+p.element+" level = "+level);
		p.acm.print();
		level++;
		printObjectNode(p.child, level);
	}

	// 世界递归遍历和印刷
	public void printObjectNode(ObjectNode node, int level){
		if (node == null) return;
		System.out.println("**************************************");
		System.out.println("Node: "+node.name+
			" parent = "+node.parent.name+" level = "+level);
		node.acm.print("acm");
		node.inv.print("inv");
		node.dummyMaterial.print();
		// 兄弟の印刷
		if (node.next != null)
			printObjectNode(node.next, level);
		// 子供の印刷
		if (node.child != null){
			level++;
			printObjectNode(node.child, level);
		}
	}

}
