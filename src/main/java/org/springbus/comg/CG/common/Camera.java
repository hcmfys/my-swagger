package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Camera.java
// カメラ（視点および投影スクリーン定義用）のクラス
//	プログラム３−１３
//		Cameraクラスのコンストラクタ，reset(),getCurrentPlainViewMatrix(),
//		setViewMatrixInverse(),setViewMatrix(),setScreenDetail(),
//		setScreen(),getScreenX(),getScreenY(),setScreenXY(),areaCode(),
//		getWorldPosition(),getCameraPosition(),setEyePosition(),
//		getEyePosition(),setScreenResolution(),getScreenWidth(),
//		getScreenHeight(),setAspectRatio(),getAspectRatio(),
//		setFieldOfView(),getFieldOfView(),setFocalLength(),
//		getFocalLength(),setParallel(),getParallel(),rotate(),
//		drawLine()メソッド

public class Camera extends MyObject {

	MyCanvas m;//描画用のキャンバスクラス
	Matrix4 viewmat;// 投影変換に使う
	Matrix4 cammat;//カメラ行列
	public   double screenMaxX, screenMaxY; // スクリーンの右上座標
	public double screenMinX, screenMinY; // スクリーンの左下座標
	public   double deltaHorizontal; // １画素水平に動いたときの実距離
	public double deltaVertical;   // １画素垂直に動いたときの実距離
	Vertex3 eyePosition; // 視点
	Vector3 upDirection; // 視点から見たスクリーンの真上方向
	Vector3 toDirection; // スクリーンから視点の方向
	Vector3 rightDirection; // 視点から見たスクリーンの右方向
	public boolean parallel = false; // 平行投影かどうかのスイッチ
	public double focalLength = 1.0; // 焦点距離　(視点からこの距離離れたところにスクリーンが置かれる。）
	public double fieldOfView = Math.PI/2; // X軸方向の視界（ラジアン）
	public double aspectRatio=4.0/3.0; // スクリーンのアスペクト比率
	public int resHorizontal=640;      // スクリーンの横方向の解像度
	public int resVertical=480;        // スクリーンの縦方向の解像度
	final static int LEFT_VIEWVOLUME = 0x1;  //ビューボリュームの左側の面
	final static int RIGHT_VIEWVOLUME = 0x2; //ビューボリュームの右側の面
	final static int BOTTOM_VIEWVOLUME = 0x4;//ビューボリュームの下側の面
	final static int TOP_VIEWVOLUME = 0x8;   //ビューボリュームの上側の面

	// コンストラクタ
	// デフォルトでＸ軸がrightDirection,
	// Ｙ軸がupDirection, Z軸がtoDirectionとなる。
	// 通常は、２つの軸が決まれば、残りの軸方向は、右ねじの法則で求まる。
	// 視点は原点(0,0,0)がデフォルト
	// スクリーンの位置は、Z = -1.0（平面上） がデフォルト
	public Camera(MyCanvas m) {
		this.m = m;
		eyePosition = new Vertex3(0,0,0);
		upDirection = new Vector3(0,1,0);
		toDirection = new Vector3(0,0,1);
		rightDirection = new Vector3(1,0,0);
		cammat = new Matrix4(); 
		parallel = false;
		focalLength = 1.0;
		fieldOfView = Math.PI/2.0;
		aspectRatio = 4.0/3.0;
		resHorizontal = 640;
		resVertical = 480;
		setScreen();
		setViewMatrix();
	}
	public Camera(){
		this(null);
	}

	// カメラのリセット
	public void reset(){
		eyePosition.x = eyePosition.y = eyePosition.z = 0;
		upDirection.x = upDirection.z = 0; upDirection.y = 1;
		toDirection.x = toDirection.y = 0; toDirection.z = 1;
		rightDirection.x = 1; rightDirection.y = rightDirection.z = 0;
		cammat.identity();
		setViewMatrix();
	}

	// ビュー行列の計算 （内部的に使われる）
	private Matrix4 getCurrentPlainViewMatrix(){
		Matrix4 m = new Matrix4();
		m.a[0] = rightDirection.x;
		m.a[1] = upDirection.x;
		m.a[2] = toDirection.x;
		m.a[4] = rightDirection.y;
		m.a[5] = upDirection.y;
		m.a[6] = toDirection.y;
		m.a[8] = rightDirection.z;
		m.a[9] = upDirection.z;
		m.a[10] = toDirection.z;
		m.a[3] = eyePosition.x;
		m.a[7] = eyePosition.y;
		m.a[11] = eyePosition.z;
		cammat.assign(m);
		return m;
	}
	private void setViewMatrixInverse() {
		try {
			viewmat = cammat.inverse();
		}
		catch (SingularMatrixException e){}
	}
	private void setViewMatrix() {
		Matrix4 m = getCurrentPlainViewMatrix();
		setViewMatrixInverse();
	}
	// スクリーンの計算（内部的に使われる）
	private void setScreenDetail(){
		screenMaxX = focalLength * Math.tan(fieldOfView/2.0);
		screenMaxY = screenMaxX / aspectRatio;
		screenMinX = -screenMaxX;
		screenMinY = -screenMaxY;
		deltaHorizontal = (screenMaxX-screenMinX)/(resHorizontal-1);
		deltaVertical = (screenMaxY-screenMinY)/(resVertical-1);
	}
	private void setScreen(){
		aspectRatio = (double)resHorizontal/(double)resVertical;
		setScreenDetail();
	}

	//スクリーンの横と縦のカメラ座標空間でのサイズを返すメソッド
	public double getScreenX(){ return(screenMaxX-screenMinX); }
	public double getScreenY(){ return(screenMaxY-screenMinY); }

	//スクリーンの縦と横をカメラ座標値のサイズで設定するメソッド
	public void setScreenXY(double xsize, double ysize){
		if (xsize <= 0 || ysize <= 0)
			throw new InternalError("スクリーンサイズは正でなければなりません"); 
		screenMaxX = xsize/2;
		screenMinX = -xsize/2;
		screenMaxY = ysize/2;
		screenMinY = -ysize/2;
		fieldOfView = 2*Math.atan2(screenMaxX,focalLength);
		aspectRatio = xsize/ysize;
		deltaHorizontal = (screenMaxX-screenMinX)/(resHorizontal-1);
		deltaVertical = (screenMaxY-screenMinY)/(resVertical-1);
	}

	// エリアコード
	public int areaCode(Vertex3 v){
		double x = v.x;
		double y = v.y;
		double z = v.z;
		int code = 0;
		if (x*focalLength < -screenMinX*z) code |= LEFT_VIEWVOLUME;
		else if (x*focalLength > -screenMaxX*z) code |= RIGHT_VIEWVOLUME;
		if (y*focalLength < -screenMinY*z) code |= BOTTOM_VIEWVOLUME;
		else if (y*focalLength > -screenMaxY*z) code |= TOP_VIEWVOLUME;
		return code;
	}

	// カメラ座標から世界座標へ変換 
	public Vertex3 getWorldPosition(Vertex3 v){
		Vertex4 v1 = new Vertex4(v.x,v.y,v.z,1);
		Vertex4 v2 = cammat.multiplyVertex4(v1);
		Vertex3 p = new Vertex3(v2.x,v2.y,v2.z);
		return p;
	}

	// 世界座標からカメラ座標へ変換
	public Vertex3 getCameraPosition(Vertex3 v){
		Vertex4 v1 = new Vertex4(v.x,v.y,v.z,1);
		Vertex4 v2 = viewmat.multiplyVertex4(v1);
		Vertex3 p = new Vertex3(v2.x,v2.y,v2.z);
		return p;
	}

	// 視点
	public void setEyePosition(Vertex3 eyePosition){
		this.eyePosition.x = eyePosition.x;
		this.eyePosition.y = eyePosition.y;
		this.eyePosition.z = eyePosition.z;
		setViewMatrix();
	}
	public void setEyePosition(double x, double y, double z){
		eyePosition.x = x;
		eyePosition.y = y;
		eyePosition.z = z;
		setViewMatrix();
	}
	public Vertex3 getEyePosition(){
		return eyePosition;
	}

	// スクリーンの解像度
	public void setScreenResolution(int Horizontal, int Vertical){
		if (Horizontal <= 0 || Vertical <= 0)
			throw new InternalError("スクリーンの解像度が不適切です");
		resHorizontal = Horizontal;
		resVertical = Vertical;
		setScreen();
	}
	public int getScreenWidth(){
		return resHorizontal;
	}
	public int getScreenHeight(){
		return resVertical;
	}

	// スクリーンのアスペクト比率
	public void setAspectRatio(double ratio){
		if (ratio <= 0)
			throw new InternalError("アスペクト比が不適切です");
		aspectRatio = ratio;
		setScreen();
	}
	public double getAspectRatio(){
		return aspectRatio;
	}	

	// 視野角度
	public void setFieldOfView(double fov){
		if (fov <= 0 || fov >= Math.PI)
			throw new InternalError("視野角度が不適切です");
		fieldOfView = fov;
		setScreenDetail();
	}
	public double getFieldOfView(){
		return fieldOfView;
	}

	// 焦点距離
	public void setFocalLength(double focalLength){
		if (focalLength <= 0)
			throw new InternalError("焦点距離が不適切です");
		this.focalLength = focalLength;
		setScreenDetail();
	}
	public double getFocalLength(){
		return focalLength;
	}		

	// 投影方法
	public void setParallel(boolean on){
		parallel = on;
	}
	public boolean getParallel(){
		return parallel;
	}

	// カメラ座標の回転 
	public void rotate(Vector3 axis, double theta){
		Matrix4 m = new Matrix4();
		Matrix4 m2 = m.rotate(axis, theta);
		cammat.multiply(m2);
		rightDirection.x = cammat.a[0];
		rightDirection.y = cammat.a[4];
		rightDirection.z = cammat.a[8];
		upDirection.x = cammat.a[1];
		upDirection.y = cammat.a[5];
		upDirection.z = cammat.a[9];
		toDirection.x = cammat.a[2];
		toDirection.y = cammat.a[6];
		toDirection.z = cammat.a[10];
		eyePosition.x = cammat.a[3];
		eyePosition.y = cammat.a[7];
		eyePosition.z = cammat.a[11];
		setViewMatrixInverse();
	}
	public void rotate(double x, double y, double z, double theta){
		Vector3 v = new Vector3(x,y,z);
		rotate(v,theta);
	}

	// カメラ座標系でのスクリーン上に線画を描画
	public void drawLine(double x1, double y1, double z1,
    	double x2, double y2, double z2){
		double xs1,ys1,xs2,ys2;
		if (!parallel){//パースペクティブディビジョン
			xs1 = -x1/z1*focalLength;
			ys1 = -y1/z1*focalLength;
			xs2 = -x2/z2*focalLength;
			ys2 = -y2/z2*focalLength;
		}
		else {//平行投影
			xs1 = x1; ys1 = y1;
			xs2 = x2; ys2 = y2;
		}
		m.drawLine(xs1,ys1,xs2,ys2);
	}
}
