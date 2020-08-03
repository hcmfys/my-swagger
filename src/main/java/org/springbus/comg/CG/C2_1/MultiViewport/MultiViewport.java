package org.springbus.comg.CG.C2_1.MultiViewport;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// MulitViewport.java
// 複数のビューポートテスト（オリジナルのキャンバス上）
// FAO林産統計年鑑のデータより
//	プログラム２−３

import org.springbus.comg.CG.common.MyCanvas;

import java.awt.*;// Java AWTパッケージを使用
import java.applet.*;//アプレット

public class MultiViewport extends Applet {

	MyCanvas m;//MyCanvasクラスのオブジェクト
	final static int idealSizeX = 600;//ウィンドウの横幅
	final static int idealSizeY = 350;//ウィンドウの縦幅

	public void init(){//初期化
		m = new MyCanvas(this);
	}

	public void paintPIGraph1(MyCanvas m){//円グラフ１の描画用のメソッド
		double r = 0.8;//大きい円の半径
		double cr = 0.3;//小さい円の半径
		// ビューポート定義　（ここに円グラフ１を書く）////////////
		m.setViewport(0.5,1.0,0.143,1.0);//ビューポート１定義
		m.setColor(new Color((float)0.95,(float)0.75,(float)0.6));
		double japan = 25.4 * 360 / 100;//日本 25.4%
		double japanx = r * Math.sin(japan*Math.PI/180);
		double japany = r * Math.cos(japan*Math.PI/180);
		m.fillArc(0,0,r,r,0,japan);//日本用の中心(0,0),半径ｒの扇形を塗りつぶす
		double sum = japan;//日本の角度を総計角度変数sumにためておく

		double usa = 19.8 * 360 / 100; // USA 19.8%
		m.fillArc(0,0,r,r,sum,usa);//USA用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += usa;//USAの角度を総計角度に加える
		double usax = r * Math.sin(sum*Math.PI/180); 
		double usay = r * Math.cos(sum*Math.PI/180);

		double italy = 4.8 * 360 / 100; // イタリア 4.8%
		m.fillArc(0,0,r,r,sum,italy);//イタリア用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += italy;//イタリアの角度を総計角度に加える
		double italyx = r * Math.sin(sum*Math.PI/180); 
		double italyy = r * Math.cos(sum*Math.PI/180);

		double korea = 4.4 * 360 / 100; // 韓国 4.4%
		m.fillArc(0,0,r,r,sum,korea);//韓国用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += korea;//韓国の角度を総計角度に加える
		double koreax = r * Math.sin(sum*Math.PI/180); 
		double koreay = r * Math.cos(sum*Math.PI/180);

		double others = 360-sum;//その他の国の角度
		m.fillArc(0,0,r,r,sum,others);//その他用中心(0,0),半径ｒの扇形を塗りつぶす

		m.setColor(Color.black);//現在色を黒色に設定
		m.drawOval(0,0,r,r);//中心(0,0),半径の円の外周を描く
		m.drawLine(0,0,0,r);//中心から時計の１２時方向の円周上の点まで描く
		m.drawLine(0,0,japanx,japany);//中心から日本の扇形の円周上の点まで描く
		m.drawLine(0,0,usax,usay);//中心からUSAの扇形の円周上の点まで描く
		m.drawLine(0,0,italyx,italyy);//中心からイタリアの扇形の円周上の点まで描く
		m.drawLine(0,0,koreax,koreay);//中心から韓国の扇形の円周上の点まで描く
		Font f0 = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 1.0);//標準サイズのフォントを生成
		m.setFont(f0);//現在フォントの設定
		m.drawString("日本",0.35,0.32);//日本という文字列を描く
		m.drawString("25.4%",0.35,0.2);//25.4%という文字列を描く
		m.drawString("アメリカ合衆国",0.15,-0.38);//アメリカ合衆国という文字列を描く
		m.drawString("19.8",0.30,-0.48);//19.8という文字列を描く
		m.drawString("その他",-0.65,0.0);//その他という文字列を描く
		m.drawString("45.6",-0.6,-0.1);//45.6という文字列を描く
		m.drawLine(0.08,-0.6,0.15,-0.85);//イタリアの扇形の内部より線を描く
		m.drawString("イタリア 4.8",0.165,-0.88);//イタリアという文字列を描く
		m.drawLine(-0.08,-0.6,-0.15,-0.89);//韓国の扇形の内部より線を描く
		m.drawString("大韓民国 4.4",-0.17,-0.98);//大韓民国という文字列を描く
		
		m.setColor(Color.white);//現在色を白色に設定
		m.fillOval(0,0,cr,cr);//中心(0,0),半径crの円を白で塗りつぶす
		m.setColor(Color.black);//現在色を黒色に戻す
		m.drawOval(0,0,cr,cr);//中心(0,0),半径crの円の外周を描く
		m.drawString("合計",-0.1,0.1);//合計という文字列を描く
		m.drawString("2億3434万m",-0.25,-0.08);//2億3434万mという文字列を描く
		Font f2 = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 0.75);//小さめのフォントを生成
		m.setFont(f2);//フォントの設定
		m.drawString("3",0.23,-0.04);//3という文字列を描く（立法メートル用）

		// ビューポート定義　///////////////////////////////////////
		m.setViewport(0.5,1.0,0.0,0.143);//ビューポート２の定義
		Font f = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 2.0);//2倍の大きさのフォント生成
		m.setFont(f);//フォントの設定
		m.drawString("1996年 木材の輸入国",-0.8,-0.3);//文字列描画
	}

	public void paintPIGraph2(MyCanvas m){//円グラフ２の描画用のメソッド
		double r = 0.8;//大きい円の半径
		double cr = 0.3;//小さい円の半径
		// ビューポート定義　（ここに円グラフ２を書く）
		m.setViewport(0.0,0.5,0.143,1.0);//ビューポート３の定義
		m.setColor(new Color((float)0.95,(float)0.75,(float)0.6));
		double canada = 22.7 * 360 / 100;//カナダ　22.7%
		double canadax = r * Math.sin(canada*Math.PI/180);
		double canaday = r * Math.cos(canada*Math.PI/180);
		m.fillArc(0,0,r,r,0,canada);//カナダ用の中心(0,0),半径ｒの扇形を塗りつぶす
		double sum = canada;//カナダの角度を総計角度変数sumにためておく

		double usa = 12.1 * 360 / 100; // USA 12.1%
		m.fillArc(0,0,r,r,sum,usa);//USA用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += usa;//USAの角度を総計角度に加える
		double usax = r * Math.sin(sum*Math.PI/180); 
		double usay = r * Math.cos(sum*Math.PI/180);

		double russia = 9.0 * 360 / 100; // Russia 9.0%
		m.fillArc(0,0,r,r,sum,russia);//ロシア用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += russia;//ロシアの角度を総計角度に加える
		double russiax = r * Math.sin(sum*Math.PI/180); 
		double russiay = r * Math.cos(sum*Math.PI/180);

		double sweden = 5.8 * 360 / 100; // Sweden 5.8%
		m.fillArc(0,0,r,r,sum,sweden);//スウェーデン用の中心(0,0),半径ｒの扇形を塗りつぶす
		sum += sweden;//スウェーデンの角度を総計角度に加える
		double swedenx = r * Math.sin(sum*Math.PI/180);
		double swedeny = r * Math.cos(sum*Math.PI/180);

		double others = 360-sum;//その他の国の角度
		m.fillArc(0,0,r,r,sum,others);//その他用の中心(0,0),半径ｒの扇形を塗りつぶす

		m.setColor(Color.black);//現在色を黒色に設定
		m.drawOval(0,0,r,r);//中心(0,0),半径rの円の外周を描く
		m.drawLine(0,0,0,r);//中心から時計の１２時方向の円周上の点まで線を描く
		m.drawLine(0,0,canadax,canaday);//中心からカナダの扇形の円周上の点まで線を描く
		m.drawLine(0,0,usax,usay);//中心からUSAの扇形の円周上の点まで線を描く
		m.drawLine(0,0,russiax,russiay);//中心からロシアの扇形の円周上の点まで線を描く
		m.drawLine(0,0,swedenx,swedeny);//中心からスウェーデンの扇形の円周上の点まで線を描く
		Font f0 = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 1.0);//標準サイズのフォント生成
		m.setFont(f0);//フォントの設定
		m.drawString("カナダ",0.3,0.35);//カナダという文字列を描く
		m.drawString("22.7%",0.35,0.2);//22.7%という文字列を描く
		m.drawString("アメリカ",0.35,-0.08);//アメリカという文字列を描く
		m.drawString("合衆国",0.38,-0.18);//合衆国という文字列を描く
		m.drawString("12.1",0.45,-0.28);//12.1という文字列を描く
		m.drawString("その他",-0.65,0.0);//その他という文字列を描く
		m.drawString("50.4",-0.6,-0.1);//50.4という文字列を描く
		m.drawString("ロシア 9.0",0.2,-0.48);//ロシアという文字列を描く
		m.drawLine(0.08,-0.6,0.15,-0.85);//スウェーデンの扇形の内部より線を描く
		m.drawString("スウェーデン 5.8",-0.17,-0.98);//スウェーデン 5.8という文字列を描く
		
		m.setColor(Color.white);//現在色を白色に設定
		m.fillOval(0,0,cr,cr);//中心(0,0),半径crの円を白色で塗りつぶす
		m.setColor(Color.black);//現在色を黒色に設定
		m.drawOval(0,0,cr,cr);//中心(0,0),半径crの円の外周を描く
		m.drawString("合計",-0.1,0.1);//合計という文字列を描く
		m.drawString("2億3401万m",-0.25,-0.08);//2億3401万mという文字列を描く
		Font f2 = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 0.75);//ちょっと小さいフォント生成
		m.setFont(f2);//フォントの設定
		m.drawString("3",0.23,-0.04);//3という文字列を描く（立法メートル用）

		// ビューポート定義
		m.setViewport(0.0,0.5,0.0,0.143);//ビューポート４の定義
		Font f = m.MyFont(m.getFont().getName(), 
			m.getFont().getStyle(), 2.0);//2倍の大きさのフォント生成
		m.setFont(f);//フォントの設定
		m.drawString("1996年 木材の輸出国",-0.8,-0.3);//文字列描画
	}

	public void paint (Graphics g){
		m.setBackground(Color.white);//背景色を白色に設定
		m.setWindow(-1,1,-1,1);//ユーザ座標系の範囲を設定
		paintPIGraph1(m);//円グラフ１の描画
		paintPIGraph2(m);//円グラフ２の描画
		m.resetViewport();//ビューポートのリセット（とても大切！）
	}
}
