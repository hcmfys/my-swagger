package org.springbus.comutergraphics.CG.C1_2.flickerTest;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// flickerApplet.java
// 単純なアニメーションで画像のちらつきが起きる例
//	プログラム１−３

import org.springbus.comutergraphics.CG.common.JApplet;

import java.awt.*;

public class flickerApplet extends JApplet { //flickerAppletクラスはAppletクラスを継承

	private int w; // アプレットの横幅
	private int h; // アプレットの縦幅
	private int radius = 20; // ボールの半径
	private float[] dx; // ボールの横方向の単位時間当たりの進み
	private float[] dy; // ボールの縦方向の単位時間当たりの進み
	// -1.0 <= dx, dy <= 1.0
	private int[] lastx, lasty; //ボールの最新の位置
	private int[] x,y; // ボールの新しい位置	
	private static int numBalls = 20;//ボールの総数
	final static float SPEED = 5.0f;//ボールのスピード
	final static double EPSILON = 1.0E-5;//十分小さい数

	public void init(){//アプレット開始時に実行されるメソッド
		Dimension d = getSize();//アプレットのサイズ取得
		w = d.width;//アプレットの横幅
		h = d.height;//アプレットの縦幅
		radius = Math.min(w,h)/10;//ボールの半径計算
		lastx = new int[numBalls];//ボールの最新の横軸の位置データ領域確保
		lasty = new int[numBalls];//ボールの最新の縦軸の位置データ領域確保
		dx = new float[numBalls];//ボールの単位時間あたりの横方向の進み量のデータ領域確保
		dy = new float[numBalls];//ボールの単位時間あたりの縦方向の進み量のデータ領域確保
		x = new int[numBalls];//ボールの横軸の現在位置データ領域の確保
		y = new int[numBalls];//ボールの縦軸の現在位置データ領域の確保
		for (int i=0 ; i < numBalls ; i++ ){ //ボールの総数だけループ
			lastx[i] = (int)(Math.random()*w);//ボールの横軸方向の初期位置をランダムに設定
			lasty[i] = (int)(Math.random()*h);//ボールの縦軸方向の初期位置をランダムに設定
			//ボールの横軸方向に単位時間あたり進む量(-1.0<=dx[i]<=1.0)
			dx[i] = (float)(2*Math.random()-1.0);
			//ボールの縦軸方向に単位時間あたり進む量(-1.0<=dy[i]<=1.0)
			dy[i] = (float)(2*Math.random()-1.0);
			double t = dx[i]*dx[i] + dy[i]*dy[i];
			if (Math.abs(t) < EPSILON) {dx[i] = 1; dy[i] = 0;}
			else {//ボールの単位時間に進む大きさを１とする
				t = Math.sqrt(t);
				dx[i] /= t; dy[i] /= t;
			}
		} 
	}

	public void paint(Graphics g){//アプレットの描画メソッド
		g.setColor(Color.black);//現在色を黒色に設定
		g.fillRect(0,0,w,h);//黒色の長方形を塗りつぶす
		g.setColor(Color.yellow);//現在色を黄色に設定
		// ボールの数だけ円の塗りつぶしを実行
		for (int i=0; i < numBalls ; i++ )  
			g.fillOval(lastx[i],lasty[i],radius,radius);
		// 次時刻でのボールの位置を計算
		for (int i =  0 ; i < numBalls ; i++ ){
			x[i] = (int)(lastx[i] + SPEED*dx[i]); 
			y[i] = (int)(lasty[i] + SPEED*dy[i]);
			if (x[i] < 0) { x[i] = 0; dx[i] = -dx[i];}//ボールが左横壁に衝突
			else if (x[i] > w-radius) { x[i] = w-radius; dx[i] = -dx[i]; }//ボールが右横壁に衝突
			if (y[i] < 0) { y[i] = 0; dy[i] = -dy[i]; }//ボールが上壁に衝突
			else if (y[i] > h-radius) { y[i] = h-radius; dy[i] = -dy[i]; }//ボールが下壁に衝突
			lastx[i] = x[i]; lasty[i] = y[i];//最新の位置を更新
		}
		repaint(50);//50ミリ秒待って再描画
	}

	public  static void  main(String[] args) {
		flickerApplet g=new flickerApplet();
		g.display();
	}
}
