package org.springbus.comutergraphics.CG.C1_2.flickerTest;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// noFlickerApplet.java
// 単純なアニメーションでの画像のちらつきをなくす例
//	プログラム１−４

import org.springbus.comutergraphics.CG.common.JApplet;

import java.awt.*;

public class noFlickerApplet extends JApplet {
  // noFlickerAppletクラスはAppletクラスを継承

  private int w; // アプレットの横幅
  private int h; // アプレットの縦幅
  private int radius = 20; // ボールの半径
  private float[] dx; // ボールの横方向の単位時間当たりの進み
  private float[] dy; // ボールの縦方向の単位時間当たりの進み
  // -1.0 <= dx, dy <= 1.0
  private int[] lastx, lasty; // ボールの最新の位置
  private int[] x, y; // ボールの新しい位置
  private static int numBalls = 20; // ボールの総数
  static final float SPEED = 5.0f; // ボールのスピード
  static final double EPSILON = 1.0E-5; // 十分小さい数
  // オフスクリーンバッファ用のデータ
  Image offScreenImage = null; // オフスクリーン矩形バッファ
  Graphics offG = null; // オフスクリーングラフィクス

  public void init() { // アプレット開始時に実行されるメソッド
    Dimension d = getSize(); // アプレットのサイズ取得
    w = d.width; // アプレットの横幅
    h = d.height; // アプレットの縦幅
    radius = Math.min(w, h) / 10; // ボールの半径計算
    lastx = new int[numBalls]; // ボールの最新の横軸の位置データ領域確保
    lasty = new int[numBalls]; // ボールの最新の縦軸の位置データ領域確保
    dx = new float[numBalls]; // ボールの単位時間あたりの横方向の進み量のデータ領域確保
    dy = new float[numBalls]; // ボールの単位時間あたりの縦方向の進み量のデータ領域確保
    x = new int[numBalls]; // ボールの横軸の現在位置データ領域の確保
    y = new int[numBalls]; // ボールの縦軸の現在位置データ領域の確保
    for (int i = 0; i < numBalls; i++) { // ボールの総数だけループ
      lastx[i] = (int) (Math.random() * w); // ボールの横軸方向の初期位置をランダムに設定
      lasty[i] = (int) (Math.random() * h); // ボールの縦軸方向の初期位置をランダムに設定
      // ボールの横軸方向に単位時間あたり進む量(-1.0<=dx[i]<=1.0)
      dx[i] = (float) (2 * Math.random() - 1.0);
      // ボールの縦軸方向に単位時間あたり進む量(-1.0<=dy[i]<=1.0)
      dy[i] = (float) (2 * Math.random() - 1.0);
      double t = dx[i] * dx[i] + dy[i] * dy[i];
      if (Math.abs(t) < EPSILON) {
        dx[i] = 1;
        dy[i] = 0;
      } else { // ボールの単位時間に進む大きさを１とする
        t = Math.sqrt(t);
        dx[i] /= t;
        dy[i] /= t;
      }
      // オフスクリーンバッファ用のデータ領域
      offScreenImage = createImage(w, h); // オフスクリーンバッファ領域確保
      offG = offScreenImage.getGraphics(); // オフスクリーンバッファ用のグラフィクス
    }
  }

  public void update(Graphics g) { // update()メソッドのオーバーライド
    paint(g);
  }

  public void paint(Graphics g) {
    if (offG != null) {
      // 描画はオフスクリーンで実行
      offG.setColor(Color.black);
      offG.fillRect(0, 0, w, h);
      offG.setColor(Color.yellow);
      // ボールの描画もオフスクリーンに
      for (int i = 0; i < numBalls; i++) offG.fillOval(lastx[i], lasty[i], radius, radius);
      // 次の位置を計算
      for (int i = 0; i < numBalls; i++) {
        x[i] = (int) (lastx[i] + SPEED * dx[i]);
        y[i] = (int) (lasty[i] + SPEED * dy[i]);
        if (x[i] < 0) {
          x[i] = 0;
          dx[i] = -dx[i];
        } // 左横壁に衝突
        else if (x[i] > w - radius) {
          x[i] = w - radius;
          dx[i] = -dx[i];
        } // 右横壁に衝突
        if (y[i] < 0) {
          y[i] = 0;
          dy[i] = -dy[i];
        } // 上壁に衝突
        else if (y[i] > h - radius) {
          y[i] = h - radius;
          dy[i] = -dy[i];
        } // 下壁に衝突
        lastx[i] = x[i];
        lasty[i] = y[i]; // 最新の位置を更新
      }
      // オフスクリーンへの描画が全部終わったらオフスクリーンバッファを手前に描画
      g.drawImage(offScreenImage, 0, 0, this);
      repaint(50); // 50ミリ秒待って再描画
    }
  }

  public static void main(String[] args) {
    noFlickerApplet m= new noFlickerApplet();
    m.display();
  }
}
