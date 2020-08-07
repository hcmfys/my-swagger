package org.springbus.comutergraphics.CG.common;

// BoundingBox.java（边界框的定义）

public class BoundingBox extends MyObject {

	Vertex3 center;//边界框的中心位置
	double xsize, ysize, zsize;//边界框大小

	// 构造函数
	public BoundingBox(Vertex3 center,
		double xsize, double ysize, double zsize){
		this.center = center;
		this.xsize = xsize;
		this.ysize = ysize;
		this.zsize = zsize;
	}
	public boolean isHit(Vertex3 o, Vector3 d){
		// 射线与边界框的交点的计算
		double t, t1, t2, x, y, z;
		double xt, yt, zt;
		t = HUGE;
		// Z = center.z+zsize/2 || Z = center.x-zsize/2 与平面相交的计算
		if (Math.abs(d.z)>EPSILON){
			t1 = (center.z+zsize/2-o.z)/d.z;
			x = o.x + t1 * d.x;
			y = o.y + t1 * d.y;
			xt = (2*(x-center.x)+xsize)/(2*xsize);
			yt = (2*(y-center.y)+ysize)/(2*ysize);
			if (t1 > 0 && t1 < t &&
				xt*(1-xt)>=0 &&
				yt*(1-yt)>=0)
				t = t1;
			t2 = (center.z-zsize/2-o.z)/d.z;
			x = o.x + t2 * d.x;
			y = o.y + t2 * d.y;
			xt = (2*(x-center.x)+xsize)/(2*xsize);
			yt = (2*(y-center.y)+ysize)/(2*ysize);
			if (t2 > 0 && t2 < t &&
				xt*(1-xt)>=0 &&
				yt*(1-yt)>=0)
				t = t2;
		}
		// X = center.x+xsize/2 || X = center.x-xsize/2 与平面相交的计算
		// o.x+t*d.x = center.x+xsize/2 (or center.x-xsize/2)
		if (Math.abs(d.x)>EPSILON){
			t1 = (center.x+xsize/2-o.x)/d.x;
			z = o.z + t1 * d.z;
			y = o.y + t1 * d.y;
			zt = (2*(z-center.z)+zsize)/(2*zsize);
			yt = (2*(y-center.y)+ysize)/(2*ysize);
			if (t1 > 0 && t1 < t &&
				zt*(1-zt)>=0 &&
				yt*(1-yt)>=0)
				t = t1;
			t2 = (-xsize/2-o.x)/d.x;
			z = o.z + t2 * d.z;
			y = o.y + t2 * d.y;
			zt = (2*(z-center.z)+zsize)/(2*zsize);
			yt = (2*(y-center.y)+ysize)/(2*ysize);
			if (t2 > 0 && t2 < t &&
				zt*(1-zt)>=0 &&
				yt*(1-yt)>=0)
				t = t2;
		}
		// Y = center.y+ysize/2 || Y = center.y-ysize/2 与平面相交的计算
		// o.y+t*d.y = center.y+ysize/2 (or center.y-ysize/2)
		if (Math.abs(d.y)>EPSILON){
			t1 = (center.y+ysize/2-o.y)/d.y;
			z = o.z + t1 * d.z;
			x = o.x + t1 * d.x;
			zt = (2*(z-center.z)+zsize)/(2*zsize);
			xt = (2*(x-center.x)+xsize)/(2*xsize);
			if (t1 > 0 && t1 < t &&
				zt*(1-zt)>=0 &&
				xt*(1-xt)>=0)
				t = t1;
			t2 = (center.y-ysize/2-o.y)/d.y;
			z = o.z + t2 * d.z;
			x = o.x + t2 * d.x;
			zt = (2*(z-center.z)+zsize)/(2*zsize);
			xt = (2*(x-center.x)+xsize)/(2*xsize);
			if (t2 > 0 && t2 < t &&
				zt*(1-zt)>=0 &&
				xt*(1-xt)>=0)
				t = t2;
		}
		if (t > LARGE) return false;
		else return true;
	}
}
