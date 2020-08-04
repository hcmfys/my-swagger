package org.springbus.comutergraphics.CG.common;


// Sphere.java（领域定义）
//球类
//程序3-6
// Sphere类构造函数

public class Sphere extends Object3d {

	// f(x,y,z) = x*x+y*y+z*z-r*r = 0
	double r; // 球体半径　(r>0)
	BoundingBox bbox;//边界框
	int numUTessellate;//U方向镶嵌
	int numVTessellate;//V方向细分

	// 构造函数
	public Sphere(double r){
		if (r <= 0) throw new InternalError("球半径设置错误");
		Vertex3 center;//边界框的中心位置
		double xsize, ysize, zsize;//边框尺寸
		this.r = r;
		this.numUTessellate = 40;
		this.numVTessellate = 20;
		//边框套
		center = new Vertex3();
		center.x = center.y = center.z = 0;
		xsize = ysize = zsize = 2*r*(1+DELTA);
		this.bbox = new BoundingBox(center,xsize,ysize,zsize);
	}
	public Sphere(){
		// 默认球体的半径为1.0
		this(1.0);
	}

	//镶嵌设置
	public void setTessellate(int unum, int vnum){
		if (unum <= 0 || vnum <= 0) throw
			new InternalError("镶嵌数必须为正");
		numUTessellate = unum;
		numVTessellate = vnum;
	}

	public void  getNearerIntersection(Ray ray, ObjectNode p){
		if (ray == null || p == null)
			throw new NullPointerException();
		if (!(p.element instanceof Sphere)) return;
		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);

		//射线与边界框的交点
		if (!bbox.isHit(o,d)) return;

		//计算射线与球体的交点
		//（o.x + t * d.x）^ 2 +（o.y + t * d.y）^ 2 +（o.z + t * d.z）^ 2 = r ^ 2
		//我们称其为a * t ^ 2 + b * t + c = 0。
		//实际上a应该是1.0（因为射线方向向量的长度是1）
		//使用二次解公式。
		double a,b,c,det,t1,t2,t;
		a = d.x * d.x + d.y * d.y + d.z * d.z;
		b = 2 * (o.x * d.x + o.y * d.y + o.z * d.z);
		c = o.x * o.x + o.y * o.y + o.z * o.z - r*r;
		det = b * b - 4.0 * a * c;
		if (det < 0.0 || Math.abs(a)<EPSILON) return;
		det = Math.sqrt(det);
		if (Math.abs(det)<EPSILON){ // 解はひとつ
			t = (-b) / (2.0 * a);
		}
		else { // 小解决方案
			t1 = (-b - det) / (2.0 * a);
			if (Math.abs(t1)>EPSILON && t1 > 0.0) t = t1;
			else {
				t2 = (-b + det) / (2.0 * a);
				if (t2 < 0.0 || Math.abs(t2)<EPSILON)
 					return;
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
		if (distance < ray.t) {//当确定球比以往更近时
			ray.t = distance;
			ray.hitNode = p;
			ray.intersection = worldSolution;
			ray.intersectionLocal = localSolution;
			return;
		}
		return;
	}

	// 球与射线相交处的法向矢量等的计算
	public void setNearestIntersection(Ray ray){

		Vector3 localNormal = new Vector3(ray.intersectionLocal);
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

		double x = ray.intersectionLocal.x;
		double y = ray.intersectionLocal.y;
		double z = ray.intersectionLocal.z;

		//局部球面的参数表示
		// x = -r * sin（phi）* sin（theta）
		// y = -r * cos（phi）
		// z = -r * sin（phi）* cos（theta）
		// 0 <= phi <= PI注意：acos（1）= 0，acos（-1）= PI acos => [0，PI]
		double phi = Math.acos(-y/this.r);
		// 0 <= theta <= 2*PI  注: atan2=>[-PI,PI]
		double theta = Math.PI + Math.atan2(x,z);
		double u = theta / (2 * Math.PI);
		double v = phi / Math.PI;
		Vector3 w = new Vector3(u,v,1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	//球体的线图
	public void draw(Camera c, ObjectNode node){
		if (!(node.element instanceof Sphere)) return;
		Sphere p = (Sphere)node.element;
		double z0 = -c.focalLength;

		Vertex3 localCenter = new Vertex3(0,0,0);
		Vertex3 worldCenter = node.getWorldPosition(localCenter);
		Vertex3 cameraCenter = c.getCameraPosition(worldCenter);
		if (cameraCenter.z-p.r >= z0) return;

		//根据球体的中心是否在屏幕后面
		//粗剪测试（用于查看体积）
		double xmax = c.screenMaxX;
		double xmin = c.screenMinX;
		double ymax = c.screenMaxY;
		double ymin = c.screenMinY;
		if (!c.parallel){
			// 球体的中心是否在正确的裁剪平面之外？
			Vector3 v = new Vector3(-z0,0,xmax);
			double t = v.innerProduct(cameraCenter);
			//找到球心与右裁剪平面之间的距离
			//平面ax + by + cz + d = 0与（x0，y0，z0）h ^ 2之间的距离的平方为
			// h^2 = |a*x0+b*y0+c*z0+d|^2/(a*a+b*b+c*c)
			double u = t*t - p.r*p.r*v.size2();
			if (t >= 0 && u > 0) return; //在右裁剪平面之外
			// 球体的中心是否在左侧裁剪平面之外？
			v = new Vector3(z0,0,-xmin);
			t = v.innerProduct(cameraCenter);
			u = t*t - p.r*p.r*v.size2();
			if (t >= 0 && u > 0) return;//左裁剪平面外
			// 球体的中心是否在上裁剪平面上方？
			v = new Vector3(0,-z0,ymax);
			t = v.innerProduct(cameraCenter);
			u = t*t - p.r*p.r*v.size2();
			if (t >= 0 && u > 0) return;//上裁剪平面外
			// 球体的中心是否在下裁剪平面下方？
			v = new Vector3(0,z0,-ymin);
			t = v.innerProduct(cameraCenter);
			u = t*t - p.r*p.r*v.size2();
			if (t >= 0 && u > 0) return;//下裁剪平面外
		}
		else {
			if (cameraCenter.x-r > c.screenMaxX) return;
			if (cameraCenter.x+r < c.screenMinX) return;
			if (cameraCenter.y-r > c.screenMaxY) return;
			if (cameraCenter.y+r < c.screenMinY) return;
		}
		//细分球体时细切线段
		drawSphere(c,node,numUTessellate,numVTessellate);
	}

	//获取球体上的坐标值
	public Vertex3 getSphereCoord(double r, double u0, double v0){
		double u = 2.0 * Math.PI * u0;
		double v = Math.PI * v0;
		double s = -Math.sin(v);
		double x = s * Math.sin(u);
		double y = -Math.cos(v);
		double z = s * Math.cos(u);
		if (Math.abs(x)<EPSILON) x = 0;
		if (Math.abs(y)<EPSILON) y = 0;
		if (Math.abs(z)<EPSILON) z = 0;
		Vertex3 w = new Vertex3(r*x,r*y,r*z);
		return w;
	}

	// 画一个球
	public void drawSphere(Camera c, ObjectNode node, int lx, int ly ){
		double u, v;
		int i,j,k;
		if (lx <= 0 || ly <= 0) return;
		if (!(node.element instanceof Sphere)) return;
		Sphere p = (Sphere)(node.element);
		double du = 1.0;
		double dv = 1.0;
		double ustep = du / lx;
		double vstep = dv / ly;
		//输出顶点
		int numVertex = lx*(ly-1)+2;
		Vertex3[] vtx = new Vertex3[numVertex];//北极+南极+其他
		int cnt=0;
		vtx[cnt++] = getSphereCoord(p.r,0,0);//南极
		for ( j = 0, v = vstep ; j < ly-1 ; j++, v += vstep )
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep )
				vtx[cnt++] = getSphereCoord(p.r,u,v);
		vtx[cnt++] = getSphereCoord(p.r,0,1);//北极
		//输出法线向量
		Vector3[] nml = new Vector3[numVertex];
		cnt=0;
		nml[cnt++] = new Vector3(0,-1,0);//南极
		for ( j = 0, v = vstep ; j < ly-1 ; j++, v += vstep )
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep ){
				nml[cnt] = new Vector3(vtx[cnt]);
				nml[cnt].scale(1.0/r);
				cnt++;
		}
		nml[cnt++] = new Vector3(0,1,0);//北极
		//输出纹理坐标
		Vertex2[] tx = new Vertex2[numVertex];
		cnt=0;
		tx[cnt++] = new Vertex2(0,0);
		for ( j = 0, v = vstep ; j < ly-1 ; j++, v += vstep )
			for ( i = 0, u = 0.0 ; i < lx ; i++, u += ustep ){
				tx[cnt] = new Vertex2();
				tx[cnt].x = u;
				tx[cnt].y = v;
				if (Math.abs(tx[cnt].x) < EPSILON) tx[cnt].x = 0;
				if (Math.abs(tx[cnt].y) < EPSILON) tx[cnt].y = 0;
				cnt++;
			}
		tx[cnt++] = new Vertex2(0,1);
		// 索引设定
		int numTriangle = 2*lx*(ly-1);
		Index3[] ind = new Index3[numTriangle];
		int m,n,o,r,q;
		cnt = 0;
		// 南极
		for ( i = 0 ; i < lx ; i++ ){
			m = i+1;
			n = (i == lx-1) ? 0 : m+1;
			ind[cnt++] = new Index3(m,0,n);
		}
		// 中央
		for ( j = 0 ; j < ly-2 ; j++ )
			for ( i = 0 ; i < lx ; i++ ){
				m = j*lx+i+1;
				n = (i == lx-1) ? m-i : m+1;
				r = (j+1)*lx+i+1;
				q = (i == lx-1) ? r-i : r+1;
				ind[cnt++] = new Index3(r,m,n);
				ind[cnt++] = new Index3(r,n,q);
		}
		//北极
		o = lx*(ly-1)+1;
		for ( i = 0 ; i < lx ; i++ ){
			m = lx*(ly-2)+1+i;
			n = (i == lx-1) ? m-i : m+1;
			ind[cnt++] = new Index3(o,m,n);
		}
		// 通过近似三角形索引面集进行绘制
		TriangleSet ts = new TriangleSet(numTriangle,ind,numVertex,vtx,nml,tx);
		ts.draw(c,node);
	}

	//球の印刷
	public void print(){
		System.out.println("球: 半径 = "+r);
	}

}
