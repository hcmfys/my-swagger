package org.springbus.comutergraphics.CG.C3_2.sphdraw;


// sphdraw.java（球面线显示程序：applet）
//程序3-19

import org.springbus.comutergraphics.CG.common.*;

import java.awt.*;

public class sphdraw extends JApplet{

	ObjectWorld ow; //ObjectWorld类的变量
	MyCanvas m; // MyCanvas类的变量
	double screenx, screeny;// 屏幕尺寸

	public   void init(){

		// ObjectWorld类对象生成
		ow = new ObjectWorld();
		// 世界的一代（宇宙）
		ObjectNode universe = ow.createUniverse();
		// MyCanvas类的对象创建
		m = new MyCanvas(this);
		// 相机类对象生成
		Camera cam = new Camera(m);
		//屏幕尺寸设定
		screenx = cam.getScreenX(); screeny = cam.getScreenY();
		// 视窗设定
		m.setWindow(-screenx/2,screenx/2,-screeny/2,screeny/2);
		// 屏幕外缓冲区设置
		m.setOffScreenBuffer();
		// 设定相机位置
		cam.setEyePosition(0,0,2);
		// 相机旋转角度设置（绕X轴旋转0.3弧度）
		cam.rotate(1,0,0,0.3);
		// 向世界添加相机
		ow.addCamera(cam,"CAMERA1");
		// 球体的定义
		Sphere sph = new Sphere(1);
		// 将球体添加到场景图
		ObjectNode sphNode = universe.addChild(sph,"Sphere");

	}

  public void paint(Graphics g) {
	  if (m != null) {
		  // 设置萤幕
		  m.setGraphics(MyCanvas.OFFSCREEN_GRAPHICS);
		  //在屏幕外绘制球体
		  ow.drawWorld();
		  // 切换到前屏
		  m.setGraphics(MyCanvas.DEFAULT_GRAPHICS);
		  //在前屏幕上绘制屏幕外图像
		  m.drawImage(m.getOffScreenImage(), -screenx / 2, screeny / 2, this);
	  }
  }

	public  static void  main(String[] args) {
		sphdraw  mm=new sphdraw();
		mm.display();
	}
}
