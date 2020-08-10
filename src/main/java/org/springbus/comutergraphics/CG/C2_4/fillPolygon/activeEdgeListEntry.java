package org.springbus.comutergraphics.CG.C2_4.fillPolygon;


// activeEdgeListEntry.java
//在此处放置活动边缘列表元素
//程序2-10

public class activeEdgeListEntry {

	int name;//指数
	int topx;//具有较大Y坐标的顶点的X坐标
	int topy;//具有较大Y坐标的顶点的Y坐标
	int boty;//较小顶点的Y坐标
	double delta; // = -(botx-topx)/(boty-topy)
	double x; //特定扫描线上Y的X坐标值
	boolean isHorizontal;//水平边缘的标志
	activeEdgeListEntry next;//指向下一个元素的指针

}
