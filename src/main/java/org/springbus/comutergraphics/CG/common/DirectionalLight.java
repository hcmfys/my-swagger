package org.springbus.comutergraphics.CG.common;

// DirectionalLight.java（平行光线）
//平行射线的类
//程序3-21
// DirectionalLight构造函数，setDirection（），getDirection（），
// setIntensity（），getIntensity（），setAmbientIntensity（），
// getAmbientIntensity（），setColor（），getColor（），
// setSwitch（），getSwitch（）方法

public class DirectionalLight extends Object3d {

	Vector3 direction; // 平行光線の照射方向
	double intensity = 1.0; //平行光线的亮度（无限远>强度> 0）
	Color3 color; // 平行光线的颜色（RGB）
	boolean on = true; // 是否附加平行光线
	double ambientIntensity = 0.0; // 环境光亮度

	// 构造函数
	public DirectionalLight(){
		// 默认方向为负Z轴
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

	// 平行射线的照射方向
	public void setDirection(Vector3 v){
		if (v.isZeroVector())
			throw new InternalError("平行光线为零向量");
		direction = v;
	}
	public void setDirection(double x, double y, double z){
		if (Math.abs(x)<EPSILON &&
		    Math.abs(y)<EPSILON &&
		    Math.abs(z)<EPSILON)
			throw new InternalError("平行光线为零向量");
		direction.x = x; direction.y = y; direction.z = z;
	}
	public Vector3 getDirection(){ return direction; }

	// 平行光线的亮度
	public void setIntensity(double intensity){
		if (intensity < 0)
			throw new InternalError("平行光线的亮度值不正确");
		this.intensity = intensity;
	}
	public double getIntensity(){ return intensity;	}

	// 平行光线的环境光分量
	public void setAmbientIntensity(double ambientIntensity){
		if (ambientIntensity <= 0)
			throw new InternalError("平行光线的环境光分量不合适");
		this.ambientIntensity = ambientIntensity;
	}
	public double getAmbientIntensity(){ return ambientIntensity; }

	// 平行光线的颜色
	public void setColor(Color3 color){
		if (color.isNegativeColor())
			throw new InternalError("平行光线的颜色不正确");
		this.color = color;
	}
	public void setColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("平行光线的颜色不正确");
		color.r = r; color.g = g; color.b = b;
	}
	public Color3 getColor(){ return color;	}

	//平行灯开关
	public void setSwitch(boolean on){ this.on = on; }
	public boolean getSwitch(){ return on; }

	//
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}
