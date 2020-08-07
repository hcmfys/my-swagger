package org.springbus.comutergraphics.CG.common;

// Ray.java



public class Ray extends MyObject {

	double t = HUGEREAL; // 視点から物体の交点までの距離
	ObjectNode hitNode; // 交差した形状ノード
	int hitIndex;//三角形のインデックスフェイスセットの場合のインデックス
	Vertex3 intersection; // 交点の世界座標
	Vertex3 intersectionLocal; // 交点のローカル座標
	Vector3 intersectionNormal; // 交点の法線ベクトル
	Vector3 intersectionU; // 交点のU方向の偏微分ベクトル
	Vector3 intersectionV; // 交点のV方向の偏微分ベクトル
	Color3 intersectionColor;//頂点カラーのある三角形の色保持
	double  u, v; // 交点における物体のローカル座標系でのテクスチャー座標値
	double  opacity; // 交点における物体にアルファテクスチャーがある場合に値(1.0: 完全不透明, 0.0:完全透明)
	boolean isNormal = false;//三角形インデックスセットの頂点法線ベクトルフラグ
	boolean isTexture = false;//三角形インデックスセットの頂点テクスチャーフラグ
	boolean isColor = false;//三角形インデックスセットの頂点カラーフラグ
	int level = 0;//レイの深さレベル
	boolean lightRay = false; // 視線のレイか交点から光源に向かうレイか
	Ray reflectedRay = null;//反射レイ
	Ray transparentRay = null;//透過レイ
	public ObjectWorld objectWorld= null;//世界データ
	double refraction = 1.0; // 現在のレイのいる空間の屈折率
	public Color3 color = null;//レイのインテンシティ保持用
	public Vertex3 origin = null;  // レイの始点
	public Vector3 direction = null; // レイの単位方向ベクトル
	int currentImageX, currentImageY; // デバッグ用
	private final static int MAXRAYLEVEL = 4;

	// コンストラクタ
	public Ray(){
		this(null,null,0,0);
	}
	public Ray(Vertex3 origin, Vector3 direction, int i, int j){
		this.t = HUGEREAL;
		this.hitNode = null;
		this.hitIndex = -1;
		this.intersection = null;
		this.intersectionLocal = null;
		this.intersectionNormal = null;
		this.level = 0;
		this.lightRay = false;
		this.reflectedRay = null;
		this.transparentRay = null;
		this.objectWorld = null;
		this.origin = new Vertex3(origin);
		this.direction = new Vector3(direction);
		this.color = new Color3(0,0,0);// 黒
		this.u = this.v = 0;
		this.opacity = 1.0;
		this.refraction = 1.0;
		this.currentImageX = i;
		this.currentImageY = j;
	}
	public Ray(Ray ray, Vector3 direction){//コピー用のコンストラクタ
		this.t = HUGEREAL;
		this.hitNode = null;
		this.hitIndex = -1;
		this.intersection = null;
		this.intersectionLocal = null;
		this.intersectionNormal = null;
		this.level = ray.level+1;
		this.lightRay = false;
		this.reflectedRay = null;
		this.transparentRay = null;
		this.objectWorld = ray.objectWorld;
		this.origin = new Vertex3(ray.intersection);
		// レイの始点を現在の当店位置よりちょっと進める
		// これは，同一交点の重複を防ぐため
		this.origin.add(
			direction.x*LITTLE,
			direction.y*LITTLE,
			direction.z*LITTLE);
		this.direction = direction;
		this.color = new Color3(0,0,0);// 黒
		this.u = 0;
		this.v = 0;
		this.opacity = 1.0;
		this.refraction = 1.0;
		this.currentImageX = ray.currentImageX;
		this.currentImageY = ray.currentImageY;
	}

	// より近い交点を探索
	public void getNearerIntersection(ObjectNode node){
		node.element.getNearerIntersection(this, node);
	}

	// 交わったオブジェクトのデータ設定（交点における法線ベクトルなど）
	public void setNearestIntersection(){
		Object3d element = this.hitNode.element;
		element.setNearestIntersection(this);
	}

	// シェーディング計算
	// 以下のシェーディングモデルで輝度を計算
	// I = M(emisssive) + (1-M(transparency))*Im +
	//	M(transparency)*It
	//
	// It = M(refraction)* (trasnmitted ray color)
	//
	// Im = SUM[ Li(on)*attenuation(i)*Irgb(i)*(Ai+Di+Si)] +
	//	M(reflection) * (reflected ray color)
	//
	// Irgb = color of i-th light source
	// 注：VRMLではAi = Iambient(i)*M(ambient)*M(diffuse)
	//	   でかつM(ambient)は3原色でなく，スカラー値です。
	//     ここでの環境光成分の実装はOpenGLに近いモデルです。
	// Ai = Iambient(i)*M(ambient)
	// Di = Ii * M(diffuse) * Lambertian(i)
	// Si = Ii * M(specular) * TorranceSparrow(i)
	// Ii = intensity of i-th light source
	final static double SHADOW_ATTENUATION = 0.2;

	public void shading(){
		Material m = this.hitNode.dummyMaterial;
		if (m == null) throw new NullPointerException();
		Color3 c = this.color; // 変数cでレイのカラーを参照
		c.add(m.emissiveColor);
		if (Math.abs(m.transparency-1)>EPSILON){ // 完全透明でない
			Color3 cm = materialIntensity();
			if (m.texture == null)
				cm.scale(1-m.transparency);
			else
				cm.scale(this.opacity);
			c.add(cm);
		}
		if (Math.abs(m.transparency)>EPSILON ||
			(m.texture != null && Math.abs(this.opacity-1)>EPSILON)){
			// 透明度がある
			Color3 ct = transmittedIntensity();
			if (m.texture == null)
				ct.scale(m.transparency);
			else
				ct.scale(1-this.opacity);
			c.add(ct);
		}
	}

	// ランバシアン（拡散反射光成分）の計算
	public double Lambertian(Vector3 light, Vector3 normal){
		double t = Vector3.innerProduct(light,normal);
		if (t < 0) t = 0;
		return t;
	}

	// ローカルな鏡面反射光成分の計算
	// Torrance & Sparrowのモデルに基づく
	public double TorranceSparrow(Vector3 light, Vector3 normal,
                                  Vector3 eye){
		Vector3 h = new Vector3(
			0.5*(light.x-eye.x),
			0.5*(light.y-eye.y),
			0.5*(light.z-eye.z));
		h.normalize();
		double s = h.innerProduct(normal);
		if (s < 0) return 0;
		Material m = this.hitNode.dummyMaterial;
		if (m.shininess <= 0) return s;
		double t = Math.pow(s,m.shininess);
		return t;
	}

	// テクスチャーの不透明度値の取得
	public double getTexelOpacity(int i, int j){
		Material m = this.hitNode.dummyMaterial;
		Texture t = m.texture;
		int value = t.texel[(t.height-j-1)*t.width+i];
		int alpha = (value >> 24) & 0xff;
		double opacity = (double)alpha/COLORLEVEL;
		return opacity;
	}

	// テクスチャーの3原色成分の取得
	public Color3 getTexelColor(int i, int j){
		Material m = this.hitNode.dummyMaterial;
		Texture t = m.texture;
		int index = (t.height-j-1)*t.width+i;
		int value = t.texel[(t.height-j-1)*t.width+i];
		int rint = (value >> 16) & 0xff;
		int gint = (value >> 8) & 0xff;
		int bint = (value) & 0xff;
		Color3 c = new Color3(
			(double)rint/COLORLEVEL,
			(double)gint/COLORLEVEL,
			(double)bint/COLORLEVEL);
		return c;
	}

	// テクスチャーの色値の計算
	public Color3 textureColor(){
		Color3 c = new Color3(0,0,0);
		Material m = this.hitNode.dummyMaterial;
		double u = this.u;
		double v = this.v;
		double ss = u * (1 - u);
		double tt = v * (1 - v);

		if (ss < 0.0 || tt < 0.0) {
			// ray.u or ray.v is out of range [0,1]
			if (!m.texture.repeatU){
				if (u < 0.0) u = 0.0;
				else if (u > 1.0) u = 1.0;
			}
			else if (ss < 0.0) // periodic
				u -= Math.floor(u);

			if (!m.texture.repeatV){
				if (v < 0.0) v = 0.0;
				else if (v > 1.0) v = 1.0;
			}
			else if (tt < 0.0) // periodic
				v -= Math.floor(v);
		}

		// 双線形補間
		if (m.texture.linearFilter){

			int iu = (int)Math.floor(u*m.texture.width);
			double ru = u * m.texture.width - iu;
			int iuu = iu+1;
			iu %= m.texture.width;
			iuu %= m.texture.width;

			int iv = (int)Math.floor(v*m.texture.height);
			double rv = v * m.texture.height - iv;
			int ivv = iv+1;
			iv %= m.texture.height;
			ivv %= m.texture.height;

			Color3 value00 = getTexelColor(iu,iv);
			Color3 value10 = getTexelColor(iuu,iv);
			Color3 value01 = getTexelColor(iu,ivv);
			Color3 value11 = getTexelColor(iuu,ivv);
			value10.subtract(value00);
			value11.subtract(value01);
			value10.scale(ru);
			value11.scale(ru);
			value00.add(value10); // bottom
			value01.add(value11); // top
			value01.subtract(value00);
			value01.scale(rv);
			value00.add(value01);

			// アルファ値（不透明度）の計算
			double val00 = getTexelOpacity(iu,iv);
			double val10 = getTexelOpacity(iuu,iv);
			double val01 = getTexelOpacity(iu,ivv);
			double val11 = getTexelOpacity(iuu,ivv);
			double bottom = val00 + ru * (val10-val00);
			double top = val01 + ru * (val11 - val01);
			double opacity = bottom + rv * (top - bottom);
			this.opacity = opacity;

			return value00;
		}
		else {//もっとも近傍の１画素だけをとってくる
			int iu = (int)Math.floor(u*m.texture.width);
			iu %= m.texture.width;
			int iv = (int)Math.floor(v*m.texture.height);
			iv %= m.texture.height;
			Color3 value00 = getTexelColor(iu,iv);
			double opacity = getTexelOpacity(iu,iv);
			this.opacity = opacity;
			return value00;
		}
	}

	// ローカルな輝度値成分の計算
	// Ai(ambient) + Di(diffuse) + Si(specular)
	public Color3 localLighting(Vector3 direction,
                                double ambientIntensity, double intensity){
		Material m = this.hitNode.dummyMaterial;
		Color3 ambient = new Color3(m.ambientColor);
		ambient.scale(ambientIntensity);
		Color3 diffuse;
		// テクスチャーがあるかどうかのチェック
		if (m.texture == null)
			diffuse = new Color3(m.diffuseColor);
		else
			diffuse = textureColor();

		// ランバシアン（拡散反射光成分）の計算
		double s = Lambertian(direction,this.intersectionNormal);
		diffuse.scale(intensity*s);

		// ローカルな鏡面反射光成分の計算
		Color3 specular = new Color3(m.specularColor);
		double t = TorranceSparrow(direction,
				this.intersectionNormal,this.direction);
		specular.scale(intensity*t);

		// 環境光成分の加算
		ambient.add(diffuse);
		ambient.add(specular);
		return ambient;
	}

	// 光源の寄与を加味した輝度値の計算
	// 点光源の場合は，付影処理もする。
	public Color3 lightIntensity(ObjectNode lightNode){
		Color3 c = new Color3(0,0,0);
		double shadowAttenuation=1.0;

		if (lightNode.element instanceof PointLight){
			PointLight p = (PointLight)lightNode.element;
			if (!p.on) return c; // スイッチがついてない
			Vertex3 lightWorldPosition =
				lightNode.getWorldPosition(p.location);
			double d = Vertex3.distance(this.intersection,
				lightWorldPosition);
			if (d > p.radius) return c; // 点光源の影響範囲外
			Vector3 direction = new Vector3(
				lightWorldPosition.x-this.intersection.x,
				lightWorldPosition.y-this.intersection.y,
				lightWorldPosition.z-this.intersection.z);
			direction.normalize();
			// 光線レイの定義
			Ray ray = new Ray(this,direction);
			ray.lightRay = true;
			ray.shoot(objectWorld.root.child);//光線レイトレーシングの開始
			if (ray.hitNode != null &&
				ray.t > 0 && ray.t < d) { // 影の中
				shadowAttenuation *= SHADOW_ATTENUATION;
			}
			ray = null;//ガーベッジコレクション用
			// 減衰率の計算
			double attenuation =
					  p.attenuation.x
					+ p.attenuation.y*d
					+ p.attenuation.z*d*d;
			if (attenuation < 1.0)
				attenuation = 1.0;
			else
				attenuation = 1.0 / attenuation;
			c.assign(p.color);
			c.scale(attenuation);

			// ローカルライティングの計算
			c.multiply(localLighting(direction,
				p.ambientIntensity,
				p.intensity));
			c.scale(shadowAttenuation);
		}
		else if (lightNode.element instanceof DirectionalLight){
			DirectionalLight p = (DirectionalLight)lightNode.element;
			if (!p.on) return c; // スイッチがついていない
			Vector3 direction = lightNode.getWorldVector(p.direction);
			direction.scale(-1);
			// 平行光源は影を作らず減衰しないと仮定
			c.assign(p.color);
			c.multiply(localLighting(direction,
				p.ambientIntensity, p.intensity));
		}
		return c;
	}

	// ローカルな輝度計算
	// 光源はレイと交差した形状ノードのシーングラフ上での兄弟か
	// 親ノードを探す
	public Color3 localIntensity(){
		ObjectNode parent = this.hitNode.parent;
		Color3 c = new Color3(0,0,0);//黒
		if (parent == null) return c;
		ObjectNode brother = parent.child;
		while (brother != null){
			if (brother.isLightNode()){
				c.add(lightIntensity(brother));
			}
			brother = brother.next;
		}
		while (parent != null){
			if (parent.isLightNode()){
				c.add(lightIntensity(parent));
			}
			parent = parent.parent;
		}
		return c;
	}

	// 材質に基づく輝度計算
	public Color3 materialIntensity(){
		// 先にローカルな反射による輝度を計算する。
		Material m = this.hitNode.dummyMaterial;
		Color3 c = localIntensity();
		if (Math.abs(m.reflection) > EPSILON){
			Color3 c2 = reflectedIntensity();
			c2.scale(m.reflection);
			c.add(c2);
		}
		return c;
	}

	// グローバルな反射光成分の計算
	public Color3 reflectedIntensity(){//反射レイの輝度
		if (this.level > MAXRAYLEVEL) return new Color3();
		Vector3 direction = ReflectedDirection();
		// 反射レイの定義
		Ray ray = new Ray(this,direction);
		this.reflectedRay = ray;
		ray.shoot(objectWorld.root.child);//反射レイトレーシングの開始
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	// グローバルな透過光成分の計算
	public Color3 transmittedIntensity(){//透過レイの輝度
		if (this.level >MAXRAYLEVEL) return new Color3();
		Vector3 direction = RefractedDirection();
		// 透過レイの定義
		Ray ray = new Ray(this,direction);
		this.transparentRay = ray;
		ray.shoot(objectWorld.root.child);//透過レイトレーシングの開始
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	// レイの交点上での視線に対する反射方向のベクトルの計算
	public Vector3 ReflectedDirection(){
		Vector3 v = new Vector3(this.intersectionNormal);
		double t = v.innerProduct(this.direction);
		v.scale(-2*t);
		v.add(this.direction);
		v.normalize();
		return v;
	}

	// スネル（Snell）の法則で屈折方向のベクトルを計算する
	// Snell's law: eta1 * sin(theta1) = eta2 * sin(theta2)
	public Vector3 RefractedDirection(){
		Material m = this.hitNode.dummyMaterial;
		if (Math.abs(m.refraction) < EPSILON)
			throw new InternalError("屈折率が0になっています。");
		double s1,c1; // s1 = sin'theta1), c1 = cos(theta1)
		double s2,c2; // s2 = sin(theta2), c2 = cos(theta2)
		double eta = this.refraction / m.refraction;
		Vector3 n = new Vector3(this.intersectionNormal);
		double t = n.innerProduct(this.direction);// = -cos(theta1);
		c1 = -t;// = cos(theta1)
		s1 = 1-c1*c1;
		if (s1 < 0.0) { s1 = 0; s2 = 0; }
		else {
			s1 = Math.sqrt(s1);// = sin(theta1)
			s2 = eta * s1;// = sin(theta2)
		}
		c2 = 1 - s2*s2;
		if (c2 < 0.0) c2 = 0;
		else c2 = Math.sqrt(c2); // = cos(theta2)
		if (c1 < 0.0) c2 = -c2;
		Vector3 p = new Vector3(this.intersectionNormal);
		p.scale(c1);
		p.add(this.direction);
		if (Math.abs(s1) < EPSILON) return(this.direction);
		p.scale(1.0/s1);
		if (p.size() < EPSILON) return(this.direction);
		p.normalize();
		Vector3 direction = new Vector3(p);
		direction.scale(s2);
		n.scale(-c2);
		direction.add(n);
		direction.normalize();
		n = null; p = null;
		return direction;
	}

	// レイトレーシング本体
	public void shoot(ObjectNode node){
		// find nearest object;
		start(node);
		if (this.t > LARGE // 遠すぎる
			|| Math.abs(t)<EPSILON // 同一点での交差を避ける
		    || this.hitNode == null) { // 交点なし
			this.color.r = this.objectWorld.background.r;
			this.color.g = this.objectWorld.background.g;
			this.color.b = this.objectWorld.background.b;
		}
		else { // 交点あり
			if (this.lightRay) return;
			setNearestIntersection();
			shading();
		}
	}

	// シーングラフの再帰的なレイと物体との交点計算
	public void start(ObjectNode node){//再帰的な交点計算
		if (node == null) return;
		if (node.isShapeNode()){//形状ノードの場合
			getNearerIntersection(node);
		}
		start(node.next);//兄弟ノードで交点計算
		start(node.child);//子供ノードで交点計算
	}
}
