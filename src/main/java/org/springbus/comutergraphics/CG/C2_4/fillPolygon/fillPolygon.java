package org.springbus.comutergraphics.CG.C2_4.fillPolygon;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// fillPolygon.java
// 多角形の塗りつぶし
// 対話的に多角形をマウスで入力
// この際，線のラバーバンド表示とマウスのダブルクリックを利用。
// 塗りつぶしは，標準的なスキャンラインアルゴリズム。
// バケット配列とアクティブエッジリストデータ構造を使う。
// 多角形は何角形でもよい。
// このためVectorクラス（不定数配列型クラス）を利用。
// 3章以降のVector3クラス，Vector4クラスなどと混同しないよう注意されたい。
//	プログラム２−１３

import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.util.*;//ベクトルクラス利用のため
import java.awt.*;//AWTパッケージ
import java.awt.image.*;//メモリイメージソース利用のため
import java.awt.event.*;//マウスイベント利用のため
import java.applet.Applet;//アプレット利用のため

public class fillPolygon extends Applet 
	implements MouseListener, MouseMotionListener {

	protected MyCanvas m;	// MyCanvasクラス用のデータ
	// スキャンライン処理用のデータ
	protected activeEdgeListEntry[] edgeData = null;//多角形の辺データ登録
	protected activeEdgeList[] bucket = null;//バケット配列
	protected activeEdgeList activeHeader = null;//アクティブエッジリストのヘッダー
	protected int numEdge = 0;//多角形の辺の数
	// マウス関連データ
	protected boolean isFirstClicked = true;//マウスの最初のクリック
	protected boolean isDoubleClicked = false;//ダブルクリック用フラグ
	protected boolean isSingleClicked = false;//シングルクリック用フラグ
	// 描画領域
	protected int width, height;//アプレットの横幅と縦幅
	protected Image image = null;//メモリイメージソース用画像領域
	protected MemoryImageSource mis = null;//メモリイメージソース
	protected int[] pixel = null;//メモリイメージソースの配色用配列
	protected int pixelWidth;//メモリイメージソースの横幅
	protected int pixelHeight;//メモリイメージソースの縦幅
	protected int xoffset;//pixelデータのウィンドウ内でのX座標のオフセット
	protected int yoffset;//pixelデータのウィンドウ内でのY座標のオフセット
	protected double leftTopx;//MyCanvasクラスのdrawImage()メソッドの開始点
	protected double leftTopy;//MyCanvasクラスのdrawImage()メソッドの開始点
	// 多角形の辺の描画用データ
	protected double x0, y0;// 一番初めの位置
	protected double lastx, lasty; // 直前の多角形の頂点
	protected double movingx, movingy;//ラバーバンド用の頂点
	protected int numPoints = 0; // 多角形の頂点数
	protected boolean isPolygonMode = true;// 多角形の描画モード
	protected Vector lines = new Vector(256,256);//ベクトルクラス

	public void init(){//アプレットの初期化
		m = new MyCanvas(this);//MyCanvasオブジェクト生成
		addMouseListener(this);//マウスリスナーを設定
		addMouseMotionListener(this);//マウスのモーションリスナーを設定
		Dimension d = getSize();//アプレットのサイズ取得
		width = d.width;//アプレットの横幅
		height = d.height;//アプレットの縦幅
	}

	public void initData(){
		isFirstClicked = true;//最初のマウスクリック用のフラグ
		isPolygonMode = true;//多角形の周囲の描画モードのフラグ
		numPoints = 0;//多角形の頂点数
		bucket = new activeEdgeList[height+1];//バケット
		for (int i=0; i < height+1 ; i++) bucket[i] = null;
		if (lines.size() > 0) lines.removeAllElements();
	}

	public void paint(Graphics g){//描画メソッド
		if (isFirstClicked){
			initData();
			Font f = m.MyFont(m.getFont().getName(), Font.BOLD+Font.ITALIC, 1.5);
			m.setFont(f);
			m.drawString("クリックで開始",-0.5,0.2);
			m.drawString("ダブルクリックで塗りつぶし",-0.9,-0.15);
		}
		m.setBackground(new Color(220,220,220));
		m.setColor(Color.black);//前面色を黒色に設定
		for (int i=0 ; i < lines.size() ; i++ ) {//多角形の周囲
			Line l = (Line)lines.elementAt(i); m.drawLine(l.x1,l.y1,l.x2,l.y2);
		}
		if (isPolygonMode && !isFirstClicked){//ラバーバンド
			m.drawLine(lastx,lasty,movingx,movingy);
		}
		if ((!isPolygonMode) && (image != null)){// 塗りつぶし
			m.drawImage(image,leftTopx,leftTopy,this);
		}
	}

	// アクティブエッジリストへ入力データを登録
	public void registerActiveEdgeEntry(){
		numEdge = lines.size();//多角形の辺の数
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
		// メモリイメージソース用の整数配列の割り当て
		pixelWidth = xmax - xmin + 1; pixelHeight = ymax - ymin + 1;
		pixel = new int[pixelWidth*pixelHeight];
		for (int k=0 ; k < pixelWidth*pixelHeight ; k++ )
			pixel[k] = 0x00000000;//初期値は完全透明色
		xoffset = xmin; yoffset = ymin;
		leftTopx = dxmin;//MyCanvas.drawImage用
		leftTopy = dymax;//MyCanvas.drawImage用
	}

	// バケットの初期化（挿入ソートを含む）
	public void bucketSort(){
		for (int i=0 ; i < lines.size() ; i++){
			Line l = (Line)lines.elementAt(i);
			if (edgeData[i].isHorizontal) continue;//水平線は処理しない
			int yt = edgeData[i].topy;
			if (bucket[yt] == null) {//まだバケットに要素がない場合
				bucket[yt] = new activeEdgeList(edgeData[i]);
				continue;
			}
			bucket[yt].insert(edgeData[i]);//挿入ソート
		}
	}

	//多角形のスキャンコンバージョン
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

	//アクティブエッジリストの処理
	public void processActiveEdgeList(int y){
		int xleft, xright; double xl, xr;
		activeEdgeListEntry left = activeHeader.header;
		activeEdgeListEntry right = left.next;
		if (right == null) return;
		while (true){
			xl = left.x; xr = right.x; 
			xleft = (int) xl; xright = (int) (xr+0.5);
			if (xleft <= xright){ 
				fillScanline(xleft,xright,y);} //スキャンラインを塗りつぶし
			left.x += left.delta;//左の辺のX座標を増加させる
			right.x += right.delta;//右の辺のX座標を増加させる
			if (left.boty >= y-1 && right.boty >= y-1){//Yの最小値での塗りつぶし
				xleft = (int)left.x; xright = (int)(right.x+0.5);
				if (xleft <= xright)
					fillScanline(xleft,xright,y-1);
			}
			if (left.boty >= y-1){//左の辺をアクティブエッジリストから削除
				activeHeader.remove(left);
			}
			if (right.boty >= y-1){//右の辺をアクティブエッジリストから削除
				activeHeader.remove(right);
			}
			left = right.next;//リストの次のペアを選択
			if (left == null) break;//もう要素はない
			right = left.next;
			if (right == null) throw new NullPointerException();
		}
		activeHeader.traverse();//線がクロスしている場合のリスト再構築
	}

	// アクティブエッジリストの作成
	public void makeActiveEdgeList(activeEdgeList list, int y){
		if (activeHeader == null) 	activeHeader = list;
		else    activeHeader = activeHeader.merge(list,y);
	}

	// タイルパターンの生成用
 	public boolean isTilePattern(int i, int j){
		if (i % 8 == 0 || j % 8 == 0 || i % 8 == 1 
		 || j % 8 == 1 || i % 8 == 2 || j % 8 == 2 )
			return true;//タイルのモルタル部分
		return false;
	}

	// 画素値に適当な色をセット
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

	//スキャンラインのX方向での画素列の塗りつぶし
	public void fillScanline(int xleft, int xright, int y){
		for (int x = xleft ; x <= xright ; x++ ) putPixel(x,y);
	}

	// 多角形の塗りつぶし（メインメソッド）
	public void scanLineFillPolygon(){
		registerActiveEdgeEntry();//辺の登録
		bucketSort();//バケットへの挿入ソート
		scanPolygon();//辺コヒーレンスを利用した多角形の塗りつぶし
		repaint();
	}

	//マウスのイベント取得
	public void mousePressed(MouseEvent e){
		int ix = e.getX();//マウスのX位置(Java座標系)
		int iy = e.getY();//マウスのY位置(Java座標系)
		if (isPolygonMode){
			double x, y; // マウスの現在位置（ユーザ座標系）
			if (e.getClickCount() >= 2){// ダブルクリック
				isDoubleClicked = true;
				lines.addElement(new Line(lastx,lasty,x0,y0));
				isPolygonMode = false;
				scanLineFillPolygon();//塗りつぶし処理の開始
				if (numEdge > 2){
					mis = new MemoryImageSource(pixelWidth,
						pixelHeight,pixel,0,
						pixelWidth);
					image = createImage(mis);
				}
			}
			else {
				if (isFirstClicked) {//はじめてのクリック
					isFirstClicked = false;
					lastx = x0 = m.getUserX(ix,
						m.getViewport(ix,iy));
					lasty = y0 = m.getUserY(iy,
						m.getViewport(ix,iy));
					movingx = lastx; movingy = lasty;
				}
				else {//２回目以降のシングルクリック
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

	// ラバーバンド用にマウスの現在位置をゲット
	public void mouseMoved(MouseEvent e){
		if (!isFirstClicked && isPolygonMode){
			int ix,iy;
			ix = e.getX();//マウスのX位置(Java座標系)
			iy = e.getY();//マウスのY位置(Java座標系)
			movingx = m.getUserX(ix, m.getViewport(ix,iy));
			movingy = m.getUserY(iy, m.getViewport(ix,iy));
			repaint();
		}
	}

	// ダミーメソッド
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}

	// ベクトル（Javaの不定数要素配列型）で定義する線分データ
	static class Line {
		public double x1, y1;
		public double x2, y2;
		//コンストラクタ
		public Line(double x1, double y1, double x2, double y2){
			this.x1 = x1; this.y1 = y1;
			this.x2 = x2; this.y2 = y2;
		}
	}
}
