package org.springbus.comutergraphics.CG.common;

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

	Color3 ambientColor; // 物体的环境光分量
	Color3 diffuseColor; // 漫反射组件颜色
		//（通常是人眼在白色光源（例如太阳）下观察到的颜色）
	Color3 specularColor; // 明亮发光的组件的颜色（镜面反射）
	Color3 emissiveColor; // 物体作为光源自然发出的颜色
	double shininess; //发光部分的强调程度
		// （越小越模糊，发出的光线越锐利）
	double transparency; //透光率（1.0完全透明，0.0不透明）
	double reflection; // 反射率（1.0完美反射，0,0完美漫反射对象）
	double refraction; // 折光率
	Texture texture = null;//质地

	// 构造函数
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

	// 环境光颜色
	public void setAmbientColor(Color3 c){
		if (c.isNegativeColor()) throw new
			InternalError("错误的环境光设置");
		ambientColor = c;
	}
	public void setAmbientColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("错误的环境光设置");
		ambientColor.r = r;
		ambientColor.g = g;
		ambientColor.b = b;
	}
	public Color3 getAmbientColor(){
		return ambientColor;
	}

	// 漫反射色
	public void setDiffuseColor(Color3 c){
		if (c.isNegativeColor()) throw new
			InternalError("不正确的漫反射设置");
		diffuseColor = c;
	}
	public void setDiffuseColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("不正确的漫反射设置");
		diffuseColor.r = r;
		diffuseColor.g = g;
		diffuseColor.b = b;
	}
	public Color3 getDiffuseColor(){
		return diffuseColor;
	}

	//镜面反射色
	public void setSpecularColor(Color3 c){
		if (c.isNegativeColor()) throw new
			InternalError("镜面光设置不正确");
		specularColor = c;
	}
	public void setSpecularColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("镜面光设置不正确");
		specularColor.r = r;
		specularColor.g = g;
		specularColor.b = b;
	}
	public Color3 getSpecularColor(){
		return specularColor;
	}

	// 自发光色
	public void setEmissiveColor(Color3 c){
		if (c.isNegativeColor()) throw new
			InternalError("自发光颜色设置不正确");
		emissiveColor = c;
	}
	public void setEmissiveColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0) throw new
			InternalError("自发光颜色设置不正确");
		emissiveColor.r = r;
		emissiveColor.g = g;
		emissiveColor.b = b;
	}
	public Color3 getEmissiveColor(){
		return emissiveColor;
	}

	// 光泽度
	public void setShininess(double s){
		if (s < 0) throw new
			InternalError("光泽度值不正确（镜面反射指数系数）");
		shininess = s;
	}
	public double getShininess(){
		return shininess;
	}

	// 透过率
	public void setTransparency(double t){
		if (t < 0) throw new
			InternalError("透射率值不合适");
		transparency = t;
	}
	public double getTransparency(){
		return transparency;
	}

	// 反射率
	public void setReflection(double r){
		if (r < 0) throw new
			InternalError("反射率值不正确");
		reflection = r;
	}
	public double getReflection(){
		return reflection;
	}

	// 屈折率
	public void setRefraction(double r){
		if (r < 0) throw new
			InternalError("折射率值不正确");
		refraction = r;
	}
	public double getRefraction(){
		return refraction;
	}

	// 质地
	public void setTexture(Texture p){
		if (p == null) throw new
			NullPointerException("纹理值不正确");
		texture = p;
	}
	public Texture getTexture(){
		return texture;
	}

	//　打印材料数据
	public void print(){
		ambientColor.print("环境光");
		diffuseColor.print("漫反射组件颜色");
		specularColor.print("镜面反射光成分的颜色");
		emissiveColor.print("自发散光组件的颜色");
		System.out.println("光泽度 = "+shininess);
		System.out.println("透过率 = "+transparency);
		System.out.println("反射率 = "+reflection);
		System.out.println("折光率 = "+refraction);
		System.out.println("纹理="+texture);
	}
}
