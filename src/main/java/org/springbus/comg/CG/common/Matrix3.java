package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Matrix3クラス
// 3x3行列のクラス

public class Matrix3 extends MyObject {

	public double a[]; // ここに３ｘ３の行列の要素を入れる
	/*
		| a[0] a[1] a[2] |
		| a[3] a[4] a[5] |
		| a[6] a[7] a[8] |
	*/

	// コンストラクタ
	public Matrix3(double b[]){
		this.a = new double[9];
		for (int i=0;i<9;i++) this.a[i] = b[i];
	}
	public Matrix3( double a0, double a1, double a2,
			double a3, double a4, double a5,
			double a6, double a7, double a8){
		this.a = new double[9];
		this.a[0] = a0; this.a[1] = a1; this.a[2] = a2;
		this.a[3] = a3; this.a[4] = a4; this.a[5] = a5;
		this.a[6] = a6; this.a[7] = a7; this.a[8] = a8;
	}
	public Matrix3(){ // 単位行列で初期化
		this.a = new double[9];
		for (int i=0;i<9;i++) this.a[i] = 0.0;
		this.a[0] = this.a[4] = this.a[8] = 1.0;
	}
	public Matrix3(Matrix3 t){ 
		this(t.a);
	}	

	// 3x3行列の行列式(determinant)の計算
	public double determinant(){
		double t =
			a[0]*a[4]*a[8] +
			a[1]*a[5]*a[6] +
			a[2]*a[7]*a[3] -
			a[6]*a[4]*a[2] -
			a[5]*a[7]*a[0] -
			a[8]*a[3]*a[1];
		return t;
	}
	public static double determinant(
		double a0, double a1, double a2,
		double a3, double a4, double a5,
		double a6, double a7, double a8){
		double t =
			a0*a4*a8 +
			a1*a5*a6 +
			a2*a7*a3 -
			a6*a4*a2 -
			a5*a7*a0 -
			a8*a3*a1;
		return t;
	}

	// 3x3行列に列ベクトルをかける
	public Vector3 multiplyVector3(Vector3 v){
		Vector3 w = new Vector3();
		w.x = a[0]*v.x + a[1]*v.y + a[2]*v.z;
		w.y = a[3]*v.x + a[4]*v.y + a[5]*v.z;
		w.z = a[6]*v.x + a[7]*v.y + a[8]*v.z;
		return w;
	}
	public Vector3 multiplyVector3(Matrix3 m, Vector3 v){
		Vector3 w = new Vector3();
		w.x = m.a[0]*v.x + m.a[1]*v.y + m.a[2]*v.z;
		w.y = m.a[3]*v.x + m.a[4]*v.y + m.a[5]*v.z;
		w.z = m.a[6]*v.x + m.a[7]*v.y + m.a[8]*v.z;
		return w;
	}
	public Vertex3 multiplyVertex3(Vertex3 v){
		Vertex3 w = new Vertex3();
		w.x = a[0]*v.x + a[1]*v.y + a[2]*v.z;
		w.y = a[3]*v.x + a[4]*v.y + a[5]*v.z;
		w.z = a[6]*v.x + a[7]*v.y + a[8]*v.z;
		return w;
	}

	// 3x3行列bを左から掛ける
	public void multiply(Matrix3 b){
		Matrix3 c = new Matrix3();
		int i,j,k,p,q,r,s;
    		for ( k=0 , p=0, q=0 ; k < 3 ; k++, q += 3 ){
           		for ( j=0 ; j < 3 ; j++ ){
             		r = q;
             		s = j;
             		c.a[p] = 0;
					for ( i=0 ; i < 3 ; i++, r++, s += 3 ){
                   			c.a[p] += b.a[r] * a[s];
              		}
					p++;
         		}
    		}
		for (k = 0; k < 9 ; k++) this.a[k] = c.a[k];
	}

	// 3x3行列の逆行列を計算する
	public Matrix3 inverse() throws SingularMatrixException {
		double t = this.determinant();
		if (Math.abs(t) < EPSILON) 
			throw new SingularMatrixException();
		t = 1.0/t;
		double[] b = new double[9];
		/*
			３ｘ３行列の逆行列は、次の行列の転置行列を
			もとの行列の行列式で割ったもの（符号に注意）
			|  |a[4] a[5]|   |a[3] a[5]|   |a[3] a[4]| |
			|  |a[7] a[8]|  -|a[6] a[8]|   |a[6] a[7]| |
			|                                          |
			|  |a[1] a[2]|   |a[0] a[2]|   |a[0] a[1]| |
			| -|a[7] a[8]|   |a[6] a[8]|  -|a[6] a[7]| |
			|                                          |
			|  |a[1] a[2]|   |a[0] a[2]|   |a[0] a[1]| |
			|  |a[4] a[5]|  -|a[3] a[5]|   |a[3] a[4]| |
			内側の４つの要素からなる| |は２ｘ２行列の行列式
		*/
 	        b[0] =  (a[4]*a[8]-a[5]*a[7])*t;
        	b[1] = -(a[1]*a[8]-a[7]*a[2])*t;
        	b[2] =  (a[1]*a[5]-a[2]*a[4])*t;
        	b[3] = -(a[3]*a[8]-a[5]*a[6])*t;
        	b[4] =  (a[0]*a[8]-a[2]*a[6])*t;
        	b[5] = -(a[0]*a[5]-a[2]*a[3])*t;
        	b[6] =  (a[3]*a[7]-a[4]*a[6])*t;
        	b[7] = -(a[0]*a[7]-a[1]*a[6])*t;
        	b[8] =  (a[0]*a[4]-a[1]*a[3])*t;
		Matrix3 m = new Matrix3(b);
		return(m);
	}

	// 軸ベクトル(Vector3)vの周りに角度thetaだけ回転する３ｘ３行列
	// 回転は軸ベクトルの先端から見て右回り
	public Matrix3 rotate(Vector3 v, double theta){
		Vector3 w = new Vector3(v);
		w.normalize();
		double s = Math.sin(theta);
		double c = Math.cos(theta);
		double t = 1-c;
    		a[0] = t*w.x*w.x + c;
    		a[1] = t*w.x*w.y + w.z*s;
    		a[2] = t*w.z*w.x - w.y*s;
    		a[3] = t*w.x*w.y - w.z*s;
    		a[4] = t*w.y*w.y + c;
    		a[5] = t*w.y*w.z + w.x*s;
    		a[6] = t*w.z*w.x + w.y*s;
    		a[7] = t*w.y*w.z - w.x*s;
    		a[8] = t*w.z*w.z + c;
		return this;
	}
	// 軸ベクトル(dx,dy,dz)の周りに角度thetaだけ回転する３ｘ３行列
	public Matrix3 rotate(
		double dx, double dy, double dz, double theta){
		Vector3 v = new Vector3(dx,dy,dz);
		return rotate(v,theta);
	}
	public Matrix3 rotate(Vector4 v){
		return rotate(v.x,v.y,v.z,v.w);
	}

	// 3x3行列の印刷
	public void print(){
		System.out.println("Matrix3クラスの要素");
		System.out.println("|"+a[0]+" "+a[1]+" "+a[2]+"|");
		System.out.println("|"+a[3]+" "+a[4]+" "+a[5]+"|");
		System.out.println("|"+a[6]+" "+a[7]+" "+a[8]+"|");
	}
}
