package org.springbus.comutergraphics.CG.common;

// Texture.java
//纹理类
//程序3-29

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;

public class Texture extends Component implements Serializable, Cloneable {

	Matrix3 mat; // 纹理转换矩阵
	JApplet applet;//使用小程序时用于固定小程序
	URL url;// 纹理图像所在的URL
	String filename; // 纹理文件名（JPEG或GIF）
	protected Image texture;// 图像的图像类
	protected int[] texel;// 保存纹理图像数据
	protected int width;//纹理图像的宽度
	protected int height;// 纹理图像的垂直宽度
	boolean repeatU = true; // U方向迭代
	boolean repeatV = true; // 在V方向重复
	boolean linearFilter = true;//标记为使用线性滤波器

	// 构造函数
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
		texture = p.texture; // 实体不重复
		texel = p.texel; // 实体不重复
		width = p.width;
		height = p.height;
		repeatU = p.repeatU;
		repeatV = p.repeatV;
		linearFilter = p.linearFilter;
	}

	// 加载纹理文件
	public void loadImage(){
		MediaTracker mt = new MediaTracker(this);
		if (filename == null) throw
			new NullPointerException("没有纹理文件名");
		if (applet == null && url == null)
			texture = this.getToolkit().getImage(filename);
		else
			texture = this.applet.getImage(url,filename);

		if (texture == null){
			texture = this.applet.getImage(url);
		}
		if (texture == null) throw
			new InternalError("纹理文件:"+filename+ "找不到");
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

	// 纹理重复设置
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

	// 纹理映射中的过滤器设置
	public void setLinearFilter(boolean linearFilter){
		this.linearFilter = linearFilter;
	}
	public boolean getLinearFilter(){
		return linearFilter;
	}

	// 纹理运动
	public void translate(double x, double y){
		mat.a[2] += x;
		mat.a[5] += y;
	}

	// 纹理缩放
	public void scale(double x, double y){
		mat.a[0] *= x;
		mat.a[4] *= y;
	}

	//纹理旋转
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
