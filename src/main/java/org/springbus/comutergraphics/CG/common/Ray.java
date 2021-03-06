package org.springbus.comutergraphics.CG.common;

// Ray.java



public class Ray extends MyObject {

	double t = HUGEREAL; // 视点到物体相交的距离
	ObjectNode hitNode; // 相交的形状节点
	int hitIndex;//三角形索引人脸索引
	Vertex3 intersection; // 相交世界坐标
	Vertex3 intersectionLocal; // 相交的局部坐标
	Vector3 intersectionNormal; // 相交法线向量
	Vector3 intersectionU; // 交U方向上点的偏微分向量
	Vector3 intersectionV; // 交点V方向上的偏微分向量
	Color3 intersectionColor;//为具有顶点颜色的三角形保留颜色
	double  u, v; // 相交对象的局部坐标系中的纹理坐标值
	double  opacity; // 相交处的对象具有alpha纹理时的值（1.0：完全不透明，0.0：完全透明）
	boolean isNormal = false;//三角形索引集的顶点法向矢量标志
	boolean isTexture = false;//三角形索引集的顶点纹理标志
	boolean isColor = false;//三角形索引集的顶点颜色标志
	int level = 0;//射线深度等级
	boolean lightRay = false; // 视线射线或从交叉点到光源的射线
	Ray reflectedRay = null;//反射射线
	Ray transparentRay = null;//透明射线
	public ObjectWorld objectWorld= null;//世界数据
	double refraction = 1.0; // 当前射线空间的折射率
	public Color3 color = null;//用于保持射线强度
	public Vertex3 origin = null;  // 射线的起点
	public Vector3 direction = null; //射线单位方向向量
	int currentImageX, currentImageY; // 用于调试
	private final static int MAXRAYLEVEL = 4;

	// 构造函数
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
	public Ray(Ray ray, Vector3 direction){//复制构造函数
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
		//将射线的起点从当前存储位置稍微向前移动
		//这是为了防止交叉点重复
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

	// 查找更近的交点
	public void getNearerIntersection(ObjectNode node){
		node.element.getNearerIntersection(this, node);
	}

	// 相交对象的数据设置（相交处的法向矢量等）
	public void setNearestIntersection(){
		Object3d element = this.hitNode.element;
		element.setNearestIntersection(this);
	}

	//阴影计算
	//使用以下阴影模型计算亮度
	// I = M(emisssive) + (1-M(transparency))*Im +
	//	M(transparency)*It
	//
	// It = M(refraction)* (trasnmitted ray color)
	//
	// Im = SUM[ Li(on)*attenuation(i)*Irgb(i)*(Ai+Di+Si)] +
	//	M(reflection) * (reflected ray color)
	//
	// Irgb = color of i-th light source
	//注意：VRML中的Ai = Iambient（i）* M（ambient）* M（diffuse）
	//并且M（ambient）是一个标量值，而不是三个原色。
	//这里的环境光组件的实现是类似于OpenGL的模型。
	// Ai = Iambient(i)*M(ambient)
	// Di = Ii * M(diffuse) * Lambertian(i)
	// Si = Ii * M(specular) * TorranceSparrow(i)
	// Ii = intensity of i-th light source
	final static double SHADOW_ATTENUATION = 0.2;

	public void shading(){
		Material m = this.hitNode.dummyMaterial;
		if (m == null) throw new NullPointerException();
		Color3 c = this.color; // 参考变量c中的射线颜色
		c.add(m.emissiveColor);
		if (Math.abs(m.transparency-1)>EPSILON){ // 不完全透明
			Color3 cm = materialIntensity();
			if (m.texture == null)
				cm.scale(1-m.transparency);
			else
				cm.scale(this.opacity);
			c.add(cm);
		}
		if (Math.abs(m.transparency)>EPSILON ||
			(m.texture != null && Math.abs(this.opacity-1)>EPSILON)){
			// 有透明度
			Color3 ct = transmittedIntensity();
			if (m.texture == null)
				ct.scale(m.transparency);
			else
				ct.scale(1-this.opacity);
			c.add(ct);
		}
	}

	// Lambassian（漫反射分量）计算
	public double Lambertian(Vector3 light, Vector3 normal){
		double t = Vector3.innerProduct(light,normal);
		if (t < 0) t = 0;
		return t;
	}

	//局部镜面反射分量计算
	//基于Torrance和Sparrow模型
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

	// 获取纹理不透明度值
	public double getTexelOpacity(int i, int j){
		Material m = this.hitNode.dummyMaterial;
		Texture t = m.texture;
		int value = t.texel[(t.height-j-1)*t.width+i];
		int alpha = (value >> 24) & 0xff;
		double opacity = (double)alpha/COLORLEVEL;
		return opacity;
	}

	// 获取纹理的三个原色分量
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

	// 计算纹理颜色值
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

		// 双线性插值
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

			// 计算Alpha值（不透明度）
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
		else {//仅获取最近的像素
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

	// 计算局部亮度值分量
	// Ai(ambient) + Di(diffuse) + Si(specular)
	public Color3 localLighting(Vector3 direction,
                                double ambientIntensity, double intensity){
		Material m = this.hitNode.dummyMaterial;
		Color3 ambient = new Color3(m.ambientColor);
		ambient.scale(ambientIntensity);
		Color3 diffuse;
		// 检查纹理是否存在
		if (m.texture == null)
			diffuse = new Color3(m.diffuseColor);
		else
			diffuse = textureColor();

		// Lambassian（漫反射分量）计算
		double s = Lambertian(direction,this.intersectionNormal);
		diffuse.scale(intensity*s);

		// 局部镜面反射分量的计算
		Color3 specular = new Color3(m.specularColor);
		double t = TorranceSparrow(direction,
				this.intersectionNormal,this.direction);
		specular.scale(intensity*t);

		// 添加环境光组件
		ambient.add(diffuse);
		ambient.add(specular);
		return ambient;
	}

	//计算亮度值，要考虑到光源的贡献
	//在点光源的情况下，还会执行阴影处理。
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
			// 射线射线定义
			Ray ray = new Ray(this,direction);
			ray.lightRay = true;
			ray.shoot(objectWorld.root.child);//光线追踪开始
			if (ray.hitNode != null &&
				ray.t > 0 && ray.t < d) { // 影の中
				shadowAttenuation *= SHADOW_ATTENUATION;
			}
			ray = null;//用于垃圾收集
			// 衰减计算
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

			// 局部照明计算
			c.multiply(localLighting(direction,
				p.ambientIntensity,
				p.intensity));
			c.scale(shadowAttenuation);
		}
		else if (lightNode.element instanceof DirectionalLight){
			DirectionalLight p = (DirectionalLight)lightNode.element;
			if (!p.on) return c; // 没有开关
			Vector3 direction = lightNode.getWorldVector(p.direction);
			direction.scale(-1);
			// 假设平行光源不会投射阴影并且不会衰减
			c.assign(p.color);
			c.multiply(localLighting(direction,
				p.ambientIntensity, p.intensity));
		}
		return c;
	}

	//局部亮度计算
	//光源是与射线相交的形状节点的场景图上的同级对象吗？
	//找到父节点
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

	// 基于材质的亮度计算
	public Color3 materialIntensity(){
		// 首先，计算由于局部反射引起的亮度。
		Material m = this.hitNode.dummyMaterial;
		Color3 c = localIntensity();
		if (Math.abs(m.reflection) > EPSILON){
			Color3 c2 = reflectedIntensity();
			c2.scale(m.reflection);
			c.add(c2);
		}
		return c;
	}

	// 计算整体反射光分量
	public Color3 reflectedIntensity(){//反射光线的亮度
		if (this.level > MAXRAYLEVEL) return new Color3();
		Vector3 direction = ReflectedDirection();
		// 反射光线的定义
		Ray ray = new Ray(this,direction);
		this.reflectedRay = ray;
		ray.shoot(objectWorld.root.child);//开始反射光线追踪
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	// 整体透射光分量计算
	public Color3 transmittedIntensity(){//透明射线的亮度
		if (this.level >MAXRAYLEVEL) return new Color3();
		Vector3 direction = RefractedDirection();
		// 透明射线的定义
		Ray ray = new Ray(this,direction);
		this.transparentRay = ray;
		ray.shoot(objectWorld.root.child);//开始透明光线追踪
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	// 计算射线相交处视线的反射方向向量
	public Vector3 ReflectedDirection(){
		Vector3 v = new Vector3(this.intersectionNormal);
		double t = v.innerProduct(this.direction);
		v.scale(-2*t);
		v.add(this.direction);
		v.normalize();
		return v;
	}

	// 使用斯涅尔定律计算折射方向上的向量
	// 斯涅尔定律：eta1 * sin（theta1）= eta2 * sin（theta2）
	public Vector3 RefractedDirection(){
		Material m = this.hitNode.dummyMaterial;
		if (Math.abs(m.refraction) < EPSILON)
			throw new InternalError("折射率为0。");
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

	//射线追踪体
	public void shoot(ObjectNode node){
		// find nearest object;
		start(node);
		if (this.t > LARGE // 太远
			|| Math.abs(t)<EPSILON // 避免在同一点交叉
		    || this.hitNode == null) { // 无路口
			this.color.r = this.objectWorld.background.r;
			this.color.g = this.objectWorld.background.g;
			this.color.b = this.objectWorld.background.b;
		}
		else { // 有一个路口
			if (this.lightRay) return;
			setNearestIntersection();
			shading();
		}
	}

	// 场景图与对象交点的递归射线计算
	public void start(ObjectNode node){//递归相交计算
		if (node == null) return;
		if (node.isShapeNode()){//对于形状节点
			getNearerIntersection(node);
		}
		start(node.next);//同级节点的相交计算
		start(node.child);//子节点的交点计算
	}
}
