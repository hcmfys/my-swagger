package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// ObjectNode.java　
// シーングラフ（兄弟をリンク付きリストでもつＮ分木）のノードの定義
// すべてのノードはローカルな変換行列とelementという名前のオブジェクトをもつ。
// ノードが光源のインスタンスであれば、その光源はすべての兄弟および
// その光源の子供のノードたちを照射すると仮定する。
// 材質(material)は初期設定ではnullとし、setMaterialメソッドではじめて
// 材質が定義される。材質は、自分より年下の兄弟と子供に伝搬すると仮定する。
// ただし、伝搬するのは材質がnullのノードに対してのみ。
//
//	プログラム３−１１
//		ObjectNodeクラスのコンストラクタ，addChild(),addBrother(),
//		reset(),translate(),scale(),rotate(),
//		isShapeNode(),isLightNode()メソッ
//	プログラム３−２４
//		setMaterial(),getOriginalMaterial(),getMaterial().
//		getWorldPosition(),getWorldVector(),getWorldNormal(),
//		getLocalPosition(),getLocalVector()メソッド追加

public class ObjectNode extends MyObject {

	String name = null; // シーングラフの中でのこのノードの名前
	Matrix4 mat = null; // ローカルな変換行列
	Matrix4 acm = null; // 世界からの累積行列　（計算用）
	Matrix4 inv = null; // 世界からの累積行列の逆行列　（計算用）
	Material material = null; // 材質設定用
	Material dummyMaterial = null; // 材質伝搬用（かつ計算用）
	Object3d element = null; // ノードの要素
	ObjectNode next = null;// 兄弟ノード
	public ObjectNode child = null;// 子供ノード
	ObjectNode parent = null;// 親ノード

	// コンストラクタ
	ObjectNode(Object3d element, String name) {
		this.element = element;
		this.name = name;
		this.mat = new Matrix4();//4x4行列の初期化
		this.acm = null;
		this.inv = null;
		this.material = null;
		this.dummyMaterial = null;
		this.next = null;
		this.parent = null;
		this.child = null;
	}
	private ObjectNode(ObjectNode node){ // ノードの再帰的コピー
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
	// コピー用のコンストラクタ
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

	// ノードのタイプチェック
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
			System.out.println(ss+"はシーングラフのノードに出来ません。");
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

	// 光源ノードかどうかのチェック
	public boolean isLightNode(){
		if (element instanceof PointLight ||
		    element instanceof DirectionalLight)
		return true;
		return false;
	}

	// ノードの複製（コピーコンストラクタで使用）
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

	// 子供の追加
	public ObjectNode addChild(Object3d element, String name){
		if (isInvalidNodeType(element)) 
			throw new InternalError("子供ノードに出来ません");
		ObjectNode t=null;
		if (this.child == null){//子供がいない場合
			this.child = t = new ObjectNode(element,name);
		}
		else // 子供がいたら兄弟の末尾に追加
			t = this.child.addBrother(element,name);
		t.parent = this;// 親は同じ
		return t;
	} 
	public ObjectNode addChild(ObjectNode node){
		ObjectNode t;
		if (node == null) throw new NullPointerException();
		if (this.child == null)//子供がいない場合
			this.child = t = node;
		else //子供がいたら兄弟の末尾に追加
			t = this.child.addBrother(node);
		t.parent = this;//親は同じ
		return t;
	}

	// 親のデータ返答
	public ObjectNode getParent(ObjectNode node){
		return node.parent;
	}

	// 兄弟の追加
	public ObjectNode addBrother(Object3d element, String name){
		ObjectNode t=null;
		if (this.next == null) { // 初めての兄弟
			this.next = t = new ObjectNode(element,name);
		}
		else {//兄弟リストの末尾に追加
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
		if (this.next == null){ // 初めての兄弟
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
