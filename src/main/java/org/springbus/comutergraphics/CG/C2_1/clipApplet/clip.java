package org.springbus.comutergraphics.CG.C2_1.clipApplet;

// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// clip.java
// クリッピングテスト（クリッピングする場合）

import org.springbus.comutergraphics.CG.C1_2.flickerTest.noFlickerApplet;
import org.springbus.comutergraphics.CG.common.JApplet;
import org.springbus.comutergraphics.CG.common.MyCanvas;

import java.awt.*;
import java.applet.Applet;

public class clip extends JApplet {

  MyCanvas m; // MyCanvasクラスの変数宣言

  public void init() {
    m = new MyCanvas(this); // MyCanvasクラスのオブジェクト生成
  }

  public void fillTriangle(MyCanvas m) {
    double[] x = {-1.2, 1.2, 0.0}; // 三角形のX座標
    double[] y = {-0.8, -0.8, 1.2}; // 三角形のY座標
    double[] framex = {-1, 1, 1, -1}; // 枠のX座標
    double[] framey = {-1, -1, 1, 1}; // 枠のY座標
    m.setViewport(0.1, 0.9, 0.1, 0.9); // ビューポート定義
    m.setColor(Color.yellow); // 黄色に設定
    m.fillPolygon(framex, framey, 4); // ウィンドウの塗りつぶし
    m.setColor(Color.black); // 黒色に設定
    m.drawPolygon(framex, framey, 4); // ウィンドウの枠を黒色で描画
    m.fillPolygon(x, y, 3); // 三角形の塗りつぶし
  }

  public void paint(Graphics g) {
    if (m != null) {
      m.setBackground(Color.white); // 背景は白色
      m.setWindow(-1, 1, -1, 1); // ウィンドウの設定
      fillTriangle(m); // 三角形をビューポートでクリッピングして描画
      m.resetViewport(); // ビューポートのリセット（ここではなくても可）
    }
  }

  public static void main(String[] args) {
    clip m = new clip();
    m.display();
  }
}
