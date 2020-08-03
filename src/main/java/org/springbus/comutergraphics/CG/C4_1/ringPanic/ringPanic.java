package org.springbus.comutergraphics.CG.C4_1.ringPanic;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// ringPanic.java
// 不規則に弾むリングと板の衝突検出するアニメーション
// 板の位置はマウスでクリックしドラッグすることで変更可能
//	プログラム４−３

import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.awt.*;//AWTパッケージ
import java.awt.image.*;//メモリイメージソース利用のため
import java.awt.event.*;//マウスイベント利用のため
import java.applet.*;//Appletクラス利用のため
import java.net.*;//URLクラス利用のため

public class ringPanic extends Applet implements Runnable, 
	MouseListener, MouseMotionListener {

	final static double EPSILON = 1.0E-10;//十分小さい数
	final static int HITMAX = 30;//衝突回数の最大値
	protected long TIMEMAX = 60000;//タイムリミット（ミリ秒）
	//******************* 背景画像　********************************
	protected Image brickImage = null;
	protected String backImage = "brick.jpg";
	//******************* リングに関する変数 ***********************
	protected double bw = 0.05;//リングの横幅（ユーザ座標系）
	protected double bh = 0.05;//リングの縦幅（ユーザ座標系）
	protected int ringH;//リングの縦幅
	protected int ringW;//リングの横幅
	protected Ring[] ring;//リングクラスのオブジェクト
	protected int numRing = 10;//リングの数
	//******************* 板に関する変数 ***************************
	protected Plate plate;
	protected double pw = 0.2; // 板の横幅（ユーザ座標系）
	protected double ph = 0.1; // 板の縦幅（ユーザ座標系）
	protected int plateW;//板の横幅
	protected int plateH;//板の縦幅
	protected long numHit = 0;//衝突回数のカウント
	//******************* マウス関連のデータ ***********************
	protected double lastmx=0, lastmy=0;//マウスの最新の位置
	protected int mousex=0, mousey=0;//マウスのイベントで拾った座標
	protected boolean pressOut = false;//マウスが押されたかどうか
	//******************** その他の変数　***************************
	protected MyCanvas m;	// MyCanvasクラス用のデータ
	protected int width=400, height=400;// ウィンドウのサイズ
	protected double dwidth=2.0, dheight=2.0;//ウィンドウのサイズ（ユーザ座標系）
	protected Toolkit toolkit;//衝突を知らせる効果音のため
	protected boolean isBeep = false; // 衝突音のフラグ
	protected Image image = null;//オフスクリーン画像
	protected boolean isFirst = true;
	protected Thread thread=null;//スレッド
	protected long totalTime;//時間
	protected long initTime;//開始時間
	protected String timeString = null;//計測時間表示用
	protected AudioClip finalAudio;
	protected String sakebi = "sakebi.au";

	public void init(){//初期化
		loadAudioImage();
		m = new MyCanvas(this);//MyCanvasクラスのオブジェクト生成
		width = m.getWidth();//横幅決定
		height = m.getHeight();//縦幅決定
		toolkit = m.getToolkit();//Toolkitオブジェクト生成
		addMouseListener(this);//マウスリスナー設定
		addMouseMotionListener(this);//マウスモーションリスナー設定
		m.setOffScreenBuffer(2*width,height);//横幅２倍のオフスクリーン
		image = m.getOffScreenImage();//オフスクリーン画像のオブジェクトゲット
		try {
			String s1,s2,s3,s4;
			s1 = getParameter("numRing");
			if (s1 != null)	numRing = Integer.valueOf(s1).intValue();
			s2 = getParameter("timeLimit");
			if (s2 != null) TIMEMAX = (long)(1000*
				Integer.valueOf(s2).intValue());
			s3 = getParameter("ringSpeed");
			if (s3 != null) Ring.RING_INIT_SPEED = (double)(0.01*
				Integer.valueOf(s3).intValue());
			s4 = getParameter("chime");
			if (s4 != null) {
				if (s4.equals("ON")) isBeep = true;
				else if (s4.equals("OFF")) isBeep = false;
			}
		}
		catch (NumberFormatException e){System.out.println(e);}
		initRing();//リングの初期化
		initPlate();//板の初期化
		createBackground();//背景の初期化
		initTime = System.currentTimeMillis();//ゲーム開始時刻セット
	}

	public void loadAudioImage(){//オーディオと背景画像のロード
		if (getParameter("backImage") != null)
			backImage = getParameter("backImage");
		try {
			finalAudio = getAudioClip(new URL(getCodeBase(),sakebi));
			brickImage = getImage(new URL(getCodeBase(),backImage));
		}
		catch (MalformedURLException e){}
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(brickImage,1);
		try {
			mt.waitForID(1);
		}
		catch(InterruptedException e){}
	}

	public void createBackground(){//背景をオフスクリーン右バッファに書く
		m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		m.drawImage(brickImage,1.0,1.0,dwidth,dheight,this);
		m.copyArea(width,0,width,height,-width,0);
	}

	public void initPlate(){ //板の初期化
		plateW = m.getDimensionX(pw);
		plateH = m.getDimensionY(ph);
		plate = new Plate(this,pw,ph,plateW,plateH);
	}

	public boolean isInside(double x, double y){
		if ((plate.lastx < x) && (x < (plate.lastx+plate.w)) &&
		    ((plate.lasty-plate.h) < y)  && (y < plate.lasty))
			return true;
		return false;
	}

	public void initRing(){//リングデータの初期化
		ring = new Ring[numRing];
		double w,h;
		for (int i=0; i < numRing ; i++){
			int r = (int)(Math.random()*255);
			int g = (int)(Math.random()*255);
			int b = (int)(Math.random()*255);
			//明るい色が得られるまで繰り返す
			if (r < 120 && g < 120 && b < 120) { i--; continue;}
			Color c = new Color(r,g,b);
			w = h = bw*(1+4*Math.random());
			ringW = m.getDimensionX(w);
			ringH = m.getDimensionY(h);
			ring[i] = new Ring(this,w,h,ringW,ringH,c);
		}
	}

	public void start(){//アプレットの開始=スレッドの開始
		if (thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop(){//アプレットの終了=スレッドの中断
		if (thread != null){
			thread.interrupt();
			thread = null;
		}
	}

	public void run(){//スレッドの実行
		while (thread != null){
			//現在スレッドを休止しプロセッサを他のスレッドに譲る
			try {
				thread.sleep(30);//30ミリ秒休止
			}
			catch (InterruptedException e){}
			repaint();
		}
		thread = null;
	}

	public void advanceRing(){// リングの次の位置を計算
		for (int i=0 ; i < numRing ; i++ ){
			ring[i].move();
			int c = isHit(i);
			if (c < 0) { ring[i].hit = false; continue;}
			// リングと板が衝突 
			if (ring[i].hit) continue;
			ring[i].hit = true;
 			if (c == 1){//板の上面と衝突
				ring[i].y = plate.lasty + 1.2*ring[i].h;
				ring[i].dy = -ring[i].dy;
				countUp(i);
			}
			else if (c == 2){//板の下面と衝突
				ring[i].y = plate.lasty - plate.h-ring[i].h/5;
				ring[i].dy = -ring[i].dy;
				countUp(i);
			}
			else if (c == 3){//板の左側面と衝突
				ring[i].x = plate.lastx - 1.2*ring[i].w;
				ring[i].dx = -ring[i].dx;
				countUp(i);
			}
			else if (c == 4){//板の右側面と衝突
				ring[i].x = plate.lastx + plate.w+ring[i].w/5;
				ring[i].dx = -ring[i].dx;
				countUp(i);
			}
			ring[i].changeRingSpeed(true);
		}
	}

	public void countUp(int i){//衝突回数カウント
		numHit++;
		if (isBeep) toolkit.beep();
		ring[i].lastx = ring[i].x;
		ring[i].lasty = ring[i].y;
	}

	public void advancePlate(){//板の位置の移動
		if (!pressOut) return;
		double x = m.getUserX(mousex,m.getViewport(mousex,mousey));
		double y = m.getUserY(mousey,m.getViewport(mousex,mousey));
		plate.move(x,y);
		lastmx = plate.lastmx;
		lastmy = plate.lastmy;
	}

	public int isHit(int i){//リングと板の衝突検証
		int hit = -1;
		double tmin = 1.0E10;
		double xold = ring[i].x0+ring[i].w/2;
		double yold = ring[i].y0-ring[i].h/2;
		double xnew = ring[i].x+ring[i].w/2;
		double ynew = ring[i].y-ring[i].h/2;
		double x1 = plate.lastx-ring[i].w/4;
		double y1 = plate.lasty-plate.h-ring[i].h/4;
		double x2 = plate.lastx+plate.w+ring[i].w/4;
		double y2 = plate.lasty+ring[i].h/4;
		double s, t, x, y;
		if (Math.abs(xnew-xold) > EPSILON){
			t = (x1-xold)/(xnew-xold);
			if (0 <= t && t <= 1){
				y = yold + t*(ynew-yold);
				s = (y-y1)/(y2-y1);
				if (0 <= s && s <= 1){//左と衝突
					if (t < tmin){
						tmin = t;hit = 3;
					}
				}
			}
			t = (x2-xold)/(xnew-xold);
			if (0 <= t && t <= 1){
				y = yold + t*(ynew-yold);
				s = (y-y1)/(y2-y1);
				if (0 <= s && s <= 1){//右と衝突
					if (t < tmin){
						tmin = t;hit = 4;
					}
				}
			}
		}
		if (Math.abs(ynew-yold)>EPSILON){
			t = (y1-yold)/(ynew-yold);
			if (0 <= t && t <= 1){
				x = xold+t*(xnew-xold);
				s = (x-x1)/(x2-x1);
				if (0 <= s && s <= 1){
					if (t < tmin){//下と衝突
						tmin = t;hit = 2;
					}
				}
			}
			t = (y2-yold)/(ynew-yold);
			if (0 <= t && t <= 1){
				x = xold+t*(xnew-xold);
				s = (x-x1)/(x2-x1);
				if (0 <= s && s <= 1){
					if (t < tmin){//上と衝突
						tmin = t;hit = 1;
					}
				}
			}
		}
		return hit;
	}

	public void update(Graphics g){//update()メソッドのオーバーライド
		paint(g);
	}

	public void paint(Graphics g){//描画メソッド
		//まずオフスクリーンに描画
		if (isFinal()) paintFinal();
		else paintObject();
		//最後に手前に描画する
		m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		m.drawImage(image,-1.0,1.0,this);
	}

	public void paintObject(){//リングと板の描画
		advanceRing();
		advancePlate();
		paintOffScreen();
		paintFrontScreen();
	}

	private boolean isFinal(){//ゲーム終了の判定
		totalTime = System.currentTimeMillis() - initTime;
		if ((totalTime > TIMEMAX)||(numHit > HITMAX))
		return true;
		return false;
	}

	public void paintFinal(){//ゲーム終了時の描画
		m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		m.drawImage(brickImage,-1,1,dwidth,dheight,this);
		m.setColor(Color.white);
		Font f = m.MyFont(m.getFont().getName(),
			m.getFont().getStyle(),2.0);
		m.setFont(f);
		if (numHit > HITMAX)
			m.drawString("ゲームオーバー",-0.4,0.15);
		else	m.drawString("あなたの勝ち！",-0.4,0);
		if (isFirst){
			isFirst = false;
			if (numHit > HITMAX){
				double t2 = (double)totalTime/1000.0;
				int time = (int)t2;
				timeString = "時間 = "+String.valueOf(time)+" 秒";
				finalAudio.play();
			}
		}
		if (numHit > HITMAX)
			m.drawString(timeString,-0.4,-0.2);
	}

	public void paintOffScreen(){//リングと板をオフスクリーンに描画
		int ix, iy;
		m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		//リングと板の古い位置の矩形だけ背景で塗りつぶす
		for (int i=0; i < numRing ; i++){
			ix = m.getX(ring[i].x0);
			iy = m.getY(ring[i].y0);
			m.copyArea(width+ix,iy,
				ring[i].width, ring[i].height, -width, 0);
		}
		ix = m.getX(plate.x0);
		iy = m.getY(plate.y0);
		m.copyArea(width+ix,iy,
			plate.width, plate.height,-width,0);
		//リングと板の現在位置をオフスクリーンに描画
		for (int i=0; i < numRing ; i++){
			m.drawImage(ring[i].image,
				ring[i].lastx,ring[i].lasty,
				ring[i].w,ring[i].h,this);
		}
		m.drawImage(plate.image,
			plate.lastx,plate.lasty,plate.w,plate.h,this);
		m.drawString(Long.toString(numHit),
			plate.lastx+plate.w/4,plate.lasty-2*plate.h/3);
	}

	public void paintFrontScreen(){
		int ix, iy, iw, ih;
		m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		for (int i=0; i < numRing ; i++){
			ix = m.getX(ring[i].lefttopx);
			iy = m.getY(ring[i].lefttopy);
			iw = m.getDimensionX(ring[i].movew);
			ih = m.getDimensionY(ring[i].moveh);

			Graphics gR = m.create(ix,iy,iw,ih);
			if (gR == null) return;
			gR.drawImage(image,-ix,-iy,this);
			gR.dispose();
		}
		ix = m.getX(plate.lefttopx);
		iy = m.getY(plate.lefttopy);
		iw = m.getDimensionX(plate.movew);
		ih = m.getDimensionY(plate.moveh);

		Graphics gP = m.create(ix,iy,iw,ih);
		if (gP == null) return;
		gP.drawImage(image,-ix,-iy,this);
		gP.dispose();
	}

	public void mousePressed(MouseEvent e){
		int ix = e.getX();//マウスのX位置(Java座標系)
		int iy = e.getY();//マウスのY位置(Java座標系)
		double x, y; // マウスの現在位置（ユーザ座標系）
		x = m.getUserX(ix,m.getViewport(ix,iy));
		y = m.getUserY(iy,m.getViewport(ix,iy));
		if (isInside(x,y)) {//マウスが板の中でクリックされた
			mousex = ix;mousey = iy;
			lastmx = x;lastmy = y;
			pressOut = true;
		}
		else pressOut = false;
	}

	public void mouseReleased(MouseEvent e){
		pressOut = false;
		plate.dx = 0; plate.dy = 1; plate.speed = 0.1;
	}

	public void mouseDragged(MouseEvent e){
		int ix = e.getX();//マウスのX位置(Java座標系)
		int iy = e.getY();//マウスのY位置(Java座標系)
		mousex = ix;
		mousey = iy;
	}

	// ダミーメソッド
	public void mouseMoved(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	// リングクラス
	static class Ring {
		public static double RING_INIT_SPEED = 0.02;
		public double w, h;//リングのユーザ座標での横幅と縦幅
		public int width, height;//リングの横幅，縦幅
		public boolean hit;//直前に板と衝突した
		public double ringSpeedX;//リングのX方向のスピード
		public double ringSpeedY;//リングのX方向のスピード
		public Color color;//リングの色
		public double dx; // リングの横方向の単位時間当たりの進み
		public double dy; // リングの縦方向の単位時間当たりの進み
		public double x,y; // リングの新しい位置
		public double x0,y0;//リングの前の位置
		public double lastx, lasty; //リングの最新の位置
		public double lefttopx, lefttopy;//リングの軌跡の左上
		public double movew, moveh;//リングの軌跡の幅
		public Image image;//リング用オフスクリーンバッフ
		public Component component;//リングの描画コンポーネント
		public Ring(Component comp, double w, double h,
			int width, int height, Color c){
			component = comp;
			this.w = w; this.h = h;
			this.width = width; this.height = height;
			hit = false;
			//リングの横軸方向の初期位置をランダムに設定
			x0 = lastx = 2*Math.random()-1.0;
			//リングの縦軸方向の初期位置をランダムに設定
			y0 = lasty = 2*Math.random()-1.0;
			//リングの横軸方向に単位時間あたり進む量
			dx = 2*Math.random()-1.0;
			//リングの縦軸方向に単位時間あたり進む量
			dy = 2*Math.random()-1.0;
			double t = dx*dx + dy*dy;
			if (Math.abs(t) < EPSILON) {dx = 1; dy = 0;}
			ringSpeedX = RING_INIT_SPEED;
			ringSpeedY = RING_INIT_SPEED;
			color = c;
			// メモリイメージソースで作ったリング用の描画領域
			image = getRingImage(c);
		}
		public Image getRingImage(Color c){
			//現在のリング色でメモリイメージソースを作成
			int pixel = 0x00000000;
			int wr = width/2;
			int hr = height/2;
			int wwr = width/3;
			int hhr = height/3;
			int F, E;
			int R = c.getRed() & 0xff;
			int G = c.getGreen() & 0xff;
			int B = c.getBlue() & 0xff;
			int A = 0xff;
			int[] rgb = new int[width*height];
			for (int y = 0 ; y < height ; y++ )
				for (int x = 0 ; x < width ; x++ ){
					F = hr*hr*(x-wr)*(x-wr)+
				    	wr*wr*(y-hr)*(y-hr)-
				    	wr*wr*hr*hr;
					E = hhr*hhr*(x-wr)*(x-wr)+
				    	wwr*wwr*(y-hr)*(y-hr)-
				    	wwr*wwr*hhr*hhr;
					if (F <= 0 && E >= 0)
						pixel = (A<<24)|(R<<16)|(G<<8)|B;
					else    pixel = 0x00000000;
					rgb[width*y+x] = pixel;
			}
			MemoryImageSource mis = new 
				MemoryImageSource(width,height,rgb,0,width);
			return component.createImage(mis);
		}
		public void changeRingSpeed(boolean flag){
			//リングのスピードを変える
			ringSpeedX = 
				RING_INIT_SPEED * (1 + (int)(5*Math.random()));
			ringSpeedY = 
				RING_INIT_SPEED * (1 + (int)(5*Math.random()));
			if (flag) return;
			dx = 2*Math.random()-1.0;
			dy = 2*Math.random()-1.0;
			if (dx > 0) dx = Math.abs(dx);
			else dx = -Math.abs(dx);
			if (dy > 0) dy = Math.abs(dy);
			else dy = -Math.abs(dy);
		}
		public void move(){
			x0 = lastx; y0 = lasty;
			x = lastx + ringSpeedX * dx;
			y = lasty + ringSpeedY * dy;
			if (x < -1) { 
				x = -1; dx = -dx;
				changeRingSpeed(false); 
			}
			else if (x > 1-w) { 
				x = 1-w; dx = -dx;
				changeRingSpeed(false); 
			}
			if (y < -(1-h)) { 
				y = -(1-h); dy = -dy;
				changeRingSpeed(false);
			}
			else if (y > 1.0) { 
				y = 1.0; dy = -dy; 
				changeRingSpeed(false);
			}
			lefttopx = Math.min(x, lastx);
			lefttopy = Math.max(y, lasty);
			movew = w + Math.abs(ringSpeedX*dx);
			moveh = h + Math.abs(ringSpeedY*dy);
			lastx = x;
			lasty = y;
		}
	}

	static class Plate {
		public double speed = 0.1;
		public Color color = Color.cyan;//板の色
		public int width;//板の横幅
		public int height;//板の縦幅
		public double w = 0.2; // 板の横幅（ユーザ座標系）
		public double h = 0.1; // 板の縦幅（ユーザ座標系）
		public double dx;//板の横方向の単位時間当たりの進み
		public double dy;//板の縦方向の単位時間当たりの進み
		public double lastx, lasty;//板の最新の位置
		public double x0, y0;//板の前の位置
		public double x, y;//板の新しい位置
		public double lefttopx, lefttopy; //板の軌跡の左上
		public double movew, moveh;//板の軌跡の幅
		public double lastmx, lastmy;//マウスの最新位置
		public Image image;//板の画像
		public Component component;//板の描画コンポーネント
		public Plate(Component comp, double w, double h, 
			int width, int height){
			this.component = comp;
			this.w = w;
			this.h = h;
			this.width = width;
			this.height = height;
			this.x0 = this.lastx = -this.w/2;
			this.y0 = this.lasty = this.h/2;
			this.lefttopx = this.lastx;
			this.lefttopy = this.lasty;
			this.movew = this.w;
			this.moveh = this.h;
			this.dx = this.dy = 0;
			this.x = this.y = 0;
			this.image = getPlateImage();
		}
		public Image getPlateImage(){
		//現在の板の色でメモリイメージソースを作成
			int pixel = 0x00000000;
			int wr = width/2;
			int hr = height/2;
			int F, R, G, B;
			int A = 0xff;
			int[] rgb = new int[width*height];
			for (int y = 0 ; y < height ; y++ ){
				for (int x = 0 ; x < width ; x++ ){
					if ((x < 2) || (x > (width-3))) { 
						R = 255; G = B = 0;
					}
					else if ((y < 2)||( y > (height-3))){
						R = 255; G = B = 0;
					}
					else {
						R = color.getRed() & 0xff;
						G = color.getGreen() & 0xff;
						B = color.getBlue() & 0xff;
					}
					pixel = (A<<24)|(R<<16)|(G<<8)|B;
					rgb[width*y+x] = pixel;
				}
			}
			MemoryImageSource mis = new 
				MemoryImageSource(width,height,rgb,0,width);
			return component.createImage(mis);
		}
		public void move(double px, double py){
			// マウスの現在位置（ユーザ座標系）
			x0 = lastx; y0 = lasty;
			x = lastx + (px - lastmx);
			y = lasty + (py - lastmy);
			dx = px - lastmx;
			dy = py - lastmy;
			lastmx = px;
			lastmy = py;
			lefttopx = Math.min(x, lastx);
			lefttopy = Math.max(y, lasty);
			movew = w + Math.abs(dx);
			moveh = h + Math.abs(dy);
			lastx = x; lasty = y;
			if (lastx < -1) { lastx = -1; }
			else if (lastx > (1-w)) { lastx = 1-w; }
			if (lasty < -(1-h)) {	lasty = -(1-h);}
			else if (lasty > 1.0) { lasty = 1.0;}
		}
	}
}
