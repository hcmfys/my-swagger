package org.springbus.comutergraphics.CG.common;


// MyCanvas.java
//用户的画布类
//用户坐标到视口的映射
//将视口转换为Java AWT坐标
//在用户坐标系等中二维图形的绘制方法

import java.awt.*;
import java.awt.image.*;

public class MyCanvas extends Component {

	// 用户坐标范围(默认值为[-1,1] x [-1,1])
	protected double userMinx = -1;//用户坐标系的X轴上的最小值
	protected double userMaxx = 1;//用户坐标系中X轴的最大值
	protected double userMiny = -1;//用户坐标系的Y轴上的最小值
	protected double userMaxy = 1;//用户坐标系中Y轴的最大值
	// 视口范围(默认为[0,1] x [0,1])
	protected double[] viewMinx;//视口X轴上的最小值
	protected double[] viewMaxx;//视口X轴的最大值
	protected double[] viewMiny;//视口Y轴的最小值
	protected double[] viewMaxy;//视口Y轴的最大值
	final static int DefaultViewportMax = 256;//默认视口数
	protected int viewportMax = DefaultViewportMax;//视口数量
	protected int viewportNum = 0;//当前视口数
	protected int currentViewport = 0;//当前视口的索引
	// 窗户尺寸
	final static int DefaultWindowSize = 256;//默认窗口大小
	protected int windowWidth = DefaultWindowSize;//窗宽
	protected int windowHeight = DefaultWindowSize;//窗户的高度
	// Java AWT Graphics and its Component
	protected Graphics graphics;//MyCanvas Graphics类数据
	protected Component component;//MyCanvas的组件类数据
	protected Color currentFrontColor = Color.white;//当前颜色
	protected Color currentBackColor = Color.black;//当前背景色

	// 内存映像源
	protected Image image;//内存图像源中使用的图像类数据
	protected MemoryImageSource mis;//内存映像源数据
	protected int[] pixel;// 用于创建内存图像源的颜色值数组
	protected int pixelWidth;//以上像素数据的宽度
	protected int pixelHeight;// 以上像素数据的垂直宽度
	protected int xoffset;// 窗口X坐标中的像素数据偏移
	protected int yoffset;// 窗口中Y坐标处的像素数据偏移
	//  支持MoveTo(x，y)和LineTo(x，y)
	protected double lastx = 0;//先前的X值
	protected double lasty = 0;//最后的Y值
	// 屏幕外和打印机数据
	protected Graphics currentGraphics;//当前图形
	protected Image offScreenImage;//屏幕外区域
	protected Graphics offScreenGraphics;//屏幕外图形
	protected Graphics printerGraphics;//打印机的图形数据
	protected Graphics userGraphics;//用户图形
	public final static int DEFAULT_GRAPHICS = 0;
	public final static int OFFSCREEN_GRAPHICS = 1;
	final static int PRINTER_GRAPHICS = 2;
	final static int USER_GRAPHICS = 3;
	// 字体
	final static int DefaultFontSize = 12;

	//构造函数
	//默认构造函数中的viewportMax = 256
	public MyCanvas(Component a) {
		component = a; //为MyCanvas类设置组件
		graphics = a.getGraphics(); //为MyCanvas设置图形
		currentGraphics = graphics; //设置当前图形
		windowWidth = a.getSize().width; //窗口宽度
		windowHeight = a.getSize().height; //窗口高度
		createViewport(DefaultViewportMax); //视口分配
	}

	public MyCanvas(Component a, int vMax) {
		component = a;//设置MyCanvas类的组件
		graphics = a.getGraphics();//设置MyCanvas的图形
		currentGraphics = graphics;//设置当前图形
		windowWidth = a.getSize().width;//窗宽
		windowHeight = a.getSize().height;//窗户的高度
		createViewport(vMax);//视口分配
	}

	private void createViewport(int max) {
		currentViewport = 0;//视口索引的初始设置
		viewportMax = max;//设置最大视口数
		viewMinx = new double[viewportMax];//视口X轴最小数组
		viewMaxx = new double[viewportMax];//视口X轴最大值数组
		viewMiny = new double[viewportMax];//视口Y轴最小阵列
		viewMaxy = new double[viewportMax];//视口Y轴最大数组
		viewMinx[0] = viewMiny[0] = 0.0;//最小视口值为0
		viewMaxx[0] = viewMaxy[0] = 1.0;//视口的最大值为1
		viewportNum = 1;//将视口的当前索引设置为1
	}

	//窗宽
	public int getWidth() {
		return windowWidth;
	}

	//窗户的高度
	public int getHeight() {
		return windowHeight;
	}

	//屏幕外缓冲区设置
	public void setOffScreenBuffer() {
		if (component == null) return;
		offScreenImage =
				component.createImage(windowWidth, windowHeight);
		offScreenGraphics = offScreenImage.getGraphics();
	}

	public void setOffScreenBuffer(int width, int height) {
		if (component == null) return;
		offScreenImage =
				component.createImage(width, height);
		offScreenGraphics = offScreenImage.getGraphics();
	}

	//区域副本
	public void copyArea(double x, double y, double w, double h,
						 double dx, double dy) {
		int ix1 = getX(x);//将复制开始位置的x坐标值设置为Java坐标值
		int iy1 = getY(y);//将复制开始位置的y坐标值设置为Java坐标值
		int iw = getDimensionX(w);//宽度
		int ih = getDimensionY(h);//高度
		int ix2 = getDimensionX(dx);//转换目的地的相对坐标
		ix2 *= Sign(dx);
		int iy2 = getDimensionY(dy);//转换目的地的相对坐标
		iy2 *= Sign(dy);
		currentGraphics.copyArea(ix1, iy1, iw, ih, ix2, iy2);
	}

	public void copyArea(double x, double y, double w, double h,
						 double dx, double dy, boolean flag) {
		int ix1 = getX(x);//将复制开始位置的x坐标值设置为Java坐标值
		int iy1 = getY(y);//将复制开始位置的y坐标值设置为Java坐标值
		int iw = getDimensionX(w);//横幅
		int ih = getDimensionY(h);//縦幅
		int ix2 = getDimensionX(dx);//转换目的地的相对坐标
		ix2 *= Sign(dx);
		int iy2 = getDimensionY(dy);//转换目的地的相对坐标
		iy2 *= Sign(dy);
		currentGraphics.copyArea(ix1, iy1, iw, ih, ix2, iy2);
	}

	public void copyArea(int x, int y, int w, int h, int dx, int dy) {
		currentGraphics.copyArea(x, y, w, h, dx, dy);
	}

	// 用户图形生成
	public Graphics create(double x, double y, double w, double h) {
		int ix = getX(x);//图形区域位置的X坐标值到Java坐标值
		int iy = getY(y);//图形区域位置的Y坐标值到Java坐标值
		int iw = getDimensionX(w);//宽度
		int ih = getDimensionY(h);//高度
		userGraphics = currentGraphics.create(ix, iy, iw, ih);
		return userGraphics;
	}

	public Graphics create(int x, int y, int w, int h) {
		userGraphics = currentGraphics.create(x, y, w, h);
		return userGraphics;
	}

	// 注销
	public void dispose() {
		if (currentGraphics == null) return;
		currentGraphics.dispose();
	}

	// Toolkitの獲得
	public Toolkit getToolkit() {
		return component.getToolkit();
	}

	// Visibility
	public void setVisible(boolean b) {
		component.setVisible(b);
	}

	// カーソル
	public void setCursor(Cursor c) {
		component.setCursor(c);
	}

	// 屏幕外图像
	public Image getOffScreenImage() {
		return (offScreenImage);
	}

	// 屏幕外图形
	public Graphics getOffScreenGraphics() {
		return (offScreenGraphics);
	}

	// 采集当前图形
	public Graphics getGraphics() {
		return (currentGraphics);
	}

	//打印机图形设置
	public void setPrinterGraphics(Graphics g) {
		printerGraphics = g;
	}

	// 图形设定
	public void setUserGraphics(Graphics g) {
		currentGraphics = g;
	}

	//当前图形设置
	public void setGraphics(int index) {
		if (index == DEFAULT_GRAPHICS) currentGraphics = graphics;
		else if (index == OFFSCREEN_GRAPHICS)
			currentGraphics = offScreenGraphics;
		else if (index == PRINTER_GRAPHICS)
			currentGraphics = printerGraphics;
		else if (index == USER_GRAPHICS)
			currentGraphics = userGraphics;
	}

	// 视窗大小调整方法
	public void setSize(int w, int h) {
		windowWidth = w; //窗口宽度设置
		windowHeight = h; //窗口高度设置
		component.setSize(w, h); //使用Component类的setSize()方法
	}

	// 设置用户坐标系的范围
	public void setWindow(double x1, double x2, double y1, double y2) {
		userMinx = x1; //在窗口的X轴上设置最小值
		userMaxx = x2; //设置窗口X轴的最大值
		userMiny = y1; //设置窗口的Y轴最小值
		userMaxy = y2; //设置窗口Y轴的最大值
	}

	// 视口设置(剪切)
	public void setViewport(double x1, double x2, double y1, double y2) {
		viewMinx[viewportNum] = x1; //当前视口X轴的最小值
		viewMaxx[viewportNum] = x2; //当前视口X轴上的最大值
		viewMiny[viewportNum] = y1; //当前视口的Y轴最小值
		viewMaxy[viewportNum] = y2; //当前视口的Y轴上的最大值
		currentViewport = viewportNum; //设置当前视口索引
		viewportNum++; //增加视口数量
		setClip(x1, y1, x2, y2, true); //在视口中设置裁剪
	}

	//视口设置，无需裁剪
	public void setNoClipViewport(double x1, double x2, double y1, double y2) {
		viewMinx[viewportNum] = x1; //当前视口X轴的最小值
		viewMaxx[viewportNum] = x2; //当前视口X轴上的最大值
		viewMiny[viewportNum] = y1; //当前视口的Y轴最小值
		viewMaxy[viewportNum] = y2; //当前视口的Y轴上的最大值
		currentViewport = viewportNum; //设置当前视口索引
		viewportNum++; //增加视口数量
	}

	//视口重置
	public void resetViewport() {
		currentViewport = 0; //常规索引返回0
		viewMinx[0] = viewMiny[0] = 0.0; //最小视点0
		viewMaxx[0] = viewMaxy[0] = 1.0; //最大眼镜1
		viewportNum = 1; //常规编号设置1
	}

	// 将视口坐标映射到Java AWT坐标的方法
	public int getIntX(double x) {
		return (int) (windowWidth * x);//双窗宽度
	}

	public int getIntY(double y) {//1-y代替y
		return (int) (windowHeight * (1 - y));//双窗高度
	}

	// 一种将用户坐标映射到视口坐标的方法
	public double viewX(double x) {
		double s = (x - userMinx) / (userMaxx - userMinx);
		double t = viewMinx[currentViewport] +
				s * (viewMaxx[currentViewport] - viewMinx[currentViewport]);
		return t;
	}

	public double viewY(double y) {
		double s = (y - userMiny) / (userMaxy - userMiny);
		double t = viewMiny[currentViewport] +
				s * (viewMaxy[currentViewport] - viewMiny[currentViewport]);
		return t;
	}

	// 从用户坐标获取Java AWT坐标的方法
	public int getX(double x) {
		double xx = viewX(x);//将x映射到视口
		int ix = getIntX(xx);//将视口映射到Java坐标系
		return ix;
	}

	public int getY(double y) {
		double yy = viewY(y);//将y映射到视口
		int iy = getIntY(yy);//将视口映射到Java坐标系
		return iy;
	}

	// 获取尺寸的方法
	public int getDimensionX(double w) {
		double x = viewMaxx[currentViewport] - viewMinx[currentViewport];
		x *= windowWidth * w / (userMaxx - userMinx);
		return ((int) Math.abs(x));
	}

	public int getDimensionY(double h) {
		double y = viewMaxy[currentViewport] - viewMiny[currentViewport];
		y *= windowHeight * h / (userMaxy - userMiny);
		return ((int) Math.abs(y));
	}

	//反向映射
	//从Java AWT坐标到视口的反向映射
	public int getViewport(int ix, int iy) {
		if (viewportNum == 1) return 0; // 默认视口
		double s = (double) (ix) / (double) windowWidth;
		double t = (double) (windowHeight - iy) / (double) windowHeight;
		for (int i = 0; i < viewportNum; i++) {
			if (s >= viewMinx[i] && s <= viewMaxx[i] &&
					t >= viewMiny[i] && t <= viewMaxy[i])
				return i;
		}
		return 0;
	}

	// 从视口到用户坐标系(X坐标)的逆向映射
	public double getUserX(int ix, int v) {
		double t = (double) (ix) / (double) windowWidth;
		double x = userMinx +
				(t - viewMinx[v]) / (viewMaxx[v] - viewMinx[v]) *
						(userMaxx - userMinx);
		return x;
	}

	// 从视口到用户坐标系(Y坐标)的逆向映射
	public double getUserY(int iy, int v) {
		double t = (double) (windowHeight - iy) / (double) windowHeight;
		double y = userMiny +
				(t - viewMiny[v]) / (viewMaxy[v] - viewMiny[v]) *
						(userMaxy - userMiny);
		return y;
	}

	// 颜色规格方法
	public Color getColor() {
		return currentFrontColor;//查找当前颜色
	}

	public void setColor(Color c) {
		currentGraphics.setColor(c);//设置当前颜色
		currentFrontColor = c;
	}

	public void setColor(double r, double g, double b) {
		Color c = new Color((float) r, (float) g, (float) b);
		currentGraphics.setColor(c);
		//現在の色を設定
		currentFrontColor = c;
	}

	public Color getForeground() {
		return currentFrontColor;//前面色を検索
	}

	public void setForeground(Color c) {
		component.setForeground(c);//前面色を設定
		currentFrontColor = c;
	}

	public void setForeground(double r, double g, double b) {
		Color c = new Color((float) r, (float) g, (float) b);
		component.setForeground(c);//前面色を設定
		currentFrontColor = c;
	}

	public Color getBackground() {
		return currentBackColor;//背景色を検索
	}

	public void setBackground(Color c) {
		component.setBackground(c);//背景色を設定
		currentBackColor = c;
	}

	public void setBackground(double r, double g, double b) {
		Color c = new Color((float) r, (float) g, (float) b);
		component.setBackground(c);//背景色を設定
		currentBackColor = c;
	}

	// クリッピング
	public void clipRect(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.clipRect(x0, y0, width, height);
	}

	public void setClip(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.setClip(x0, y0, width, height);
	}

	public void setClip(double x1, double y1, double x2, double y2,
						boolean flag) {
		int ix1 = getIntX(x1); //使视口的x坐标值位于四个角的Java坐标值中
		int iy1 = getIntY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getIntX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getIntY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.setClip(x0, y0, width, height);
	}

	//
	//绘制方法
	//
	//画线段
	//
	public void drawLine(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //转换为Java AWT坐标值
		int iy1 = getY(y1); //转换为Java AWT坐标值
		int ix2 = getX(x2); //转换为Java AWT坐标值
		int iy2 = getY(y2); //转换为Java AWT坐标值
		currentGraphics.drawLine(ix1, iy1, ix2, iy2);
	}

	//绘制一个矩形
	public void drawRect(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.drawRect(x0, y0, width, height);
	}

	//填充矩形
	public void fillRect(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.fillRect(x0, y0, width, height);
	}

	// 用矩形清除区域
	public void clearRect(double x1, double y1, double x2, double y2) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.clearRect(x0, y0, width, height);
	}

	// 绘制带有圆角的矩形
	public void drawRoundRect(double x1, double y1, double x2, double y2,
							  double arcW, double arcH) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		int iarcWidth = getDimensionX(arcW); //圆角的水平尺寸
		int iarcHeight = getDimensionY(arcH); //圆角的垂直大小
		currentGraphics.drawRoundRect(x0, y0, width, height,
				iarcWidth, iarcHeight);
	}

	//用圆角填充矩形
	public void fillRoundRect(double x1, double y1, double x2, double y2,
							  double arcW, double arcH) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		int iarcWidth = getDimensionX(arcW); //圆角的水平尺寸
		int iarcHeight = getDimensionY(arcH); //圆角的垂直大小
		currentGraphics.fillRoundRect(x0, y0, width, height,
				iarcWidth, iarcHeight);
	}

	// 画一个上升的矩形
	public void draw3DRect(double x1, double y1, double x2, double y2,
						   boolean raised) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.draw3DRect(x0, y0, width, height, raised);
	}

	// 浮かび上がる矩形の塗りつぶし
	public void fill3DRect(double x1, double y1, double x2, double y2,
						   boolean raised) {
		int ix1 = getX(x1); //将四个角中任意一个的x坐标值设置为Java坐标值
		int iy1 = getY(y1); //将与x1相同的点的y坐标值设置为Java坐标值
		int ix2 = getX(x2); //对于x1，将对角的x坐标值设置为Java坐标值
		int iy2 = getY(y2); //对于y1，将对角线的y坐标值设置为Java坐标值
		int width = Math.abs(ix1 - ix2) + 1; //计算宽度
		int height = Math.abs(iy1 - iy2) + 1; //计算高度
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //起点的X坐标(左上方)
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //起点的Y坐标(左上方)
		currentGraphics.fill3DRect(x0, y0, width, height, raised);
	}

	// だ円の描画　(中心(x,y), 半径(xr,yr))
	public void drawOval(double x, double y, double xr, double yr) {
		int ix = getX(x); //椭圆中心的Java AWT中的X坐标
		int iy = getY(y); //椭圆中心的Java AWT Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //椭圆周围矩形的左上角(X)
		int y0 = iy - iyr; //包围椭圆的矩形的左上角(X)
		currentGraphics.drawOval(x0, y0, 2 * ixr, 2 * iyr);
	}

	// だ円の塗りつぶし　(中心(x,y), 半径(xr,yr))
	public void fillOval(double x, double y, double xr, double yr) {
		int ix = getX(x); //椭圆中心的Java AWT中的X坐标
		int iy = getY(y); //椭圆中心的Java AWT Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //椭圆周围矩形的左上角(X)
		int y0 = iy - iyr; //包围椭圆的矩形的左上角(X)
		currentGraphics.fillOval(x0, y0, 2 * ixr, 2 * iyr);
	}

	// 円弧の描画　(中心(x,y) 半径(xr,yr))
	public void drawArc(double x, double y, double xr,
						double yr, double startAngle, double arcAngle) {
		int ix = getX(x); //弧中心的Java AWT X坐标
		int iy = getY(y); // Java AWT中圆弧中心的Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //封闭圆弧的矩形的左上角(X)
		int y0 = iy - iyr; //封闭圆弧的矩形的左上角(X)
		int is = (int) (90 - (startAngle + arcAngle)); //起始角度(度)
		int ia = (int) arcAngle; //扇形圆弧的角度(度)
		currentGraphics.drawArc(x0, y0, 2 * ixr, 2 * iyr, is, ia);
	}

	// 扇形填充（中心（x，y）半径（xr，yr））
	public void fillArc(double x, double y, double xr,
						double yr, double startAngle, double arcAngle) {
		int ix = getX(x); //扇区中心的Java AWT中的X坐标
		int iy = getY(y); // Java AWT中扇区中心的Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //扇区周围矩形的左上角(X)
		int y0 = iy - iyr; //扇区周围矩形的左上角(X)
		int is = (int) (90 - (startAngle + arcAngle)); //起始角度(度)
		int ia = (int) arcAngle; //扇形圆弧的角度(度)
		currentGraphics.fillArc(x0, y0, 2 * ixr, 2 * iyr, is, ia);
	}

	//画线
	public void drawPolyline(double[] x, double[] y, int numPoints) {
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i = 0; i < numPoints; i++) {//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.drawPolyline(ix, iy, numPoints);
	}

	// 绘制多边形
	public void drawPolygon(double[] x, double[] y, int numPoints) {
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i = 0; i < numPoints; i++) {//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.drawPolygon(ix, iy, numPoints);
	}

	//多边形填充
	public void fillPolygon(double[] x, double[] y, int numPoints) {
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i = 0; i < numPoints; i++) {//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.fillPolygon(ix, iy, numPoints);
	}

	// 画线
	public void drawString(String str, double x, double y) {
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		currentGraphics.drawString(str, ix, iy);
	}

	// 字型设定
	public Font getFont() {
		return currentGraphics.getFont();//查找当前的Graphics类字体
	}

	public void setFont(Font f) {
		currentGraphics.setFont(f);//当前Graphics类的字体设置
	}

	public double getFontSize(Font f) {
		int isize = f.getSize();
		double size = (double) isize / DefaultFontSize;
		return size;
	}

	public Font MyFont(String name, int style, double size) {
		if (size <= 0) size = 1.0;//如果大小为负，则为默认值
		int isize = (int) (DefaultFontSize * size);//比例字体
		Font f = new Font(name, style, isize);
		return f;
	}

	public FontMetrics getFontMetrics(Font f) {
		return component.getFontMetrics(f);
	}

	public double getStringWidth(String s, Font f) {
		int width = getFontMetrics(f).stringWidth(s);
		double w = (double) width / windowWidth * (userMaxx - userMinx);
		return w;
	}

	public double getHeight(Font f) {
		int height = getFontMetrics(f).getHeight();
		double h = (double) height / windowHeight;
		return h;
	}

	// 图像绘图
	public boolean drawImage(Image img, double x, double y,
							 ImageObserver observer) {
		if (currentGraphics == null) return false;
		int ix = getX(x);//转换为Java AWT坐标值
		int iy = getY(y);//转换为Java AWT坐标值
		return currentGraphics.drawImage(img, ix, iy, observer);
	}

	public boolean drawImage(Image img, double x, double y,
							 double w, double h, ImageObserver observer) {
		if (currentGraphics == null) return false;
		int ix = getX(x);//转换为Java AWT坐标值
		int iy = getY(y);//转换为Java AWT坐标值
		int iw = getDimensionX(w);//getX(w)-getX(0);//影像宽度
		int ih = getDimensionY(h);//getY(0)-getY(h);//影像高度
		return currentGraphics.drawImage(img, ix, iy, iw, ih, observer);
	}

	public boolean drawImage(Image img, double x, double y,
							 Color bgcolor, ImageObserver observer) {
		int ix = getX(x);//转换为Java AWT坐标值
		int iy = getY(y);//转换为Java AWT坐标值
		return currentGraphics.drawImage(img, ix, iy, bgcolor, observer);
	}

	public boolean drawImage(Image img, double x, double y,
							 double w, double h,
							 Color bgcolor, ImageObserver observer) {
		int ix = getX(x);//转换为Java AWT坐标值
		int iy = getY(y);//转换为Java AWT坐标值
		int iw = getDimensionX(w);//影像宽度
		int ih = getDimensionY(h);//影像高度
		return currentGraphics.drawImage(img, ix, iy, iw, ih, bgcolor, observer);
	}

	public boolean drawImage(Image img,
							 double dx1, double dy1, double dx2, double dy2,
							 double sx1, double sy1, double sx2, double sy2,
							 ImageObserver observer) {
		int idx1 = getX(dx1);//转换为Java AWT坐标值
		int idy1 = getY(dy1);//转换为Java AWT坐标值
		int idx2 = getX(dx2);//转换为Java AWT坐标值
		int idy2 = getY(dy2);//转换为Java AWT坐标值
		int isx1 = getX(sx1);//转换为Java AWT坐标值
		int isy1 = getY(sy1);//转换为Java AWT坐标值
		int isx2 = getX(sx2);//转换为Java AWT坐标值
		int isy2 = getY(sy2);//转换为Java AWT坐标值
		return currentGraphics.drawImage(img,
				idx1, idy1, idx2, idy2, isx1, isy1, isx2, isy2, observer);
	}

	public boolean drawImage(Image img,
							 double dx1, double dy1, double dx2, double dy2,
							 double sx1, double sy1, double sx2, double sy2,
							 Color bgcolor, ImageObserver observer) {
		int idx1 = getX(dx1);//转换为Java AWT坐标值
		int idy1 = getY(dy1);//转换为Java AWT坐标值
		int idx2 = getX(dx2);//转换为Java AWT坐标值
		int idy2 = getY(dy2);//转换为Java AWT坐标值
		int isx1 = getX(sx1);//转换为Java AWT坐标值
		int isy1 = getY(sy1);//转换为Java AWT坐标值
		int isx2 = getX(sx2);//转换为Java AWT坐标值
		int isy2 = getY(sy2);//转换为Java AWT坐标值
		return currentGraphics.drawImage(img,
				idx1, idy1, idx2, idy2, isx1, isy1, isx2, isy2,
				bgcolor, observer);
	}

	// 画素値に現在の色値をセットするメソッド
	public void putPixel(int i, int j) {
		int r = getColor().getRed() & 0xff;//赤色値
		int g = getColor().getGreen() & 0xff;//緑色値
		int b = getColor().getBlue() & 0xff;//青色値
		int a = 0xff000000 | (r << 16) | (g << 8) | b;//画素値をセット
		pixel[(pixelHeight - 1 - (j - yoffset)) * pixelWidth + (i - xoffset)] = a;
	}

	// 返回x的符号的方法
	public int Sign(int x) {
		if (x > 0) return 1;
		else if (x < 0) return -1;
		return 0;
	}

	public int Sign(double x) {
		if (x > 0.0) return 1;
		else if (x < 0.0) return -1;
		return 0;
	}

	// 线图(光栅化版本)实数布雷森纳姆算法
	public void rasterizeDrawLine(double x1, double y1, double x2, double y2) {
		double leftTopx, leftTopy;
		int ix1 = getX(x1);
		int iy1 = windowHeight - getY(y1);
		int ix2 = getX(x2);
		int iy2 = windowHeight - getY(y2);
		if (x1 < x2) {//当x1小于x2时
			leftTopx = x1;//将左上X坐标值设置为x1
			xoffset = ix1;//窗口中的偏移设置
		} else {
			leftTopx = x2;//将左上X坐标值设置为x2
			xoffset = ix2;//窗口中的偏移设置
		}
		if (y1 < y2) {
			leftTopy = y2;//将左上Y坐标值设置为y2
			yoffset = iy1;//窗口中的偏移设置
		} else {
			leftTopy = y1;//将左上Y坐标值设置为y1
			yoffset = iy2;//窗口中的偏移设置
		}

		int dx = ix2 - ix1;//沿X方向求差
		int dy = iy2 - iy1;//沿Y方向求差
		int adx = Math.abs(dx);//X方向差的绝对值
		int ady = Math.abs(dy);//Y方向差的绝对值
		pixelWidth = adx + 1;//要分配的数组的宽度
		pixelHeight = ady + 1;//要分配的数组的高度
		pixel = new int[pixelWidth * pixelHeight];
		for (int k = 0; k < pixelWidth * pixelHeight; k++)
			pixel[k] = 0x00000000; // 背景是完全透明的
		int sx = Sign(dx);
		int sy = Sign(dy);
		int x = ix1;//x从ix1开始
		int y = iy1;//y从iy1开始
		if (adx == 0) {//在直线的情况下Y =常数
			for (int j = 1; j <= ady; j++) {
				putPixel(x, y);
				y += sy;
			}
		} else if (ady == 0) {//在直线的情况下X =常数
			for (int i = 1; i <= adx; i++) {
				putPixel(x, y);
				x += sx;
			}
		} else if (adx > ady) {//X方向的倾斜度大时
			double d = (double) dy / (double) dx;
			double ty = (double) y;
			for (int i = 1; i <= adx; i++, x += sx) {
				putPixel(x, y);
				ty += sx * d;//添加Y轴前进量(请注意符号)
				if (Math.abs(ty - y) > Math.abs(ty - (y + sy)))
					y += sy;//将Y值沿线段方向前进
			}
		} else { // adx <= ady　当Y方向的倾斜度大或等于X方向时
			double d = (double) dx / (double) dy;
			double tx = (double) x;
			for (int j = 1; j <= ady; j++, y += sy) {
				putPixel(x, y);
				tx += sy * d;//添加X轴前进量(注意符号)
				if (Math.abs(tx - x) > Math.abs(tx - (x + sx)))
					x += sx;//使X的值沿线段的行进方向前进
			}
		}
		mis = new MemoryImageSource(pixelWidth, pixelHeight, pixel,
				0, pixelWidth);//创建内存映像源
		image = createImage(mis);//图像类数据创建
		drawImage(image, leftTopx, leftTopy, this);//描画
	}

	// 画线的另一种方法
	public void moveTo(double x, double y) {
		lastx = x;//设置最近的X位置
		lasty = y;//设置最近的Y位置
	}

	public void lineTo(double x, double y) {
		drawLine(lastx, lasty, x, y);//直線を描画
		lastx = x;
		lasty = y;//最近の位置を更新
	}
}
