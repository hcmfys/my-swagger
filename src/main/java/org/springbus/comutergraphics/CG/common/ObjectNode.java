package org.springbus.comutergraphics.CG.common;

// ObjectNode.java
//场景图的节点定义（链表中具有同级的N元树）
//每个节点都有一个局部转换矩阵和一个名为element的对象。
//如果该节点是光源的实例，则该光源是
//假设您要照亮光源的子节点。
//默认情况下，材质为null
//定义材料。假设材料传播给了年幼的兄弟姐妹和孩子。
//但是，它仅传播到材料为null的节点。

public class ObjectNode extends MyObject {

	String name = null; //场景图中此节点的名称
	Matrix4 mat = null; //局部变换矩阵
	Matrix4 acm = null; //来自世界的累积矩阵（用于计算）
	Matrix4 inv = null; //来自世界的累积矩阵的逆矩阵（用于计算）
	Material material = null; //用于材质设置
	Material dummyMaterial = null; //用于材料传播（和计算）
	Object3d element = null; //节点元素
	ObjectNode next = null; //兄弟节点
	public ObjectNode child = null; //子节点
	ObjectNode parent = null; //父节点

	// 构造函数
	ObjectNode(Object3d element, String name) {
		this.element = element;
		this.name = name;
		this.mat = new Matrix4();//初始化4x4矩阵
		this.acm = null;
		this.inv = null;
		this.material = null;
		this.dummyMaterial = null;
		this.next = null;
		this.parent = null;
		this.child = null;
	}
	private ObjectNode(ObjectNode node){ // 节点的递归副本
		this.element = node.duplicate(node.element);
		this.name = node.name;
		this.mat = new Matrix4(node.mat);
		this.acm = null;
		this.inv = null;
		if (node.material == null) this.material = null;
		else this.material = new Material(node.material);
		this.dummyMaterial = null;
		if (node.next == null) this.next = null;
		else this.next = new ObjectNode(node.next);
		this.parent = node.parent;
		if (node.child == null) this.child = null;
		else this.child = new ObjectNode(node.child);
	}
	// 复制构造函数
	private ObjectNode(ObjectNode node, String name){
 		if (node == null) throw new NullPointerException();
		this.element = node.duplicate(node.element);
		this.name = name;
		this.mat = new Matrix4(node.mat);
		this.acm = null;
		this.inv = null;
		if (node.material == null) this.material = null;
		else this.material = new Material(node.material);
		this.dummyMaterial = null;
		this.next = null; // 兄弟は伝搬しない
		this.parent = null; // 親は伝搬しない
		// 子供はnullでなければ伝搬する
		if (node.child == null) this.child = null;
		else this.child = new ObjectNode(node.child);
	}

	// 节点类型检查
	private boolean isInvalidNodeType(Object element){
		if (element instanceof Material ||
		    element instanceof Texture ||
		    element instanceof Camera ||
		    element instanceof CameraList ||
		    element instanceof ObjectNode ||
		    element instanceof ObjectWorld ||
		    element instanceof Ray ||
		    element instanceof Matrix4 ||
		    element instanceof Matrix3 ||
		    element instanceof Vector4 ||
		    element instanceof Vector3 ||
		    element instanceof Vertex3 ||
		    element instanceof Vertex4 ||
		    element instanceof Vertex2 ||
		    element instanceof Index3  ||
		    element instanceof Color3
		) {
			String s = element.toString();
			int index = s.indexOf((int)'@');
			String ss = s.substring(0,index);
			System.out.println(ss+"不能是场景图中的节点。");
			return true;
		}
		return false;
	}

	// 形状ノードかどうかのチェック
	public boolean isShapeNode(){
		if (element instanceof Sphere ||
		    element instanceof TriangleSet ||
		    element instanceof Box ||
		    element instanceof Cone ||
		    element instanceof Cylinder)
		return true;
		return false;
	}

	// 检查光源节点
	public boolean isLightNode(){
		if (element instanceof PointLight ||
		    element instanceof DirectionalLight)
		return true;
		return false;
	}


	//复制节点（在复制构造函数中使用）
	public Object3d duplicate(Object3d element){
		if (element instanceof Sphere){
			Sphere p = (Sphere) element;
			Sphere newP = new Sphere(p.r);
			return newP;
		}
		else if (element instanceof Box){
			Box p = (Box)element;
			Box newP = new Box(p.xsize,p.ysize,p.zsize);
			return newP;
		}
		else if (element instanceof Cone){
			Cone p = (Cone)element;
			Cone newP = new Cone(p.r,p.h);
			return newP;
		}
		else if (element instanceof Cylinder){
			Cylinder p = (Cylinder)element;
			Cylinder newP = new Cylinder(p.r,p.h);
			return newP;
		}
		else if (element instanceof TriangleSet){
			TriangleSet p = (TriangleSet)element;
			TriangleSet newP = new TriangleSet(
				p.numTriangle, p.faceIndex,
				p.numVertex, p.vertex,
				p.colorIndex, p.numColor, p.color,
				p.normalIndex, p.numNormal, p.normal,
				p.textureIndex, p.numTexture, p.texture);
			return newP;
		}
		else if (element instanceof Group){
			Group p = (Group)element;
			Group newP = new Group();
			return newP;
		}
		else if (element instanceof PointLight){
			PointLight p = (PointLight)element;
			PointLight newP = new PointLight(p);
			return newP;
		}
		else if (element instanceof DirectionalLight){
			DirectionalLight p = (DirectionalLight)element;
			DirectionalLight newP =
				new DirectionalLight(p);
			return newP;
		}
		return null;
	}

	// 加子
	public ObjectNode addChild(Object3d element, String name){
		if (isInvalidNodeType(element))
			throw new InternalError("不能是子节点");
		ObjectNode t=null;
		if (this.child == null){//如果你没有孩子
			this.child = t = new ObjectNode(element,name);
		}
		else // 如果有孩子，添加到兄弟姐妹中
			t = this.child.addBrother(element,name);
		t.parent = this;// 父母都一样
		return t;
	}
	public ObjectNode addChild(ObjectNode node){
		ObjectNode t;
		if (node == null) throw new NullPointerException();
		if (this.child == null)//如果你没有孩子
			this.child = t = node;
		else //如果有孩子，添加到兄弟姐妹中
			t = this.child.addBrother(node);
		t.parent = this;//父母都一样
		return t;
	}

	// 父级数据响应
	public ObjectNode getParent(ObjectNode node){
		return node.parent;
	}

	// 添加同级
	public ObjectNode addBrother(Object3d element, String name){
		ObjectNode t=null;
		if (this.next == null) { // 第一兄弟
			this.next = t = new ObjectNode(element,name);
		}
		else {//添加到同级列表的末尾
			ObjectNode q;
			ObjectNode p = this.next;
			do {
				q = p;
				p = p.next;
			} while (p != null);
			t = q.next = new ObjectNode(element,name);
		}
		t.parent = this.parent; // 親は共通
		return t;
	}
	public ObjectNode addBrother(ObjectNode node){
		ObjectNode t=null;
		if (node == null) throw new NullPointerException();
		if (this.next == null){ // 第一兄弟
			this.next = t = node;
		}
		else {
			ObjectNode q;
			ObjectNode p = this.next;
			do {
				q = p;
				p = p.next;
			} while (p != null);
			t = q.next = node;
		}
		t.parent = this.parent; // 親は共通
		return t;
	}

	// ノードの名前の取得
	public String getObjectNodeName(){
		return name;
	}

	// ノードの変換行列のリセット
	public void reset(){
		mat.identity();
	}

	// ノードの平行移動
	public void translate(Vertex3 v){
		mat.a[3] += v.x;
		mat.a[7] += v.y;
		mat.a[11] += v.z;
	}
	public void translate(double x, double y, double z){
		mat.a[3] += x;
		mat.a[7] += y;
		mat.a[11] += z;
	}

	// ノードの拡大縮小
	public void scale(Vector3 v){
		mat.a[0] *= v.x;
		mat.a[5] *= v.y;
		mat.a[10] *= v.z;
	}
	public void scale(double x, double y, double z){
		mat.a[0] *= x;
		mat.a[5] *= y;
		mat.a[10] *= z;
	}

	// ノードの回転
	public void rotate(Vector3 axis, double theta){
		Matrix4 m = new Matrix4();
		Matrix4 m2 = m.rotate(axis, theta);
		mat.multiply(m2);
	}
	public void rotate(double dx, double dy, double dz, double theta){
		Vector3 v = new Vector3(dx,dy,dz);
		rotate(v,theta);
	}
	public void rotate(Vector4 v){
		rotate(v.x,v.y,v.z,v.w);
	}

	// Material（材質データ）ノードをセット
	public void setMaterial(Material material){
		if (material == null) throw new NullPointerException();
		this.material = material;
		this.dummyMaterial = material;
	}
	public Material getOriginalMaterial(){
		return material;
	}
	public Material getMaterial(){
		return dummyMaterial;
	}

	// 累積行列を使ってローカル座標値を世界座標値に変換
	public Vertex3 getWorldPosition(Vertex3 v){
		if (v == null) throw new NullPointerException();
		Vertex4 p = new Vertex4(v.x,v.y,v.z,1.0);
		Vertex4 q = this.acm.multiplyVertex4(p);
		Vertex3 r = new Vertex3(q.x,q.y,q.z);
		return r;
	}
	public Vertex3 getWorldPosition(double x, double y, double z){
		Vertex3 v = new Vertex3(x,y,z);
		return getWorldPosition(v);
	}

	// 累積行列を使ってローカルベクトル値を世界ベクトル値に変換
	public Vector3 getWorldVector(Vector3 v){
		if (v == null) throw new NullPointerException();
		if (this.acm == null) throw new NullPointerException();
		Matrix3 m = new Matrix3(
			acm.a[0],acm.a[1],acm.a[2],
			acm.a[4],acm.a[5],acm.a[6],
			acm.a[8],acm.a[9],acm.a[10]);
		return m.multiplyVector3(v);
	}
	public Vector3 getWorldVector(double x, double y, double z){
		Vector3 v = new Vector3(x,y,z);
		return getWorldVector(v);
	}

	// 累積行列を使ってローカルな法線ベクトル値を世界法線ベクトル値に変換
	// 法線ベクトルの場合、累積行列の転置行列の逆行列を使う！
	public Vector3 getWorldNormal(Vector3 v){
		if (v == null) throw new NullPointerException();
		Matrix4 m = null;
		try {
			m = acm.transpose().inverse();
		}
		catch (SingularMatrixException e){ }
		if (m == null) return null;
		Vector4 p = m.multiplyVector4(new Vector4(v.x,v.y,v.z,1));
		Vector3 q = new Vector3(p.x,p.y,p.z);
		return q;
	}
	public Vector3 getWorldNormal(double x, double y, double z){
		Vector3 v = new Vector3(x,y,z);
		return getWorldNormal(v);
	}

	// 累積行列の逆行列を使って世界座標値をローカルな座標値に変換
	public Vertex3 getLocalPosition(Vertex3 v){
		if (v == null) throw new NullPointerException();
		if (this.inv == null) throw new NullPointerException();
		Vertex4 p = new Vertex4(v.x,v.y,v.z,1.0);
		Vertex4 q = this.inv.multiplyVertex4(p);
		Vertex3 r = new Vertex3(q.x,q.y,q.z);
		return r;
	}
	public Vertex3 getLocalPosition(double x, double y, double z){
		Vertex3 v = new Vertex3(x,y,z);
		return getLocalPosition(v);
	}

	// 累積行列の逆行列を使って世界座標でのベクトルをローカルな座標系
	// でのベクトルに変換
	public Vector3 getLocalVector(Vector3 v){
		if (v == null) throw new NullPointerException();
		if (this.inv == null) throw new NullPointerException();
		Matrix3 m = new Matrix3(
			inv.a[0],inv.a[1],inv.a[2],
			inv.a[4],inv.a[5],inv.a[6],
			inv.a[8],inv.a[9],inv.a[10]);
		Vector3 r = m.multiplyVector3(v);
		return r;
	}
	public Vector3 getLocalVector(double x, double y, double z){
		Vector3 v = new Vector3(x,y,z);
		return getLocalVector(v);
	}

}
