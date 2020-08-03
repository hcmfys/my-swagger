package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// DirectionalLight.java （平行光線）
// 平行光線用のクラス
//	プログラム３−２１
//		DirectionalLightコンストラクタ，setDirection(),getDirection(),
//		setIntensity(),getIntensity(),setAmbientIntensity(),
//		getAmbientIntensity(),setColor(),getColor(),
//		setSwitch(),getSwitch()メソッド

public class DirectionalLight extends Object3d {

	Vector3 direction; // 平行光線の照射方向
	double intensity = 1.0; // 平行光線の明るさ(無限大 > intensity >0) 
	Color3 color; // 平行光線の色　(RGB)
	boolean on = true; // 平行光線がついているかどうか
	double ambientIntensity = 0.0; // 環境光の輝度

	// コンストラクタ
	public DirectionalLight(){
		// デフォルトの方向はＺ軸の負方向
		direction = new Vector3(0,0,-1);
		intensity = 1.0;
		color = new Color3(1,1,1); // 白色光
		on = true;
		ambientIntensity = 0.0;
	}
	public DirectionalLight(DirectionalLight p){
		direction = new Vector3(p.direction);
		intensity = p.intensity;
		color = new Color3(p.color);
		on = p.on;
		ambientIntensity = p.ambientIntensity;
	}

	// 平行光線の照射方向ベクトル
	public void setDirection(Vector3 v){ 
		if (v.isZeroVector())
			throw new InternalError("平行光線がゼロベクトルです");
		direction = v; 
	}
	public void setDirection(double x, double y, double z){
		if (Math.abs(x)<EPSILON && 
		    Math.abs(y)<EPSILON && 
		    Math.abs(z)<EPSILON)
			throw new InternalError("平行光線がゼロベクトルです");
		direction.x = x; direction.y = y; direction.z = z;
	}
	public Vector3 getDirection(){ return direction; }

	// 平行光線の輝度
	public void setIntensity(double intensity){
		if (intensity < 0) 
			throw new InternalError("平行光線の輝度値が不適切です");
		this.intensity = intensity;
	}
	public double getIntensity(){ return intensity;	}

	// 平行光線の環境光成分
	public void setAmbientIntensity(double ambientIntensity){
		if (ambientIntensity <= 0)
			throw new InternalError("平行光線の環境光成分が不適切です");
		this.ambientIntensity = ambientIntensity;
	}
	public double getAmbientIntensity(){ return ambientIntensity; }

	// 平行光線の色
	public void setColor(Color3 color){ 
		if (color.isNegativeColor())
			throw new InternalError("平行光線の色が不適切です");
		this.color = color;
	}
	public void setColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("平行光線の色が不適切です");
		color.r = r; color.g = g; color.b = b;
	}
	public Color3 getColor(){ return color;	}

	// 平行光線のスイッチ
	public void setSwitch(boolean on){ this.on = on; }
	public boolean getSwitch(){ return on; }

	//ダミーメソッド
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}
