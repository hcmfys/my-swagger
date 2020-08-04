package org.springbus.comutergraphics.CG.common;



import java.io.*;

public class MyObject extends Object implements Serializable, Cloneable {

	// 仅具有常量定义的类
	public final static double EPSILON = 1.0E-10;//十分小的正数
	public final static double DELTA = 0.01; //1％公差
	public final static double HUGE = 1.0E31;//大正实数
	public final static double HUGEREAL = 1.0E30;//大正实数
	public final static double LARGE = 1.0E20;//相当大的正实数
	public final static int COLORLEVEL = 255;//色彩等级（8位）
	public final static double LITTLE = 1.0E-6;//数量少（用于光线跟踪）

}
