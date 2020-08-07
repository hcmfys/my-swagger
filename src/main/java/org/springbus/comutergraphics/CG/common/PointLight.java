package org.springbus.comutergraphics.CG.common;

// PointLight.java（点光源）
//点源类
//程序3-20

public class PointLight extends Object3d {

	Vertex3 location; // 点光源的位置
	double intensity = 1.0; // 点光源亮度（无穷>强度> 0）
	Color3 color; // 点光源颜色（RGB）
	//点源衰减模型：
	// 1 /（衰减.x + r *衰减.y + r * r *衰减.z）衰减
	//其中r是物体与点光源之间的距离
	Vertex3 attenuation;
	boolean on = true; // 是否有点光源
	double radius = LARGE; // 点光源覆盖的范围（不超过此范围）
	double ambientIntensity = 0.0; // 光源的环境光强度

	// 构造函数
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

	//点光源的位置
	public void setLocation(Vertex3 v){
		location.x = v.x; location.y = v.y; location.z = v.z;
	}
	public void setLocation(double x, double y, double z){
		location.x = x; location.y = y; location.z = z;
	}
	public Vertex3 getLocation(){ return location; }

	// 点光源的亮度
	public void setIntensity(double intensity){
		if (intensity <= 0)
			throw new InternalError("点光源的亮度值不合适");
		this.intensity = intensity;
	}
	public double getIntensity(){ return intensity;	}

	// 点光源的环境光分量
	public void setAmbientIntensity(double ambientIntensity){
		if (ambientIntensity <= 0)
			throw new InternalError("点光源的环境光成分不合适");
		this.ambientIntensity = ambientIntensity;
	}
	public double getAmbientIntensity(){ return ambientIntensity; }

	// 点光源的颜色
	public void setColor(Color3 color){
		if (color.isNegativeColor())
			throw new InternalError("点光源的颜色不正确");
		this.color = color;
	}
	public void setColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("点光源的颜色不正确");
		color.r = r; color.g = g; color.b = b;
	}
	public Color3 getColor(){ return color;	}

	// 点源衰减
	public void setAttenuation(Vertex3 attenuation){
		if (Math.abs(attenuation.x) < EPSILON &&
		    Math.abs(attenuation.y) < EPSILON &&
		    Math.abs(attenuation.z) < EPSILON)
			throw new InternalError("点光源衰减不正确");
		this.attenuation = attenuation;
	}
	public void setAttenuation(double x, double y, double z){
		if (Math.abs(x) < EPSILON &&
		    Math.abs(y) < EPSILON &&
		    Math.abs(z) < EPSILON)
			throw new InternalError("点光源衰减不正确");
		attenuation.x = x; attenuation.y = y; attenuation.z = z;
	}
	public Vertex3 getAttenuation(){ return attenuation; }

	// 点光源范围
	public void setRadius(double radius){
		if (radius <= 0)
			throw new InternalError("点光源的影响范围不合适");
		this.radius = radius;
	}
	public double gteRadius(){ return radius; }

	// 点灯开关
	public void setSwitch(boolean on){ this.on = on; }
	public boolean getSwitch(){ return on; }

	//空方法
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}
