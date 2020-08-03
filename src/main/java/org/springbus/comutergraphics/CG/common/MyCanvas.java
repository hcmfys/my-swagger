package org.springbus.comutergraphics.CG.common;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// MyCanvas.java
//		プログラム２−１
//			MyCanvas()コンストラクタ
//			createViewport(),setWindow(),setViewport(),resetViewport(),
//			getIntX(),getIntY(),viewX(),viewY(),getX(),getY(),
//			getDimensionX(),getDimensionY(),getColor(),setColor(),
//			getForeground(),setForeground(),getBackground(),setBakcground(),
//			drawLine(),drawRect(),fillRect(),clearRect(),
//			drawRoundRect(),fillRoundRect(),draw3DRect(),fill3DRect(),
//			drawOval(),fillOval(),drawArc(),fillArc(),drawPolyline(),
//			drawPolygon(),fillPolygon(),drawString(),
//			getFont(),setFont(),MyFont()メソッド
//		プログラム２−４による追加・変更
//			setViewport(),setClip()メソッド
//		プログラム２−５による追加・変更
//			rasterizeDrawLine(),putPixel(),Sign(),drawImage()メソッド
//		プログラム２−８による追加・変更
//			moveTo(),lineTo()メソッド
//		プログラム２−１２による追加・変更
//			getViewport(),getUserX(),getUserY()メソッド
//		プログラム３−１８による追加・変更
//			setOffScreenBuffer(),setGraphics()メソッド
//		プログラム４−１による追加・変更
//			setOffScreenBuffer(),copyArea(),create(),dispose(),
//			getToolkit()メソッド
//
//  ユーザ用のキャンバスクラス
//	ユーザ座標からビューポートへのマッピング
// 	ビューポートからJava AWT座標への変換
// 	ユーザ座標系での２次元図形の描画メソッドなど

import java.awt.*;
import java.awt.image.*;

public class MyCanvas extends Component {

	// ユーザ座標の範囲 （デフォルトは[-1,1]ｘ[-1,1]）
	protected double userMinx = -1;//ユーザ座標系のX軸の最小値
	protected double userMaxx = 1;//ユーザ座標系のX軸の最大値
	protected double userMiny = -1;//ユーザ座標系のY軸の最小値
	protected double userMaxy = 1;//ユーザ座標系のY軸の最大値
	// ビューポートの範囲　（デフォルトは[0,1]x[0,1]）
	protected double[] viewMinx;//ビューポートのX軸の最小値
	protected double[] viewMaxx;//ビューポートのX軸の最大値
	protected double[] viewMiny;//ビューポートのY軸の最小値
	protected double[] viewMaxy;//ビューポートのY軸の最大値
	final static int DefaultViewportMax = 256;//デフォルトのビューポート数
	protected int viewportMax = DefaultViewportMax;//ビューポートの数
	protected int viewportNum = 0;//現在のビューポートの数
	protected int currentViewport = 0;//現在のビューポートのインデックス
	// ウィンドウのサイズ
	final static int DefaultWindowSize = 256;//デフォルトのウィンドウのサイズ
	protected int windowWidth = DefaultWindowSize;//ウィンドウの横幅
	protected int windowHeight = DefaultWindowSize;//ウィンドウの縦幅
	// Java AWT Graphics and its Component
	protected Graphics graphics;//MyCanvasのGraphicsクラスデータ
	protected Component component;//MyCanvasのComponentクラスデータ
	protected Color currentFrontColor = Color.white;//現在の色 
	protected Color currentBackColor = Color.black;//現在の背景色

	// メモリイメージソース
	protected Image image;//メモリイメージソースで用いるImageクラスデータ
	protected MemoryImageSource mis;//メモリイメージソースデータ
	protected int[] pixel;//メモリイメージソースを作るための色値の入った配列
	protected int pixelWidth;//上述のpixelデータの横幅
	protected int pixelHeight;//上述のpixelデータの縦幅
	protected int xoffset;//画素データのウィンドウ内のX座標でのオフセット
	protected int yoffset;//画素データのウィンドウ内のY座標でのオフセット
	// MoveTo(x,y)とLineTo(x,y)のサポート用
	protected double lastx=0;//直前のX値
	protected double lasty=0;//直前のY値
	// オフスクリーン用およびプリンター用データ
	protected Graphics currentGraphics;//現在のグラフィックス
	protected Image offScreenImage;//オフスクリーン用領域
	protected Graphics offScreenGraphics;//オフスクリーン用グラフィックス
	protected Graphics printerGraphics;//プリンター用グラフィックスデータ
	protected Graphics userGraphics;//ユーザグラフィックス
	public final static int DEFAULT_GRAPHICS = 0;
	public final static int OFFSCREEN_GRAPHICS = 1;
	final static int PRINTER_GRAPHICS = 2;
	final static int USER_GRAPHICS = 3;
	// フォント用
	final static int DefaultFontSize = 12;

	// コンストラクタ
	// デフォルトのコンストラクタでは viewportMax = 256
	public MyCanvas(Component a){
		component = a;//MyCanvasクラス用のComponentをセット
		graphics = a.getGraphics();//MyCanvas用のGraphicsをセット
		currentGraphics = graphics;//現在のグラフィックスを設定
		windowWidth = a.getSize().width;//ウィンドウの横幅
		windowHeight = a.getSize().height;//ウィンドウの縦幅
		createViewport(DefaultViewportMax);//ビューポートの割り当て
	}
	public MyCanvas(Component a, int vMax){
		component = a;//MyCanvasクラス用のComponentをセット
		graphics = a.getGraphics();//MyCanvas用のGraphicsをセット
		currentGraphics = graphics;//現在のグラフィックスを設定
		windowWidth = a.getSize().width;//ウィンドウの横幅
		windowHeight = a.getSize().height;//ウィンドウの縦幅
		createViewport(vMax);//ビューポートの割り当て
	}
	private void createViewport(int max){
		currentViewport = 0;//ビューポートインデックスの初期値設定
		viewportMax = max;//ビューポート数の最大値を設定
		viewMinx = new double[viewportMax];//ビューポートのX軸の最小値配列
		viewMaxx = new double[viewportMax];//ビューポートのX軸の最大値配列
		viewMiny = new double[viewportMax];//ビューポートのY軸の最小値配列
		viewMaxy = new double[viewportMax];//ビューポートのY軸の最大値配列
		viewMinx[0] = viewMiny[0] = 0.0;//ビューポートの最小値は０
		viewMaxx[0] = viewMaxy[0] = 1.0;//ビューポートの最大値は１
		viewportNum = 1;//ビューポートの現在インデックスを１とする
	}
	//ウィンドウの横幅
	public int getWidth(){ return windowWidth; }
	//ウィンドウの縦幅
	public int getHeight(){	return windowHeight; }
	//オフスクリーンバッファ設定
	public void setOffScreenBuffer(){
		if (component == null) return;
		offScreenImage = 
			component.createImage(windowWidth,windowHeight);
		offScreenGraphics = offScreenImage.getGraphics();
	}
	public void setOffScreenBuffer(int width, int height){
		if (component == null) return;
		offScreenImage =
			component.createImage(width, height);
		offScreenGraphics = offScreenImage.getGraphics();
	}
	// エリアのコピー
	public void copyArea(double x, double y, double w, double h,
		double dx, double dy){
		int ix1 = getX(x);//コピー開始位置のｘ座標値をJava座標値に
		int iy1 = getY(y);//コピー開始位置のｙ座標値をJava座標値に
		int iw = getDimensionX(w);//横幅
		int ih = getDimensionY(h);//縦幅
		int ix2 = getDimensionX(dx);//行き先の相対的な座標値を変換
		ix2 *= Sign(dx); 
		int iy2 = getDimensionY(dy);//行き先の相対的な座標値を変換
		iy2 *= Sign(dy);
		currentGraphics.copyArea(ix1,iy1,iw,ih,ix2,iy2);
	}
	public void copyArea(double x, double y, double w, double h,
		double dx, double dy, boolean flag){
		int ix1 = getX(x);//コピー開始位置のｘ座標値をJava座標値に
		int iy1 = getY(y);//コピー開始位置のｙ座標値をJava座標値に
		int iw = getDimensionX(w);//横幅
		int ih = getDimensionY(h);//縦幅
		int ix2 = getDimensionX(dx);//行き先の相対的な座標値を変換
		ix2 *= Sign(dx); 
		int iy2 = getDimensionY(dy);//行き先の相対的な座標値を変換
		iy2 *= Sign(dy);
		currentGraphics.copyArea(ix1,iy1,iw,ih,ix2,iy2);
	}
	public void copyArea(int x, int y, int w, int h, int dx, int dy){
		currentGraphics.copyArea(x,y,w,h,dx,dy);
	}
	// ユーザグラフィックスの生成
	public Graphics create(double x, double y, double w, double h){
		int ix = getX(x);//グラフィックス領域位置のｘ座標値をJava座標値に
		int iy = getY(y);//グラフィックス領域位置のｙ座標値をJava座標値に
		int iw = getDimensionX(w);//横幅
		int ih = getDimensionY(h);//縦幅
		userGraphics = currentGraphics.create(ix,iy,iw,ih);
		return userGraphics;
	}
	public Graphics create(int x, int y, int w, int h){
		userGraphics = currentGraphics.create(x,y,w,h);
		return userGraphics;
	}
	// グラフィクスの棄却
	public void dispose(){
		if (currentGraphics == null) return;
		currentGraphics.dispose();
	}
	// Toolkitの獲得
	public Toolkit getToolkit(){ return component.getToolkit();	}
	// Visibility
	public void setVisible(boolean b){ component.setVisible(b);	}
	// カーソル
	public void setCursor(Cursor c){ component.setCursor(c); }
	// オフスクリーンイメージ
	public Image getOffScreenImage(){ return(offScreenImage); }
	// オフスクリーングラフィックス
	public Graphics getOffScreenGraphics(){	return(offScreenGraphics); }
	// 現在のグラフィックスの獲得
	public Graphics getGraphics(){ return(currentGraphics); }
	//プリンターグラフィックスの設定
	public void setPrinterGraphics(Graphics g){	printerGraphics = g; }
	// グラフィックスの設定
	public void setUserGraphics(Graphics g){ currentGraphics = g;}
	//現在グラフィックスの設定
	public void setGraphics(int index){
		if (index == DEFAULT_GRAPHICS) currentGraphics = graphics;
		else if (index == OFFSCREEN_GRAPHICS) 
			currentGraphics = offScreenGraphics;
		else if (index == PRINTER_GRAPHICS)
			currentGraphics = printerGraphics;
		else if (index == USER_GRAPHICS)
			currentGraphics = userGraphics;
	}
	// ウィンドウのサイズ設定メソッド
	public void setSize(int w, int h){
		windowWidth = w;//ウィンドウの横幅設定
		windowHeight = h;//ウィンドウの縦幅設定
		component.setSize(w,h);//ComponentクラスのsetSize()メソッドを利用
	}
	// ユーザ座標系の範囲の設定
	public void setWindow(double x1, double x2, double y1, double y2){
		userMinx = x1;//ウィンドウのX軸の最小値設定
		userMaxx = x2;//ウィンドウのX軸の最大値設定
		userMiny = y1;//ウィンドウのY軸の最小値設定
		userMaxy = y2;//ウィンドウのY軸の最大値設定
	}
	// ビューポートの設定（クリッピングする）
	public void setViewport(double x1, double x2, double y1, double y2){
		viewMinx[viewportNum] = x1;//現在のビューポートのX軸の最小値
		viewMaxx[viewportNum] = x2;//現在のビューポートのX軸の最大値
		viewMiny[viewportNum] = y1;//現在のビューポートのY軸の最小値
		viewMaxy[viewportNum] = y2;//現在のビューポートのY軸の最大値
		currentViewport = viewportNum;//現在のビューポートインデックスの設定
		viewportNum++;//ビューポートの数を増加させる
		setClip(x1,y1,x2,y2,true);//ビューポートでクリッピングを設定
	}
    // クリッピングしないビューポートの設定
	public void setNoClipViewport(double x1, double x2, double y1, double y2){
		viewMinx[viewportNum] = x1;//現在のビューポートのX軸の最小値
		viewMaxx[viewportNum] = x2;//現在のビューポートのX軸の最大値
		viewMiny[viewportNum] = y1;//現在のビューポートのY軸の最小値
		viewMaxy[viewportNum] = y2;//現在のビューポートのY軸の最大値
		currentViewport = viewportNum;//現在のビューポートインデックスの設定
		viewportNum++;//ビューポートの数を増加させる
	}
	// ビューポートのリセット
	public void resetViewport(){
		currentViewport = 0;//ビューポートインデックスを０に戻す
		viewMinx[0] = viewMiny[0] = 0.0;//ビューポートの最小値０
		viewMaxx[0] = viewMaxy[0] = 1.0;//ビューポートの最大値１
		viewportNum = 1;//ビューポートの数を１とする
	}
	// ビューポート座標をJava AWT座標にマッピングするメソッド
	public int getIntX(double x){
		return (int)(windowWidth * x);//ウィンドウの横幅倍する
	}
	public int getIntY(double y){//yでなく1-yとする
		return (int)(windowHeight * (1-y));//ウィンドウの縦幅倍する
	}
	// ユーザ座標をビューポート座標にマッピングするメソッド
	public double viewX(double x){
		double s = (x - userMinx)/(userMaxx - userMinx);
		double t = viewMinx[currentViewport] + 
			s * (viewMaxx[currentViewport]-viewMinx[currentViewport]);
		return t;
	}
	public double viewY(double y){
		double s = (y - userMiny)/(userMaxy - userMiny);
		double t = viewMiny[currentViewport] + 
			s * (viewMaxy[currentViewport]-viewMiny[currentViewport]);
		return t;
	}
	// ユーザ座標からJava AWT座標を得るメソッド
	public int getX(double x){
		double xx = viewX(x);//xをビューポートにマッピング
		int ix = getIntX(xx);//ビューポートをJava座標系にマッピング
		return ix;
	}
	public int getY(double y){
		double yy = viewY(y);//yをビューポートにマッピング
		int iy = getIntY(yy);//ビューポートをJava座標系にマッピング
		return iy;
	}
	// Dimensionを得るメソッド
	public int getDimensionX(double w){
		double x = viewMaxx[currentViewport] - viewMinx[currentViewport];
		x *= windowWidth * w / (userMaxx-userMinx);
		return ((int)Math.abs(x));
	}
	public int getDimensionY(double h){
		double y = viewMaxy[currentViewport] - viewMiny[currentViewport];
		y *= windowHeight * h / (userMaxy-userMiny);
		return ((int)Math.abs(y));
	}
	// 逆マッピング
	// Java AWT座標からビューポートに逆マッピング
	public int getViewport(int ix, int iy){
		if (viewportNum == 1) return 0; // デフォルトのビューポート
		double s = (double)(ix)/(double)windowWidth;
		double t = (double)(windowHeight-iy)/(double)windowHeight;
		for (int i=0; i < viewportNum ; i++){
			if (s >= viewMinx[i] && s <= viewMaxx[i] &&
			    t >= viewMiny[i] && t <= viewMaxy[i])
				return i;
		}
		return 0;
	}
	// ビューポートからユーザ座標系に逆マッピング（X座標）
	public double getUserX(int ix, int v){
		double t = (double)(ix)/(double)windowWidth;
		double x = userMinx + 
			(t-viewMinx[v]) / (viewMaxx[v]-viewMinx[v]) * 
			(userMaxx-userMinx);
		return x;
	}
	// ビューポートからユーザ座標系に逆マッピング（Y座標）
	public double getUserY(int iy, int v){
		double t = (double)(windowHeight-iy)/(double)windowHeight;
		double y = userMiny + 
			(t-viewMiny[v]) / (viewMaxy[v]-viewMiny[v]) * 
			(userMaxy-userMiny);
		return y;
	}
	// 色指定関連のメソッド
	public Color getColor(){
		return currentFrontColor;//現在の色を検索
	}
	public void setColor(Color c){
		currentGraphics.setColor(c);//現在の色を設定
		currentFrontColor = c;
	}
	public void setColor(double r, double g, double b){
		Color c = new Color((float)r,(float)g,(float)b);
		currentGraphics.setColor(c);
		//現在の色を設定
		currentFrontColor = c;
	}
	public Color getForeground(){
		return currentFrontColor;//前面色を検索
	}
	public void setForeground(Color c){
		component.setForeground(c);//前面色を設定
		currentFrontColor = c;
	}
	public void setForeground(double r, double g, double b){
		Color c = new Color((float)r,(float)g,(float)b);
		component.setForeground(c);//前面色を設定
		currentFrontColor = c;
	}
	public Color getBackground(){
		return currentBackColor;//背景色を検索
	}
	public void setBackground(Color c){
		component.setBackground(c);//背景色を設定
		currentBackColor = c;
	}
	public void setBackground(double r, double g, double b){
		Color c = new Color((float)r,(float)g,(float)b);
		component.setBackground(c);//背景色を設定
		currentBackColor = c;
	}
	// クリッピング
	public void clipRect(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.clipRect(x0,y0,width,height);
	}
	public void setClip(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.setClip(x0,y0,width,height);
	}
	public void setClip(double x1, double y1, double x2, double y2, 
		boolean flag){
		int ix1 = getIntX(x1);//４隅のどこかの点のビューポートｘ座標値をJava座標値に
		int iy1 = getIntY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getIntX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getIntY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.setClip(x0,y0,width,height);
	}

	//
	// 描画メソッド
	//
	// 線分の描画
	public void drawLine(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//Java AWT座標値に変換
		int iy1 = getY(y1);//Java AWT座標値に変換
		int ix2 = getX(x2);//Java AWT座標値に変換
		int iy2 = getY(y2);//Java AWT座標値に変換
		currentGraphics.drawLine(ix1,iy1,ix2,iy2);
	}
	// 矩形の描画
	public void drawRect(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.drawRect(x0,y0,width,height);
	}
	// 矩形の塗りつぶし
	public void fillRect(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.fillRect(x0,y0,width,height);
	}
	// 矩形で領域をクリア
	public void clearRect(double x1, double y1, double x2, double y2){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.clearRect(x0,y0,width,height);
	}
	// 角に丸みのある矩形の描画
	public void drawRoundRect(double x1, double y1, double x2, double y2, 
		double arcW, double arcH){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		int iarcWidth = getDimensionX(arcW);//角の丸みの横サイズ
		int iarcHeight = getDimensionY(arcH);//角の丸みの縦サイズ
		currentGraphics.drawRoundRect(x0,y0,width,height,
			iarcWidth,iarcHeight);
	}
	// 角に丸みのある矩形の塗りつぶし
	public void fillRoundRect(double x1, double y1, double x2, double y2,
		double arcW, double arcH){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		int iarcWidth = getDimensionX(arcW);//角の丸みの横サイズ
		int iarcHeight = getDimensionY(arcH);//角の丸みの縦サイズ
		currentGraphics.fillRoundRect(x0,y0,width,height,
			iarcWidth,iarcHeight);
	}
	// 浮かび上がる矩形の描画
	public void draw3DRect(double x1, double y1, double x2, double y2,
		boolean raised){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.draw3DRect(x0,y0,width,height,raised);
	}
	// 浮かび上がる矩形の塗りつぶし
	public void fill3DRect(double x1, double y1, double x2, double y2,
		boolean raised){
		int ix1 = getX(x1);//４隅のどこかの点のｘ座標値をJava座標値に
		int iy1 = getY(y1);//x1と同じ点のｙ座標値をJava座標値に
		int ix2 = getX(x2);//x1に対し対角線上の隅のｘ座標値をJava座標値に
		int iy2 = getY(y2);//y1に対し対角線上の隅のｙ座標値をJava座標値に
		int width = Math.abs(ix1-ix2)+1;//横幅を計算
		int height = Math.abs(iy1-iy2)+1;//縦幅を計算
		int x0 = (ix1 <= ix2) ? ix1 : ix2;//開始点のX座標（左上）
		int y0 = (iy1 <= iy2) ? iy1 : iy2;//開始点のY座標（左上）
		currentGraphics.fill3DRect(x0,y0,width,height,raised);
	}		
	// だ円の描画　（中心(x,y), 半径(xr,yr))
	public void drawOval(double x, double y, double xr, double yr){
		int ix = getX(x);//だ円の中心のJava AWTでのX座標
		int iy = getY(y);//だ円の中心のJava AWTでのY座標
		int ixr = getDimensionX(xr);//半径の横幅
		int iyr = getDimensionY(yr);//半径の縦幅
		int x0 = ix - ixr;//だ円を囲む矩形の左上隅（X）
		int y0 = iy - iyr;//だ円を囲む矩形の左上隅（X）
		currentGraphics.drawOval(x0,y0,2*ixr,2*iyr);
	}
	// だ円の塗りつぶし　（中心(x,y), 半径(xr,yr))
	public void fillOval(double x, double y, double xr, double yr){
		int ix = getX(x);//だ円の中心のJava AWTでのX座標
		int iy = getY(y);//だ円の中心のJava AWTでのY座標
		int ixr = getDimensionX(xr);//半径の横幅
		int iyr = getDimensionY(yr);//半径の縦幅
		int x0 = ix - ixr;//だ円を囲む矩形の左上隅（X）
		int y0 = iy - iyr;//だ円を囲む矩形の左上隅（X）
		currentGraphics.fillOval(x0,y0,2*ixr,2*iyr);
	}
	// 円弧の描画　（中心(x,y) 半径(xr,yr))
    	public void drawArc(double x, double y, double xr, 
		double yr, double startAngle, double arcAngle){
		int ix = getX(x);//円弧の中心のJava AWTでのX座標
		int iy = getY(y);//円弧の中心のJava AWTでのY座標
		int ixr = getDimensionX(xr);//半径の横幅
		int iyr = getDimensionY(yr);//半径の縦幅
		int x0 = ix - ixr;//円弧を囲む矩形の左上隅（X）
		int y0 = iy - iyr;//円弧を囲む矩形の左上隅（X）
		int is = (int)(90-(startAngle+arcAngle));//開始アングル（デグリー）
		int ia = (int)arcAngle;//扇形の弧の角度（デグリー）
		currentGraphics.drawArc(x0,y0,2*ixr,2*iyr,is,ia);
	}
	// 扇形の塗りつぶし　（中心(x,y) 半径(xr,yr))
    	public void fillArc(double x, double y, double xr, 
		double yr, double startAngle, double arcAngle){
		int ix = getX(x);//扇形の中心のJava AWTでのX座標
		int iy = getY(y);//扇形の中心のJava AWTでのY座標
		int ixr = getDimensionX(xr);//半径の横幅
		int iyr = getDimensionY(yr);//半径の縦幅
		int x0 = ix - ixr;//扇形を囲む矩形の左上隅（X）
		int y0 = iy - iyr;//扇形を囲む矩形の左上隅（X）
		int is = (int)(90-(startAngle+arcAngle));//開始アングル（デグリー）
		int ia = (int)arcAngle;//扇形の弧の角度（デグリー）
		currentGraphics.fillArc(x0,y0,2*ixr,2*iyr,is,ia);
	}	
	// 折れ線の描画	
	public void drawPolyline(double[] x, double[] y, int numPoints){
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i=0; i < numPoints ; i++){//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.drawPolyline(ix,iy,numPoints);
	}
	// 多角形の描画
	public void drawPolygon(double[] x, double[] y, int numPoints){
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i=0; i < numPoints ; i++){//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.drawPolygon(ix,iy,numPoints);
	}
	// 多角形の塗りつぶし
	public void fillPolygon(double[] x, double[] y, int numPoints){
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for (int i=0; i < numPoints ; i++){//Java AWT座標値に変換
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		currentGraphics.fillPolygon(ix,iy,numPoints);
	}
	// 文字列の描画
	public void drawString(String str, double x, double y){
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		currentGraphics.drawString(str,ix,iy);
	}
	// フォントの設定
	public Font getFont(){
		return currentGraphics.getFont();//現在のGraphicsクラスのフォントを検索
	}
	public void setFont(Font f){
		currentGraphics.setFont(f);//現在のGraphicsクラスのフォント設定
	}
	public double getFontSize(Font f){
		int isize = f.getSize();
		double size = (double)isize / DefaultFontSize;
		return size;
	}
	public Font MyFont(String name, int style, double size){
		if (size <= 0) size = 1.0;//サイズが負ならデフォルト値
		int isize = (int)(DefaultFontSize*size);//フォントをスケーリング
		Font f = new Font(name,style,isize);
		return f;
	}
	public FontMetrics getFontMetrics(Font f){
		return component.getFontMetrics(f);
	}
	public double getStringWidth(String s, Font f){
		int width = getFontMetrics(f).stringWidth(s);
		double w = (double) width / windowWidth * (userMaxx-userMinx);
		return w;
	}
	public double getHeight(Font f){
		int height = getFontMetrics(f).getHeight();
		double h = (double) height / windowHeight;
		return h;
	}
	// 画像の描画
    public boolean drawImage(Image img, double x, double y, 
		ImageObserver observer){
		if (currentGraphics == null) return false;
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		return currentGraphics.drawImage(img,ix,iy,observer);
	}		
   	public boolean drawImage(Image img, double x, double y, 
		double w, double h, ImageObserver observer){
		if (currentGraphics == null) return false;
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		int iw = getDimensionX(w);//getX(w)-getX(0);//画像の横幅
		int ih = getDimensionY(h);//getY(0)-getY(h);//画像の縦幅
		return currentGraphics.drawImage(img,ix,iy,iw,ih,observer);
	}
   	public boolean drawImage(Image img, double x, double y, 
		Color bgcolor, ImageObserver observer){
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		return currentGraphics.drawImage(img,ix,iy,bgcolor,observer);
	}		
   	public boolean drawImage(Image img, double x, double y, 
		double w, double h, 
		Color bgcolor, ImageObserver observer){
		int ix = getX(x);//Java AWT座標値に変換
		int iy = getY(y);//Java AWT座標値に変換
		int iw = getDimensionX(w);//画像の横幅
		int ih = getDimensionY(h);//画像の縦幅
		return currentGraphics.drawImage(img,ix,iy,iw,ih,bgcolor,observer);
	}		
	public boolean drawImage(Image img,
		double dx1, double dy1, double dx2, double dy2,
		double sx1, double sy1, double sx2, double sy2,
		ImageObserver observer){
		int idx1 = getX(dx1);//Java AWT座標値に変換
		int idy1 = getY(dy1);//Java AWT座標値に変換
		int idx2 = getX(dx2);//Java AWT座標値に変換
		int idy2 = getY(dy2);//Java AWT座標値に変換
		int isx1 = getX(sx1);//Java AWT座標値に変換
		int isy1 = getY(sy1);//Java AWT座標値に変換
		int isx2 = getX(sx2);//Java AWT座標値に変換
		int isy2 = getY(sy2);//Java AWT座標値に変換
		return currentGraphics.drawImage(img,
			idx1, idy1, idx2, idy2,	isx1, isy1, isx2, isy2, observer);
	}
	public boolean drawImage(Image img,
		double dx1, double dy1, double dx2, double dy2,
		double sx1, double sy1, double sx2, double sy2,
		Color bgcolor, ImageObserver observer){
		int idx1 = getX(dx1);//Java AWT座標値に変換
		int idy1 = getY(dy1);//Java AWT座標値に変換
		int idx2 = getX(dx2);//Java AWT座標値に変換
		int idy2 = getY(dy2);//Java AWT座標値に変換
		int isx1 = getX(sx1);//Java AWT座標値に変換
		int isy1 = getY(sy1);//Java AWT座標値に変換
		int isx2 = getX(sx2);//Java AWT座標値に変換
		int isy2 = getY(sy2);//Java AWT座標値に変換
		return currentGraphics.drawImage(img,
			idx1, idy1, idx2, idy2, isx1, isy1, isx2, isy2,
			bgcolor, observer);
	}
	// 画素値に現在の色値をセットするメソッド
	public void putPixel(int i, int j){
		int r = getColor().getRed() & 0xff;//赤色値
		int g = getColor().getGreen() & 0xff;//緑色値
		int b = getColor().getBlue() & 0xff;//青色値
		int a = 0xff000000|(r<<16)|(g<<8)|b;//画素値をセット
		pixel[(pixelHeight-1-(j-yoffset))*pixelWidth+(i-xoffset)] = a;
	}
	// xの符号を返すメソッド
	public int Sign(int x){
		if (x > 0) return 1;
		else if (x < 0) return -1;
		return 0;
	}
	public int Sign(double x){
		if (x > 0.0) return 1;
		else if (x < 0.0) return -1;
		return 0;
	}
	// 線画（ラスタライズバージョン）実数型Bresenham'sアルゴリズム
	public void rasterizeDrawLine(double x1, double y1, double x2, double y2){
		double leftTopx, leftTopy;
		int ix1 = getX(x1);
		int iy1 = windowHeight - getY(y1);
		int ix2 = getX(x2);
		int iy2 = windowHeight - getY(y2);
		if (x1 < x2){//x1の方がx2より小さいとき
			leftTopx = x1;//左上のX座標値をx1にセット
			xoffset = ix1;//ウィンドウの中でのオフセット設定
		}
		else {
			leftTopx = x2;//左上のX座標値をx2にセット
			xoffset = ix2;//ウィンドウの中でのオフセット設定
		}
		if (y1 < y2){
			leftTopy = y2;//左上のY座標値をy2にセット
			yoffset = iy1;//ウィンドウの中でのオフセット設定
		}
		else {
			leftTopy = y1;//左上のY座標値をy1にセット
			yoffset = iy2;//ウィンドウの中でのオフセット設定
		}	

		int dx = ix2-ix1;//X方向の差分をとる
		int dy = iy2-iy1;//Y方向の差分をとる
		int adx = Math.abs(dx);//X方向の差分の絶対値
		int ady = Math.abs(dy);//Y方向の差分の絶対値
		pixelWidth = adx + 1;//割り当てる配列の横幅
		pixelHeight = ady + 1;//割り当てる配列の縦幅
		pixel = new int[pixelWidth*pixelHeight];
		for (int k=0 ; k < pixelWidth * pixelHeight ; k++)
			pixel[k] = 0x00000000; // 背景は完全透明
		int sx = Sign(dx);
		int sy = Sign(dy);
		int x = ix1;//ｘはix1よりスタート
		int y = iy1;//ｙはiy1よりスタート
		if (adx == 0){//Y=定数　という直線の場合
			for (int j=1 ; j <= ady ; j++ ){
				putPixel(x,y);
				y += sy;
			}
		}
		else if (ady == 0){// X=定数　という直線の場合	
			for (int i=1 ; i <= adx ; i++ ){
				putPixel(x,y);
				x += sx;
			}
		}
		else if (adx > ady){//X方向の傾きが大きい場合
			double d = (double)dy/(double)dx;
			double ty = (double)y;
			for (int i=1 ; i <= adx ; i++, x += sx){
				putPixel(x,y);
				ty += sx*d;//Y軸の進む量を加える（符号に注意）
				if (Math.abs(ty-y) > Math.abs(ty-(y+sy)))
					y += sy;//Yの値を線分の進行方向に進める
			}
		}
		else { // adx <= ady　Y方向の傾きが大きいかX方向と等しい場合
			double d = (double)dx/(double)dy;
			double tx = (double)x;
			for (int j=1 ; j <= ady ; j++, y += sy){
				putPixel(x,y);
				tx += sy*d;//X軸の進む量を加える（符号に注意）
				if (Math.abs(tx-x) > Math.abs(tx-(x+sx)))
					x += sx;//Xの値を線分の進行方向に進める
			}
		}
		mis = new MemoryImageSource(pixelWidth,pixelHeight,pixel,
			0, pixelWidth);//メモリイメージソース作成
		image = createImage(mis);//Imageクラスのデータ作成
		drawImage(image,leftTopx,leftTopy,this);//描画
	}
	// 直線描画の別のメソッド
	public void moveTo(double x, double y){
		lastx = x;//もっとも最近のX位置をセット
		lasty = y;//もっとも最近のY位置をセット
	}
	public void lineTo(double x, double y){
		drawLine(lastx,lasty,x,y);//直線を描画
		lastx = x; lasty = y;//最近の位置を更新
	}
}
