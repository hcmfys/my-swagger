package org.springbus.comutergraphics.CG.common;

// CameraList.java
//带有链接的摄像机数据列表

public class CameraList extends MyObject {

	static int numCameras=0;//相机数量
	Camera camera;//用于固定相机对象
	String name;//相机名称
	CameraList next = null;//指向列表中下一个元素的指针

	// 构造函数
	public CameraList(Camera camera, String name){
		numCameras++;
		this.camera = camera;
		this.name = name;
		this.next = null;
	}
}
