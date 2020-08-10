package org.springbus.comutergraphics.CG.C2_4.fillPolygon;


// fillPolygon.java
//填充多边形
//用鼠标交互式输入多边形
//此时，使用线的橡皮筋显示并双击鼠标。
//填充是标准的扫描线算法。
//使用存储区数组和活动边缘列表数据结构。
//多边形可以是任何多边形。
//为此，请使用Vector类（非常数数组类型类）。
//请注意不要与Vector3类，Vector4类等混淆。
//程序2-13

import org.springbus.comutergraphics.CG.C2_1.MultiViewport.MultiViewport;
import org.springbus.comutergraphics.CG.common.JApplet;
import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.util.*;//ベクトルクラス利用のため
import java.awt.*;//AWTパッケージ
import java.awt.image.*;//メモリイメージソース利用のため
import java.awt.event.*;//マウスイベント利用のため
import java.applet.Applet;//アプレット利用のため

public class fillPolygon extends JApplet
	implements MouseListener, MouseMotionListener {

	protected MyCanvas m;	// MyCanvas类的数据
	// 扫描线处理数据
	protected activeEdgeListEntry[] edgeData = null;//注册多边形面数据
	protected activeEdgeList[] bucket = null;//铲斗阵列
	protected activeEdgeList activeHeader = null;//活动边缘列表标题
	protected int numEdge = 0;//多边形的边数
	// 鼠标相关数据
	protected boolean isFirstClicked = true;//第一次点击鼠标
	protected boolean isDoubleClicked = false;//双击标志
	protected boolean isSingleClicked = false;//单击标志
	// 绘图区
	protected int width, height;//小程序的宽度和高度
	protected Image image = null;//内存图像源的图像区域
	protected MemoryImageSource mis = null;//内存映像源
	protected int[] pixel = null;//内存图像源颜色排列
	protected int pixelWidth;//内存图像源的宽度
	protected int pixelHeight;//内存图像源的垂直宽度
	protected int xoffset;//像素数据窗口内X坐标的偏移
	protected int yoffset;//像素数据窗口中Y坐标的偏移
	protected double leftTopx;//MyCanvas类的drawImage（）方法的起点
	protected double leftTopy;//MyCanvas类的drawImage（）方法的起点
	// 用于绘制多边形边的数据
	protected double x0, y0;// 第一名
	protected double lastx, lasty; // 前一个多边形的顶点
	protected double movingx, movingy;//橡皮筋的顶点
	protected int numPoints = 0; // 多边形的顶点数
	protected boolean isPolygonMode = true;// 多边形绘图模式
	protected Vector lines = new Vector(256,256);//向量类

	public void init(){//小程序初始化
		m = new MyCanvas(this);//MyCanvas对象生成
		addMouseListener(this);//设置鼠标侦听器
		addMouseMotionListener(this);//设置鼠标动作侦听器
		Dimension d = getSize();//获取小程序大小
		width = d.width;//小程序的宽度
		height = d.height;//小程序高度
	}

	public void initData(){
		isFirstClicked = true;//标记为第一次单击鼠标
		isPolygonMode = true;//在多边形周围绘制模式标志
		numPoints = 0;//多边形的顶点数
		bucket = new activeEdgeList[height+1];//桶
		for (int i=0; i < height+1 ; i++) bucket[i] = null;
		if (lines.size() > 0) lines.removeAllElements();
	}

  public void paint(Graphics g) { // 画图方法
		g.clearRect(0, 0, getWidth(), getHeight());
    if (m != null) {
      if (isFirstClicked) {
        initData();
        Font f = m.MyFont(m.getFont().getName(), Font.BOLD + Font.ITALIC, 1.5);
        m.setFont(f);
        m.drawString("点击开始", -0.5, 0.2);
        m.drawString("双击填写", -0.9, -0.15);
      }
      m.setBackground(new Color(220, 220, 220));
      m.setColor(Color.black); //将前景色设置为黑色
      for (int i = 0; i < lines.size(); i++) { // 围绕多边形
        Line l = (Line) lines.elementAt(i);
        m.drawLine(l.x1, l.y1, l.x2, l.y2);
      }
      if (isPolygonMode && !isFirstClicked) { // 橡皮筋
        m.drawLine(lastx, lasty, movingx, movingy);
      }
      if ((!isPolygonMode) && (image != null)) { //填
        m.drawImage(image, leftTopx, leftTopy, this);
      }
    }
	}

	// 将输入数据注册到活动边沿列表
	public void registerActiveEdgeEntry(){
		numEdge = lines.size();//多边形的边数
		edgeData = new activeEdgeListEntry[numEdge];
		for (int i=0 ; i < numEdge ; i++) edgeData[i] = new activeEdgeListEntry();
		int LARGE = 0x0fffffff;
		int xmin=LARGE, xmax= -1, ymin=LARGE, ymax= -1;
		double dxmin=LARGE, dxmax= -1, dymin=LARGE, dymax = -1;
		for (int i=0 ; i < numEdge ; i++){
			Line l = (Line)lines.elementAt(i);
			int ix1 = m.getX(l.x1); int iy1 = height - m.getY(l.y1);
			int ix2 = m.getX(l.x2); int iy2 = height - m.getY(l.y2);
			edgeData[i].topx = 0; edgeData[i].name = i; edgeData[i].next = null;
			if (iy1 > iy2) {
				edgeData[i].isHorizontal = false;
				edgeData[i].topy = iy1; edgeData[i].topx = ix1;
				edgeData[i].x = ix1; edgeData[i].boty = iy2;
				edgeData[i].delta = (double)(-(ix2-ix1))/(iy2-iy1);
				if (iy2 < ymin) { ymin = iy2; dymin = l.y2;}
				if (iy1 > ymax) { ymax = iy1; dymax = l.y1;}
			}
			else if (iy1 < iy2) {
				edgeData[i].isHorizontal = false;
				edgeData[i].topy = iy2; edgeData[i].topx = ix2;
				edgeData[i].x = ix2; edgeData[i].boty = iy1;
				edgeData[i].delta = (double)(-(ix1-ix2))/(iy1-iy2);
				if (iy1 < ymin) { ymin = iy1; dymin = l.y1;}
				if (iy2 > ymax) { ymax = iy2; dymax = l.y2;}
			}
			else {
				edgeData[i].isHorizontal = true;
				if (iy1 < ymin) { ymin = iy1; dymin = l.y1;}
				if (iy1 > ymax) { ymax = iy1; dymax = l.y1;}
			}
			// Min-Max 計算
			if (ix1 < ix2){
				if (ix1 < xmin) { xmin = ix1; dxmin = l.x1;}
				if (ix2 > xmax) { xmax = ix2; dxmax = l.x2;}
			}
			else if (ix2 < ix1){
				if (ix2 < xmin) { xmin = ix2; dxmin = l.x2;}
				if (ix1 > xmax) { xmax = ix1; dxmax = l.x1;}
			}
			else {
				if (ix1 < xmin) { xmin = ix1; dxmin = l.x1;}
				if (ix1 > xmax) { xmax = ix1; dxmax = l.x1;}
			}
		}
		// 为内存图像源分配整数数组
		pixelWidth = xmax - xmin + 1; pixelHeight = ymax - ymin + 1;
		pixel = new int[pixelWidth*pixelHeight];
		for (int k=0 ; k < pixelWidth*pixelHeight ; k++ )
			pixel[k] = 0x00000000;//初期値は完全透明色
		xoffset = xmin; yoffset = ymin;
		leftTopx = dxmin;//MyCanvas.drawImage用
		leftTopy = dymax;//MyCanvas.drawImage用
	}

	//存储桶初始化（包括插入排序）
	public void bucketSort(){
		for (int i=0 ; i < lines.size() ; i++){
			Line l = (Line)lines.elementAt(i);
			if (edgeData[i].isHorizontal) continue;//不处理水平线
			int yt = edgeData[i].topy;
			if (bucket[yt] == null) {//如果存储桶中还没有元素
				bucket[yt] = new activeEdgeList(edgeData[i]);
				continue;
			}
			bucket[yt].insert(edgeData[i]);//插入排序
		}
	}

	//多边形扫描转换
	public void scanPolygon(){
		activeHeader = null;
		for (int y=height-1 ; y >= 0 ; y--){
			if (bucket[y] != null){
				makeActiveEdgeList(bucket[y],y); processActiveEdgeList(y);
			}
			else if (activeHeader != null && activeHeader.header != null){
				processActiveEdgeList(y);
			}
		}
	}

	//主动边缘列表处理
	public void processActiveEdgeList(int y){
		int xleft, xright; double xl, xr;
		activeEdgeListEntry left = activeHeader.header;
		activeEdgeListEntry right = left.next;
		if (right == null) return;
		while (true){
			xl = left.x; xr = right.x;
			xleft = (int) xl; xright = (int) (xr+0.5);
			if (xleft <= xright){
				fillScanline(xleft,xright,y);} //填充扫描线
			left.x += left.delta;//增加左侧的X坐标
			right.x += right.delta;//增加右侧的X坐标
			if (left.boty >= y-1 && right.boty >= y-1){//用最小Y填充
				xleft = (int)left.x; xright = (int)(right.x+0.5);
				if (xleft <= xright)
					fillScanline(xleft,xright,y-1);
			}
			if (left.boty >= y-1){//从活动边缘列表中删除左侧
				activeHeader.remove(left);
			}
			if (right.boty >= y-1){//从活动边缘列表中删除右边缘
				activeHeader.remove(right);
			}
			left = right.next;//选择列表中的下一对
			if (left == null) break;//没有更多元素
			right = left.next;
			if (right == null) throw new NullPointerException();
		}
		activeHeader.traverse();//线交叉时的列表重建
	}

	//创建活动边缘列表
	public void makeActiveEdgeList(activeEdgeList list, int y){
		if (activeHeader == null) 	activeHeader = list;
		else    activeHeader = activeHeader.merge(list,y);
	}

	// 用于平铺图案生成
 	public boolean isTilePattern(int i, int j){
		if (i % 8 == 0 || j % 8 == 0 || i % 8 == 1
		 || j % 8 == 1 || i % 8 == 2 || j % 8 == 2 )
			return true;//瓷砖的砂浆部分
		return false;
	}

	// 为像素值设置适当的颜色
	public void putPixel(int i, int j){
		int r, g, b;
		if (isTilePattern(i,j)) { r = g = b = 0; }
		else {
			r = (int)(Math.random()*255);
			g = (int)(Math.random()*255);
			b = (int)(Math.random()*255);
		}
		int a = 0xff000000|(r<<16)|(g<<8)|b;//画素色値
		pixel[(pixelHeight-1-(j-yoffset))*pixelWidth+(i-xoffset)] = a;
	}

	//在扫描线X方向上填充像素列
	public void fillScanline(int xleft, int xright, int y){
		for (int x = xleft ; x <= xright ; x++ ) putPixel(x,y);
	}

	// 填充多边形（主要方法）
	public void scanLineFillPolygon(){
		registerActiveEdgeEntry();//边缘套准
		bucketSort();//将分类插入存储桶
		scanPolygon();//使用边缘相干填充多边形
		repaint();
	}

	//获取鼠标事件
	public void mousePressed(MouseEvent e){
		int ix = e.getX();//鼠标的X位置（Java坐标系）
		int iy = e.getY();//鼠标Y位置（Java坐标系）
		if (isPolygonMode){
			double x, y; // 当前鼠标位置（用户坐标系）
			if (e.getClickCount() >= 2){// 双击
				isDoubleClicked = true;
				lines.addElement(new Line(lastx,lasty,x0,y0));
				isPolygonMode = false;
				scanLineFillPolygon();//开始填充过程
				if (numEdge > 2){
					mis = new MemoryImageSource(pixelWidth,
						pixelHeight,pixel,0,
						pixelWidth);
					image = createImage(mis);
				}
			}
			else {
				if (isFirstClicked) {//第一次点击
					isFirstClicked = false;
					lastx = x0 = m.getUserX(ix,
						m.getViewport(ix,iy));
					lasty = y0 = m.getUserY(iy,
						m.getViewport(ix,iy));
					movingx = lastx; movingy = lasty;
				}
				else {//第二次及以后的单次点击
					x = m.getUserX(ix, m.getViewport(ix,iy));
					y = m.getUserY(iy, m.getViewport(ix,iy));
					lines.addElement(new Line(lastx,lasty,x,y));
					lastx = x; lasty = y;
				}
				numPoints++;
			}
			repaint();
		}
	}

	// 获取橡皮筋的鼠标当前位置
	public void mouseMoved(MouseEvent e){
		if (!isFirstClicked && isPolygonMode){
			int ix,iy;
			ix = e.getX();//鼠标的X位置（Java坐标系）
			iy = e.getY();//鼠标Y位置（Java坐标系）
			movingx = m.getUserX(ix, m.getViewport(ix,iy));
			movingy = m.getUserY(iy, m.getViewport(ix,iy));
			repaint();
		}
	}

	// 空方法
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}

	// 向量定义的线段数据（Java非常数元素数组类型）
	static class Line {
		public double x1, y1;
		public double x2, y2;
		//构造函数
		public Line(double x1, double y1, double x2, double y2){
			this.x1 = x1; this.y1 = y1;
			this.x2 = x2; this.y2 = y2;
		}
	}
	public static void main(String[] args) {
		fillPolygon m = new fillPolygon();
		m.display();
	}

}
