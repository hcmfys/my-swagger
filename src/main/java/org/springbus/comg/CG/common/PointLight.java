package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// PointLight.java （点光源）
// 点光源のクラス
//	プログラム３−２０
//		PointLightコンストラクタ，setLocation(),getLocation(),
//		setIntensity(),getIntensity(),setAmbientIntensity(),
//		getAmbientIntensity(),setColor(),getColor(),
//		setAttenuation(),getAttenuation(),setRadius(),getRadius(),
//		setSwitch(),getSwitch()メソッド

public class PointLight extends Object3d {

	Vertex3 location; // 点光源の位置
	double intensity = 1.0; // 点光源の明るさ(無限大 > intensity >0) 
	Color3 color; // 点光源の色　(RGB)
	// 点光源の減衰モデル： 
	// 1/(attenuation.x+r*attenuation.y+r*r*attenuation.z)で減衰
	// ただしrは物体と点光源との距離
	Vertex3 attenuation; 
	boolean on = true; // 点光源がついているかどうか
	double radius = LARGE; // 点光源の及ぶ範囲（これより外には届かない）
	double ambientIntensity = 0.0; // 光源がもつ環境光の強度

	// コンストラクタ
	public PointLight(){
		location = new Vertex3(0,0,0);
		intensity = 1.0;
		color = new Color3(1,1,1);//白色光
		attenuation = new Vertex3(1,0,0);
		on = true;
		radius = LARGE;
		ambientIntensity = 0.0;
	}
	public PointLight(PointLight p){
		location = new Vertex3(p.location);
		intensity = p.intensity;
		color = new Color3(p.color);
		attenuation = new Vertex3(p.attenuation);
		on = p.on;
		radius = p.radius;
		ambientIntensity = p.ambientIntensity;
	}
	public PointLight(Vertex3 v){
		this();
		this.location = v;
	}
	public PointLight(double x, double y, double z){
		this(new Vertex3(x,y,z));
	}

	// 点光源の位置
	public void setLocation(Vertex3 v){
		location.x = v.x; location.y = v.y; location.z = v.z;
	}
	public void setLocation(double x, double y, double z){
		location.x = x; location.y = y; location.z = z;
	}
	public Vertex3 getLocation(){ return location; }

	// 点光源の輝度（インテンシティ）
	public void setIntensity(double intensity){
		if (intensity <= 0)
			throw new InternalError("点光源の輝度値が不適切です");
		this.intensity = intensity;
	}
	public double getIntensity(){ return intensity;	}

	// 点光源の環境光成分
	public void setAmbientIntensity(double ambientIntensity){
		if (ambientIntensity <= 0)
			throw new InternalError("点光源の環境光成分が不適切です");
		this.ambientIntensity = ambientIntensity;
	}
	public double getAmbientIntensity(){ return ambientIntensity; }

	// 点光源の色
	public void setColor(Color3 color){
		if (color.isNegativeColor())
			throw new InternalError("点光源の色が不適切です");
		this.color = color;
	}
	public void setColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("点光源の色が不適切です");
		color.r = r; color.g = g; color.b = b;
	}
	public Color3 getColor(){ return color;	}

	// 点光源の減衰率
	public void setAttenuation(Vertex3 attenuation){
		if (Math.abs(attenuation.x) < EPSILON &&
		    Math.abs(attenuation.y) < EPSILON &&
		    Math.abs(attenuation.z) < EPSILON)
			throw new InternalError("点光源の減衰率が不適切です");
		this.attenuation = attenuation;
	}
	public void setAttenuation(double x, double y, double z){
		if (Math.abs(x) < EPSILON &&
		    Math.abs(y) < EPSILON &&
		    Math.abs(z) < EPSILON)
			throw new InternalError("点光源の減衰率が不適切です");
		attenuation.x = x; attenuation.y = y; attenuation.z = z;
	}
	public Vertex3 getAttenuation(){ return attenuation; }

	// 点光源の及ぼす範囲
	public void setRadius(double radius){ 
		if (radius <= 0)
			throw new InternalError("点光源の影響範囲が不適切です");
		this.radius = radius; 
	}
	public double gteRadius(){ return radius; }

	// 点光源のスイッチ
	public void setSwitch(boolean on){ this.on = on; }
	public boolean getSwitch(){ return on; }

	// ダミーメソッド
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}
