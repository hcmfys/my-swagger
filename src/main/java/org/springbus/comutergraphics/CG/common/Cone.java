package org.springbus.comutergraphics.CG.common;

// Cone.java（锥体定义）
//圆锥形
//程序3-8
//锥体类构造函数

public class Cone extends Object3d {

	// F = x*x+z*z-r*r*(1/2-y/h)*(1/2-y/h) = 0 */
	double r; // 圆锥底部的半径（r> 0）
	double h; // 圆锥高度（h> 0）
	BoundingBox bbox;//边界框
	int numUTessellate;//U方向上的镶嵌）
	int numVTessellate;//V方向细分

	// 构造函数
	public Cone(double r, double h){
		if (r <= 0 || h <= 0)
			throw new InternalError("锥形参数错误");
		this.r = r;
		this.h = h;
		this.numUTessellate = 30;
		this.numVTessellate = 4;
		Vertex3 center;//边界框的中心位置
		double xsize, ysize, zsize;//边框尺寸
		//边框套
		center = new Vertex3();
		center.x = center.y = center.z = 0;
		xsize = zsize = 2*r*(1+DELTA);
		ysize = h*(1+DELTA);
		this.bbox = new BoundingBox(center,xsize,ysize,zsize);
	}
	public Cone(){
		//默认圆锥体底部的半径为1.0
		//高度为2.0
		this(1.0,2.0);
	}

	//円すいの印刷
	public void print(){
		System.out.println("Cone: radius = "+r+" height = "+h);
	}

	//镶嵌设置
	public void setTessellate(int unum, int vnum){
		if (unum <= 0 || vnum <= 0)
			throw new InternalError("镶嵌数必须为正");
		numUTessellate = unum;
		numVTessellate = vnum;
	}

	// 沿锥体高度方向进行范围检查（用于射线追踪）
	public boolean isWithinConeHeight(double oy, double dy,
		double t){
		double y = oy + t * dy;
		if (-h/2 <= y && y < h/2) return true;
		return false;
	}

	// 射线锥的交点的计算
	public void  getNearerIntersection(Ray ray, ObjectNode p){
		if (ray == null || p == null)
			throw new NullPointerException();
		if (!(p.element instanceof Cone)) return;

		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);

		//射线与边界框之间的相交计算
		if (!bbox.isHit(o,d)) return;

		//计算射线与圆锥的交点
		//首先，计算与底部的交点
		// o.y+t*d.y = -h/2
		double t=HUGE, t1, x, z;
		if (Math.abs(d.y)>EPSILON){
			t1 = -(h/2+o.y)/d.y;
			x = o.x + t1 * d.x;
			z = o.z + t1 * d.z;
			if (x*x+z*z < r*r && t1 > EPSILON)
				t = t1;
		}

		// (o.x+t*d.x)^2 + (o.z+t*d.z)^2 = (r*(1/2-(o.y+t*d.y)/h))^2
		// 我们称其为a * t ^ 2 + b * t + c = 0。
		//使用二次解公式。
		double a,b,c,det,t2,q;
		q = (r/h)*(r/h);
		a = d.x * d.x + d.z * d.z - q * (d.y * d.y);
		b = 2 * (o.x * d.x + o.z * d.z)
		  - q * d.y * (2 * o.y - h);
		c = o.x * o.x + o.z * o.z
		  - q * (0.5 * h - o.y)*(0.5 * h - o.y);
		det = b * b - 4.0 * a * c;
		if (det < 0.0 || Math.abs(a)<EPSILON) return;
		det = Math.sqrt(det);
		if (Math.abs(det)<EPSILON){ // 一种解决方案
			t1 = (-b) / (2.0 * a);
			if (t1 < t &&
				isWithinConeHeight(o.y,d.y,t1))
				t = t1;
		}
		else { // 小さい解
			t1 = (-b - det) / (2.0 * a);
			if (Math.abs(t1)>EPSILON && t1 > 0.0) {
				if (t1 < t &&
					isWithinConeHeight(o.y,d.y,t1))
					t = t1;
			}
			else {
				t2 = (-b + det) / (2.0 * a);
				if (t2 < 0.0 || Math.abs(t2)<EPSILON){
					if (t > LARGE)
					return;
				}
				if (t2 < t &&
					isWithinConeHeight(o.y,d.y,t2))
					 t = t2;
			}
		}
		Vertex3 localSolution = new Vertex3(
			o.x + t * d.x,
			o.y + t * d.y,
			o.z + t * d.z
		);
		Vertex3 worldSolution = p.getWorldPosition(localSolution);
		double distance = Vertex3.distance(ray.origin,worldSolution);
		if (distance < ray.t) {
			ray.t = distance;
			ray.hitNode = p;
			ray.intersection = worldSolution;
			ray.intersectionLocal = localSolution;
			return;
		}
		return;
	}

	// 锥与射线相交处的法向矢量的计算
	public void setNearestIntersection(Ray ray){
		boolean isBottomCap = false;
		double x = ray.intersectionLocal.x;
		double y = ray.intersectionLocal.y;
		double z = ray.intersectionLocal.z;
		Vector3 localNormal;
		if (Math.abs(y) < EPSILON){ // 越过锥底
			localNormal = new Vector3(0,-1,0);
			isBottomCap = true;
		}
		else {// 穿过圆锥的侧面
			localNormal = new Vector3(x,(r/h)*(r/h)*(h/2-y),z);
		}
		localNormal.normalize();
		ray.intersectionNormal = ray.hitNode.getWorldNormal(localNormal);
		ray.intersectionNormal.normalize();
		ray.isNormal = true;
		ray.isColor = false;

		Material m = ray.hitNode.dummyMaterial;
		if (m == null) throw new NullPointerException("Material Null");
		if (m.texture == null) {
			ray.isTexture = false;
			return;
		}

		// 锥体纹理计算
		double u,v;
		if (isBottomCap){
			u = (x+r)/(2*r);
			v = (z+r)/(2*r);
		}
		else {
			// 局部圆锥体的参数表示
			// x = -r * (1-2y/h) * sin(theta)
			// y = t * h
			// z = -r * (1-2y/h) * cos(theta)
			// 0 <= t <= 1
			double t = (y + h/2) / h;
			// 0 <= theta <= 2*PI  注: atan2=>[-PI,PI]
			double theta = Math.PI + Math.atan2(x,z);
			u = theta / (2 * Math.PI);
			v = t;
		}
		Vector3 w = new Vector3(u,v,1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	//圆锥线图
	public void draw(Camera c, ObjectNode node){
		if (!(node.element instanceof Cone)) return;
		// 细化圆锥体时对线段进行精细修剪
		drawCone(c,node,numUTessellate,numVTessellate);
	}
	// 获取圆锥上的坐标值
	public Vertex3 getConeCoord(double r, double h, double u0, double v0){
		double u = 2.0 * Math.PI * u0;
		double v = h * (v0 - 0.5);
		double y = v;
		double x = -r * (0.5 - y/h) * Math.sin(u);
		double z = -r * (0.5 - y/h) * Math.cos(u);
		if (Math.abs(x)<EPSILON) x = 0;
		if (Math.abs(y)<EPSILON) y = 0;
		if (Math.abs(z)<EPSILON) z = 0;
		Vertex3 w = new Vertex3(x,y,z);
		return w;
	}
	public void drawCone(Camera c, ObjectNode node, int lx, int ly ){
		double u, v;
		int i,j,k;
		if (lx <= 0 || ly <= 0) return;
		Cone p = (Cone)(node.element);
		double du = 1.0;
		double dv = 1.0;
		double ustep = du / lx;
		double vstep = dv / ly;
		//输出顶点
		int numVertex = lx*(ly+1)+2;
		Vertex3[] vtx = new Vertex3[numVertex];//頂点(apex)＋側面＋底面
		int cnt=0;
		vtx[cnt++] = new Vertex3(0,h/2,0);//頂点
		// 側面
		for ( j = 0, v = 0.0 ; j < ly ; j++, v += vstep ){
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep )
				vtx[cnt++] = getConeCoord(p.r,p.h,u,v);
		}
		vtx[cnt++] = new Vertex3(0,-h/2,0);//底面中央
		for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep )
			vtx[cnt++] = getConeCoord(p.r,p.h,u,0.0);

		//输出法线向量
		Vector3[] nml = new Vector3[numVertex];
		cnt=0;
		nml[cnt++] = new Vector3(0,1,0);//上面
		// 側面
		Vector3 vv;
		for ( j = 0, v = vstep ; j < ly ; j++, v += vstep ){
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep ){
				vv = new Vector3(
					vtx[cnt].x,
					r*r*(0.5-vtx[cnt].y/h)/h,
					vtx[cnt].z);
				vv.normalize();
				nml[cnt] = vv;
				cnt++;
			}
		}
		nml[cnt++] = new Vector3(0,-1,0);//底面
		for ( i = 0 ; i < lx ; i++ )
			nml[cnt++] = new Vector3(0,-1,0);

		//输出纹理坐标
		Vertex2[] tx = new Vertex2[numVertex];
		cnt=0;
		tx[cnt++] = new Vertex2(0.5,1.0);//上面(Xは縮退）
		// 側面
		for ( j = 0, v = 0.0 ; j < ly ; j++, v += vstep ){
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep ){
				tx[cnt] = new Vertex2();
				tx[cnt].x = u;
				tx[cnt].y = v;
				if (Math.abs(tx[cnt].x) < EPSILON) tx[cnt].x = 0;
				if (Math.abs(tx[cnt].y) < EPSILON) tx[cnt].y = 0;
				cnt++;
			}
		}
		// 底面
		tx[cnt++] = new Vertex2(0.5,0.5);
		for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep ){
			tx[cnt] = new Vertex2(u, 0.0);
			cnt++;
		}
		// 索引设定
		int numTriangle = 2*lx*(ly-1)+2*lx;
		Index3[] ind = new Index3[numTriangle];
		int m,n,o,r,q;
		cnt = 0;
		// 側面
		for ( j = 0 ; j < ly ; j++ ){
			if (j != ly-1)
			for ( i = 0 ; i < lx ; i++ ){
				m = j*lx+i+1;
				n = (i == lx-1) ? m-i : m+1;
				r = (j+1)*lx+i+1;
				q = (i == lx-1) ? r-i : r+1;
				ind[cnt++] = new Index3(m,q,r);
				ind[cnt++] = new Index3(m,n,q);
			}
			else
			for ( i = 0 ; i < lx ; i++ ){
				m = j*lx+i;
				n = (i == lx-1) ? m-i : m+1;
				o = 0;
				ind[cnt++] = new Index3(m,n,o);
			}

		}
		//底面
		o = lx*ly+1;
		for ( i = 0 ; i < lx ; i++ ){
			m = o+1+i;
			n = (i == lx-1) ? m-i : m+1;
			ind[cnt++] = new Index3(o,n,m);

		}
		// 通过转换为三角形索引面集进行绘制
		TriangleSet ts = new TriangleSet(numTriangle, ind, numVertex, vtx, nml, tx);
		ts.draw(c,node);
	}

}
