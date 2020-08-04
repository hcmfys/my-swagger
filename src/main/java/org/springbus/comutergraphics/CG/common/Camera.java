package org.springbus.comutergraphics.CG.common;


// Camera.java
//相机（用于视点和投影屏幕定义）类
//程序3-13

public class Camera extends MyObject {

	MyCanvas m;//画布类进行绘制
	Matrix4 viewmat;// 用于投影转换
	Matrix4 cammat;//相机矩阵
	public   double screenMaxX, screenMaxY; //屏幕右上角坐标
	public double screenMinX, screenMinY; // 屏幕的左下角坐标
	public   double deltaHorizontal; // 水平移动一个像素时的实际距离
	public double deltaVertical;   // 垂直移动一个像素时的实际距离
	Vertex3 eyePosition; // 視点
	Vector3 upDirection; // 从视点正好在屏幕上方
	Vector3 toDirection; // 从屏幕到视点的方向
	Vector3 rightDirection; // 从透视图的屏幕右侧
	public boolean parallel = false; // 平行投影开关
	public double focalLength = 1.0; //焦距（屏幕放置在距视点此距离的位置。）
	public double fieldOfView = Math.PI/2; // X轴视场（弧度）
	public double aspectRatio=4.0/3.0; // 屏幕纵横比
	public int resHorizontal=640;      // 屏幕水平分辨率
	public int resVertical=480;        // 屏幕垂直分辨率
	final static int LEFT_VIEWVOLUME = 0x1;  //视线左侧
	final static int RIGHT_VIEWVOLUME = 0x2; //右侧视线
	final static int BOTTOM_VIEWVOLUME = 0x4;//视区下表面
	final static int TOP_VIEWVOLUME = 0x8;   //视线上方

	//构造函数
	//默认情况下，X轴为rightDirection，
	// Y轴为upDirection，Z轴为toDirection。
	//通常，如果确定了两个轴，则可以通过右旋螺纹定律获得其余的轴向方向。
	//默认情况下，视点为原点（0,0,0）
	//屏幕位置默认为Z = -1.0（在平面上）
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

	//相机重置
	public void reset(){
		eyePosition.x = eyePosition.y = eyePosition.z = 0;
		upDirection.x = upDirection.z = 0; upDirection.y = 1;
		toDirection.x = toDirection.y = 0; toDirection.z = 1;
		rightDirection.x = 1; rightDirection.y = rightDirection.z = 0;
		cammat.identity();
		setViewMatrix();
	}

	// 计算视图矩阵（内部使用）
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
	// 屏幕计算（内部使用）
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

	//在水平和垂直相机坐标空间中返回屏幕大小的方法
	public double getScreenX(){ return(screenMaxX-screenMinX); }
	public double getScreenY(){ return(screenMaxY-screenMinY); }

	//通过相机坐标值的大小设置屏幕的垂直和水平的方法
	public void setScreenXY(double xsize, double ysize){
		if (xsize <= 0 || ysize <= 0)
			throw new InternalError("屏幕尺寸必须为正");
		screenMaxX = xsize/2;
		screenMinX = -xsize/2;
		screenMaxY = ysize/2;
		screenMinY = -ysize/2;
		fieldOfView = 2*Math.atan2(screenMaxX,focalLength);
		aspectRatio = xsize/ysize;
		deltaHorizontal = (screenMaxX-screenMinX)/(resHorizontal-1);
		deltaVertical = (screenMaxY-screenMinY)/(resVertical-1);
	}

	// 区号
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

	// 从相机坐标转换为世界坐标
	public Vertex3 getWorldPosition(Vertex3 v){
		Vertex4 v1 = new Vertex4(v.x,v.y,v.z,1);
		Vertex4 v2 = cammat.multiplyVertex4(v1);
		Vertex3 p = new Vertex3(v2.x,v2.y,v2.z);
		return p;
	}

	//从世界坐标转换为相机坐标
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

	// 屏幕分辨率
	public void setScreenResolution(int Horizontal, int Vertical){
		if (Horizontal <= 0 || Vertical <= 0)
			throw new InternalError("屏幕分辨率不正确");
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

	// 屏幕纵横比
	public void setAspectRatio(double ratio){
		if (ratio <= 0)
			throw new InternalError("宽高比不合适");
		aspectRatio = ratio;
		setScreen();
	}
	public double getAspectRatio(){
		return aspectRatio;
	}

	// 視野角度-可视角度
	public void setFieldOfView(double fov){
		if (fov <= 0 || fov >= Math.PI)
			throw new InternalError("不合适的视角");
		fieldOfView = fov;
		setScreenDetail();
	}
	public double getFieldOfView(){
		return fieldOfView;
	}

	// 焦点距離
	public void setFocalLength(double focalLength){
		if (focalLength <= 0)
			throw new InternalError("焦距不合适");
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

	// 旋转相机坐标
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

	// 在摄像机坐标系中的屏幕上绘制线条图
	public void drawLine(double x1, double y1, double z1,
    	double x2, double y2, double z2){
		double xs1,ys1,xs2,ys2;
		if (!parallel){//透视师
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
