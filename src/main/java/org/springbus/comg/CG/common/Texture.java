package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Texture.java
// テクスチャー用のクラス
//	プログラム３−２９
//		Textureクラスのコンストラクタ，locadImage(),setRepeat(),
//		setRepeatU(),setRepeatV(),getRepeatU(),getRepeatV(),
//		setLinearFilter(),getLinearFilter(),translate(),
//		scale(),rotate()メソッド

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.applet.Applet;

public class Texture extends Component implements Serializable, Cloneable {
	
	Matrix3 mat; // テクスチャーの変換行列
	JApplet applet;//アプレットを使用する場合のアプレットの保持用
	URL url;// テクスチャー画像の置いてあるURL
	String filename; // テクスチャーファイルの名前 （JPEGかGIF）
	protected Image texture;// テクスチャーのImageクラス
	protected int[] texel;// テクスチャー画像データを保持
	protected int width;//テクスチャー画像の横幅
	protected int height;// テクスチャー画像の縦幅
	boolean repeatU = true; // U方向の反復
	boolean repeatV = true; // V方向の反復
	boolean linearFilter = true;//線形フィルターを使うかどうかのフラグ

	// コンストラクタ
	public Texture(JApplet applet, URL url, String filename){
		this.mat = new Matrix3();
		this.applet = applet;
		try {
			this.url = new URL(url,filename);
		}
		catch (MalformedURLException e){}
		this.filename = new String(filename);
		this.texture = null;
		this.texel = null;
		this.width = this.height = 0;
		this.repeatU = true;
		this.repeatV = true;
		this.linearFilter = true;
	}
	public Texture(String filename){
		this.mat = new Matrix3();
		this.applet = null;
		this.url = null;
		this.filename = new String(filename);
		this.texture = null;
		this.texel = null;
		this.width = this.height = 0;
		this.repeatU = true;
		this.repeatV = true;
		this.linearFilter = true;
	}
	public Texture(){ this(""); }
	public Texture(Texture p){
		mat = new Matrix3(p.mat);
		applet = p.applet;
		try {
			url = new URL(p.url,p.filename);
		}
		catch (MalformedURLException e){}
		filename = new String(p.filename);
		texture = p.texture; // 実体は複製しない
		texel = p.texel; // 実体は複製しない
		width = p.width;
		height = p.height;
		repeatU = p.repeatU;
		repeatV = p.repeatV;
		linearFilter = p.linearFilter;
	}

	// テクスチャーファイルのロード
	public void loadImage(){
		MediaTracker mt = new MediaTracker(this);
		if (filename == null) throw 
			new NullPointerException("テクスチャーファイル名がありません");
		if (applet == null && url == null)
			texture = this.getToolkit().getImage(filename);
		else 
			texture = this.applet.getImage(url,filename);
		if (texture == null) throw 
			new InternalError("テクスチャーファイル:"+filename+
			"が見つかりません");
		mt.addImage(texture,1);
		try { mt.waitForAll(); }
		catch (InterruptedException e){}
		width = texture.getWidth(this);
		height = texture.getHeight(this);
		texel = new int[width*height];
		PixelGrabber pg = new PixelGrabber(texture,0,0,width,height,
			texel,0,width);
		try { pg.grabPixels(); }
		catch (InterruptedException e){}
	}

	// テクスチャーの反復設定
	public void setRepeat(boolean repeatU, boolean repeatV){
		this.repeatU = repeatU;
		this.repeatV = repeatV;
	}
	public void setRepeatU(boolean repeatU){
		this.repeatU = repeatU;
	}
	public void setRepeatV(boolean repeatV){
		this.repeatU = repeatV;
	}
	public boolean getRepeatU(){
		return repeatU;
	}
	public boolean getRepeatV(){
		return repeatV;
	}

	// テクスチャーマッピングでのフィルターの設定
	public void setLinearFilter(boolean linearFilter){
		this.linearFilter = linearFilter;
	}
	public boolean getLinearFilter(){
		return linearFilter;
	}

	// テクスチャーの移動
	public void translate(double x, double y){
		mat.a[2] += x;
		mat.a[5] += y;
	}

	// テクスチャーのスケーリング
	public void scale(double x, double y){
		mat.a[0] *= x;
		mat.a[4] *= y;
	}

	// テクスチャーの回転 
	public void rotate(double theta){
		Matrix3 m = new Matrix3();
		double s = Math.sin(theta);
		double c = Math.cos(theta);
		m.a[0] = c;
		m.a[1] = s;
		m.a[3] = -s;
		m.a[4] = c;
		mat.multiply(m);
	}

}
