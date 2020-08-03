package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Box.java　（箱の定義）
// 直方体のクラス
//	プログラム３−９
//		Boxクラスのコンストラクタ

public class Box extends Object3d {

	// F = x - xsize/2 = 0 && yt*(1-yt)>=0 && zt*(1-zt)>=0 ||
	// F = y - ysize/2 = 0 && xt*(1-xt)>=0 && zt*(1-zt)>=0 ||
	// F = z - zsize/2 = 0 && xt*(1-xt)>=0 && yt*(1-yt)>=0 ||
	// F = x + xsize/2 = 0 && yt*(1-yt)>=0 && zt*(1-zt)>=0 ||
	// F = y + ysize/2 = 0 && xt*(1-xt)>=0 && zt*(1-zt)>=0 ||
	// F = z + zsize/2 = 0 && xt*(1-xt)>=0 && yt*(1-yt)>=0 
	double xsize; // 箱のＸ軸方向のサイズ　(xsize>0)
	double ysize; // 箱のＹ軸方向のサイズ　(ysize>0)
	double zsize; // 箱のＺ軸方向のサイズ　(zsize>0)

	// コンストラクタ
	public Box(double xsize, double ysize, double zsize){
		if (xsize <= 0 || ysize <= 0 || zsize <= 0)
			throw new InternalError("箱のパラメータエラー");
		this.xsize = xsize;
		this.ysize = ysize;
		this.zsize = zsize;
	}
	public Box(){
		// デフォルトの箱のサイズは各軸とも2.0
		this(2.0,2.0,2.0);
	}

	public void print(){
		System.out.println("Box: (xs,ys,zs)=("+
		xsize+","+ysize+","+zsize+")");
	}

	public boolean isInsideBox(double x, double y, double z){
		double xt = (2*x+xsize)/(2*xsize);
		double yt = (2*y+ysize)/(2*ysize);
		double zt = (2*z+zsize)/(2*zsize);
		if (xt*(1-xt) >= 0 &&
		    yt*(1-yt) >= 0 &&
		    zt*(1-zt) >= 0) return true;
		return false;
	}

	public void  getNearerIntersection(Ray ray, ObjectNode p){
		if (ray == null || p == null)
			throw new NullPointerException();
		if (!(p.element instanceof Box)) return;
		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);

		// レイと箱の交点計算
		double t, t1, t2, x, y, z;
		double xt, yt, zt;

		t = HUGE;
		// Z = zsize/2  || Z = -zsize/2 平面との交点計算
		// o.z+t*d.z = zsize/2 (or -zsize/2)
		if (Math.abs(d.z)>EPSILON){
			t1 = (zsize/2-o.z)/d.z;
			x = o.x + t1 * d.x;
			y = o.y + t1 * d.y;
			xt = (2*x+xsize)/(2*xsize);
			yt = (2*y+ysize)/(2*ysize);
			if (t1 > 0 && t1 < t &&
				xt*(1-xt)>=0 &&
				yt*(1-yt)>=0) 
				t = t1;
			t2 = (-zsize/2-o.z)/d.z;
			x = o.x + t2 * d.x;
			y = o.y + t2 * d.y;
			xt = (2*x+xsize)/(2*xsize);
			yt = (2*y+ysize)/(2*ysize);
			if (t2 > 0 && t2 < t &&
				xt*(1-xt)>=0 &&
				yt*(1-yt)>=0) 
				t = t2;
		}

		// X = xsize/2  || X = -xsize/2 平面との交点計算
		// o.x+t*d.x = xsize/2 (or -xsize/2)
		if (Math.abs(d.x)>EPSILON){
			t1 = (xsize/2-o.x)/d.x;
			z = o.z + t1 * d.z;
			y = o.y + t1 * d.y;
			zt = (2*z+zsize)/(2*zsize);
			yt = (2*y+ysize)/(2*ysize);
			if (t1 > 0 && t1 < t &&
				zt*(1-zt)>=0 &&
				yt*(1-yt)>=0) 
				t = t1;
			t2 = (-xsize/2-o.x)/d.x;
			z = o.z + t2 * d.z;
			y = o.y + t2 * d.y;
			zt = (2*z+zsize)/(2*zsize);
			yt = (2*y+ysize)/(2*ysize);
			if (t2 > 0 && t2 < t &&
				zt*(1-zt)>=0 &&
				yt*(1-yt)>=0) 
				t = t2;
		}

		// Y = ysize/2  || Y = -ysize/2 平面との交点計算
		// o.y+t*d.y = ysize/2 (or -ysize/2)
		if (Math.abs(d.y)>EPSILON){
			t1 = (ysize/2-o.y)/d.y;
			z = o.z + t1 * d.z;
			x = o.x + t1 * d.x;
			zt = (2*z+zsize)/(2*zsize);
			xt = (2*x+xsize)/(2*xsize);
			if (t1 > 0 && t1 < t &&
				zt*(1-zt)>=0 &&
				xt*(1-xt)>=0) 
				t = t1;
			t2 = (-ysize/2-o.y)/d.y;
			z = o.z + t2 * d.z;
			x = o.x + t2 * d.x;
			zt = (2*z+zsize)/(2*zsize);
			xt = (2*x+xsize)/(2*xsize);
			if (t2 > 0 && t2 < t &&
				zt*(1-zt)>=0 &&
				xt*(1-xt)>=0) 
				t = t2;
		}

		if (t > LARGE) return;
		Vertex3 localSolution = new Vertex3(
			o.x + t * d.x,
			o.y + t * d.y,
			o.z + t * d.z
		);
		Vertex3 worldSolution = p.getWorldPosition(localSolution);
		double distance = Vertex3.distance(ray.origin,worldSolution);
		if (distance < ray.t && distance > EPSILON) {
			ray.t = distance;
			ray.hitNode = p;
			ray.intersection = worldSolution;
			ray.intersectionLocal = localSolution;
			return;
		}	
		return;
	}

	// 箱とレイの交点での法線ベクトルなどの計算
	public void setNearestIntersection(Ray ray){
		double x = ray.intersectionLocal.x;
		double y = ray.intersectionLocal.y;
		double z = ray.intersectionLocal.z;
		Vector3 localNormal;
		Box p = (Box)ray.hitNode.element;
		int type=0;
		if (Math.abs(z-p.zsize/2) < EPSILON){ 
			// Z = zsize/2と交差
			localNormal = new Vector3(0,0,1);
			type = 1;
		}
		else if (Math.abs(z+p.zsize/2)<EPSILON){
			// Z = -zsize/2 と交差
			localNormal = new Vector3(0,0,-1);
			type = 2;
		}
		else if (Math.abs(x-p.xsize/2) < EPSILON){
			// X = xsize/2と交差
			localNormal = new Vector3(1,0,0);
			type = 3;
		}
		else if (Math.abs(x+p.xsize/2) < EPSILON){
			// X = -xsize/2と交差
			localNormal = new Vector3(-1,0,0);
			type = 4;
		}
		else if (Math.abs(y-p.ysize/2) < EPSILON){
			// Y = ysize/2と交差
			localNormal = new Vector3(0,1,0);
			type = 5;
		}
		else if (Math.abs(y+p.ysize/2) < EPSILON){
			// Y = -ysize/2と交差
			localNormal = new Vector3(0,-1,0);
			type = 6;
		}
		else {
			System.out.println(
			"箱のどの面とも交差してません");
			localNormal = new Vector3(0,1,0);
		}

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

		// 箱のテクスチャー座標計算
		double u,v;
		if (type == 1){
			u = (2*x+p.xsize)/(2*p.xsize);
			v = (2*y+p.ysize)/(2*p.ysize);
		}
		else if (type == 2){
			u = (2*(-x)+p.xsize)/(2*p.xsize);
			v = (2*y+p.ysize)/(2*p.ysize);
		}
		else if (type == 3){
			u = (2*(-z)+p.zsize)/(2*p.zsize);
			v = (2*y+p.ysize)/(2*p.ysize);
		}
		else if (type == 4){
			u = (2*z+p.zsize)/(2*p.zsize);
			v = (2*y+p.ysize)/(2*p.ysize);
		}
		else if (type == 5){
			u = (2*x+p.xsize)/(2*p.xsize);
			v = (2*(-z)+p.zsize)/(2*p.zsize);
		}
		else if (type == 6){
			u = (2*x+p.xsize)/(2*p.xsize);
			v = (2*z+p.zsize)/(2*p.zsize);
		}
		else {
			System.out.println(
			"箱のどの面とも交差してません");
			u = v = 0;
		}

		Vector3 w = new Vector3(u,v,1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	//箱の線画
	public void draw(Camera c, ObjectNode node){
		if (!(node.element instanceof Box)) return;
		//箱の線画
		drawBox(c, node);
	}

	public void drawBox(Camera c, ObjectNode node ){
		int i,j,k;
		Box p = (Box)(node.element);
   		//頂点を出力
		int numVertex = 24;
		Vertex3[] vtx = new Vertex3[numVertex];
		int cnt=0;
		// Y = ysize/2平面
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,-zsize/2);
		// Y = -ysize/2平面
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,zsize/2);
		// X = xsize/2平面
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,zsize/2);
		// X = -xsize/2平面
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,-zsize/2);
		// Z = zsize/2平面
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,zsize/2);
		// Z = -zsize/2平面
		vtx[cnt++] = new Vertex3(xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,-ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(-xsize/2,ysize/2,-zsize/2);
		vtx[cnt++] = new Vertex3(xsize/2,ysize/2,-zsize/2);

		//法線ベクトルを出力
		Vector3[] nml = new Vector3[numVertex];
		cnt=0;
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(0,1,0);
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(0,-1,0);
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(1,0,0);
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(-1,0,0);
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(0,0,1);
		for (i = 0 ; i < 4 ; i++)
			nml[cnt++] = new Vector3(0,0,-1);

		//テクスチャー座標を出力
		Vertex2[] tx = new Vertex2[numVertex];
		cnt=0;
		for ( i = 0 ; i < 6; i++){
			tx[cnt++] = new Vertex2(0,0);
			tx[cnt++] = new Vertex2(1,0);
			tx[cnt++] = new Vertex2(1,1);
			tx[cnt++] = new Vertex2(0,1);
		}

		// インデックスの設定
		int numTriangle = 12;
		cnt = 0;
		Index3[] ind = new Index3[numTriangle];
		for (i = 0 ; i < 24 ; i += 4){
			ind[cnt++] = new Index3(i,i+1,i+2);
			ind[cnt++] = new Index3(i,i+2,i+3);
		}
		TriangleSet ts = new TriangleSet(numTriangle,ind,numVertex,vtx,
			nml,tx);
		ts.draw(c,node);
	}
}
