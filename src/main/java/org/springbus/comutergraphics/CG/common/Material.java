package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Material.java
// 材質データ用のクラス
//	プログラム３−２３
//		Materialクラスのコンストラクタ，setAmbientrColor(),
//		getAmbientColor(),setDiffuseColor(),getDiffuseColor(),
//		setSpecularColor(),getSpecularColor(),setEmissiveColor(),
//		getEmissiveColor(),setShininess(),getShininess(),
//		setTransparency(),getTransparency(),setReflection(),
//		getReflection(),setRefraction(),getRefraction(),
//		setTexture(),getTexture()メソッド

public class Material extends MyObject {

	Color3 ambientColor; // 物体のもつ環境光成分
	Color3 diffuseColor; // 拡散反射光成分の色　
		//（通常、太陽のような白色光源のもとで人間の目で観察される色）
	Color3 specularColor; // ピカッと光る（鏡面反射）成分の色
	Color3 emissiveColor; // 物体が光源として自然に放つ色
	double shininess; // ピカッと光る部分の強調度　
		// (小さいほどぼんやり、大きいとシャープに光る）
	double transparency; // 透過率　(1.0 完全透明、0.0 不透明）
	double reflection; // 反射率　（1.0 完全反射、0,0 完全拡散反射物体）
	double refraction; // 屈折率　
	Texture texture = null;//テクスチャー

	// コンストラクタ
	public Material(){
		ambientColor = new Color3(0,0,0);
		diffuseColor = new Color3(1,1,1);
		specularColor = new Color3(0,0,0);
		emissiveColor = new Color3(0,0,0);
		shininess = 20.0;
		transparency = 0.0;
		reflection = 0.0;
		refraction = 1.0;
		texture = null;
	}
	public Material(Material p){
		ambientColor = new Color3(p.ambientColor);
		diffuseColor = new Color3(p.diffuseColor);
		specularColor = new Color3(p.specularColor);
		emissiveColor = new Color3(p.emissiveColor);
		shininess = p.shininess;
		transparency = p.transparency;
		reflection = p.reflection;
		refraction = p.refraction;
		texture = p.texture;
	}

	// 環境光の色
	public void setAmbientColor(Color3 c){
		if (c.isNegativeColor()) throw new 
			InternalError("環境光の設定が不適切です");
		ambientColor = c;
	}
	public void setAmbientColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("環境光の設定が不適切です");
		ambientColor.r = r;
		ambientColor.g = g;
		ambientColor.b = b;
	}
	public Color3 getAmbientColor(){
		return ambientColor;
	}

	// 拡散反射光色
	public void setDiffuseColor(Color3 c){
		if (c.isNegativeColor()) throw new 
			InternalError("拡散反射光の設定が不適切です");
		diffuseColor = c;
	}
	public void setDiffuseColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("拡散反射光の設定が不適切です");
		diffuseColor.r = r;
		diffuseColor.g = g;
		diffuseColor.b = b;
	}
	public Color3 getDiffuseColor(){
		return diffuseColor;
	}

	// 鏡面反射光色
	public void setSpecularColor(Color3 c){
		if (c.isNegativeColor()) throw new 
			InternalError("鏡面反射光の設定が不適切です");
		specularColor = c;
	}
	public void setSpecularColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("鏡面反射光の設定が不適切です");
		specularColor.r = r;
		specularColor.g = g;
		specularColor.b = b;
	}
	public Color3 getSpecularColor(){
		return specularColor;
	}

	// 自己発光色
	public void setEmissiveColor(Color3 c){
		if (c.isNegativeColor()) throw new 
			InternalError("自己発光色の設定が不適切です");
		emissiveColor = c;
	}
	public void setEmissiveColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("自己発光色の設定が不適切です");
		emissiveColor.r = r;
		emissiveColor.g = g;
		emissiveColor.b = b;
	}
	public Color3 getEmissiveColor(){
		return emissiveColor;
	}

	// 光沢度
	public void setShininess(double s){
		if (s < 0) throw new
			InternalError("光沢度（鏡面反射べき乗係数）の値が不適切です");
		shininess = s;
	}
	public double getShininess(){
		return shininess;
	}

	// 透過率
	public void setTransparency(double t){
		if (t < 0) throw new
			InternalError("透過率の値が不適切です");
		transparency = t;
	}
	public double getTransparency(){
		return transparency;
	}

	// 反射率
	public void setReflection(double r){
		if (r < 0) throw new
			InternalError("反射率の値が不適切です");
		reflection = r;
	}
	public double getReflection(){
		return reflection;
	}

	// 屈折率
	public void setRefraction(double r){
		if (r < 0) throw new
			InternalError("屈折率の値が不適切です");
		refraction = r;
	}
	public double getRefraction(){
		return refraction;
	}

	// テクスチャー
	public void setTexture(Texture p){
		if (p == null) throw new
			NullPointerException("テクスチャーの値が不適切です");
		texture = p;
	}
	public Texture getTexture(){
		return texture;
	}

	//　材質データの印刷
	public void print(){
		ambientColor.print("環境光");
		diffuseColor.print("拡散反射光成分の色");
		specularColor.print("鏡面反射光成分の色");
		emissiveColor.print("自己発散光成分の色");
		System.out.println("光沢度 = "+shininess);
		System.out.println("透過率　= "+transparency);
		System.out.println("反射率 = "+reflection);
		System.out.println("屈折率 = "+refraction);
		System.out.println("テクスチャー　= "+texture);
	}
}
