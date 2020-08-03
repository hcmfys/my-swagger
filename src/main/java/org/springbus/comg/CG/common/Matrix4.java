package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Matrix4クラス
// 4x4の行列のクラス
// ここで，アフィン変換を記述する

public class Matrix4 extends MyObject {

	public double a[]; // ここに4x4の行列の要素を入れる
	/*
		| a[0]  a[1]  a[2]  a[3] |
		| a[4]  a[5]  a[6]  a[7] |
		| a[8]  a[9]  a[10] a[11]|
		| a[12] a[13] a[14] a[15]|
	*/

	// コンストラクタ
	public Matrix4(double b[]){
		this.a = new double[16];
		for (int i=0;i<16;i++) this.a[i] = b[i];
	}
	public Matrix4(){ // 単位行列で初期化
		this.a = new double[16];
		for (int i=0;i<16;i++) this.a[i] = 0.0;
		this.a[0] = this.a[5] = this.a[10] = this.a[15] = 1.0;
	}
	public Matrix4(Matrix4 t){ 
		this(t.a);
	}

	//　単位行列に設定
	public Matrix4 identity(){
		if (this == null)
			throw new NullPointerException();
		for (int i=0;i<16;i++) this.a[i] = 0.0;
		this.a[0] = this.a[5] = this.a[10] = this.a[15] = 1.0;
		return this;
	}

	// 行列の代入
	public void assign(Matrix4 v){
		for (int i=0 ; i < 16 ; i++ ) a[i] = v.a[i];
	}

	// 4x4行列と4次元ベクトルとの積
	public Vector4 multiplyVector4(Vector4 v){
		Vector4 t = new Vector4();
		t.x = a[0]*v.x + a[1]*v.y + a[2]*v.z + a[3]*v.w;
		t.y = a[4]*v.x + a[5]*v.y + a[6]*v.z + a[7]*v.w;
		t.z = a[8]*v.x + a[9]*v.y + a[10]*v.z + a[11]*v.w;
		t.w = a[12]*v.x + a[13]*v.y + a[14]*v.z + a[15]*v.w;
		return t;
	}

	// 4x4行列と4次元座標値との積
	public Vertex4 multiplyVertex4(Vertex4 v){
		Vertex4 t = new Vertex4();
		t.x = a[0]*v.x + a[1]*v.y + a[2]*v.z + a[3]*v.w;
		t.y = a[4]*v.x + a[5]*v.y + a[6]*v.z + a[7]*v.w;
		t.z = a[8]*v.x + a[9]*v.y + a[10]*v.z + a[11]*v.w;
		t.w = a[12]*v.x + a[13]*v.y + a[14]*v.z + a[15]*v.w;
		return t;
	}

	// ２つの4x4行列の積（今の行列に左から掛ける）
	public Matrix4 multiplyMatrix4(Matrix4 b){
		Matrix4 c = new Matrix4();
		int i,j,k,m,n,p,r,s;
   		for ( i = r = 0 ; i < 4 ; i ++, r += 4 ) {
   			for ( j = 0 ; j < 4 ; j ++ ) {
   				m = r + j;
   				c.a[m] = 0.0;
   				for ( k = s = 0 ; k < 4 ; k++, s += 4 ){
					n = r + k;
					p = s + j;
               		c.a[m] += b.a[n] * a[p];
       			}
       		}
   		}
		return c;
	}
	public void multiply(Matrix4 b){
		Matrix4 c = new Matrix4();
		int i,j,k,m,n,p,r,s;
   		for ( i = r = 0 ; i < 4 ; i ++, r += 4 ) {
   			for ( j = 0 ; j < 4 ; j ++ ) {
   				m = r + j;
   				c.a[m] = 0.0;
   				for ( k = s = 0 ; k < 4 ; k++, s += 4 ){
					n = r + k;
					p = s + j;
               		c.a[m] += b.a[n] * a[p];
       			}
       		}
   		}
		for ( k = 0 ; k < 16 ; k++ ) this.a[k] = c.a[k];
	}

	// 4x4行列の転置行列（行と列を入れ替えた行列）
	// 転置行列用のインデックス
	final static int[] TR = { 0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15 };
	public Matrix4 transpose(){
		Matrix4 m = new Matrix4();
		for (int i = 0 ; i < 16 ; i++ ){
			m.a[i] = a[TR[i]];
		}
		return m;
	}

	// 4x4行列の逆行列
	// 3x3行列の逆行列問題に変換し，解析的に解く。

	// 余因子行列用のインデックス
	final static int[][] AA = {
 		/* aa00 */ {5,6,7,9,10,11,13,14,15},
		/* aa10 */ {1,2,3,9,10,11,13,14,15},
		/* aa20 */ {1,2,3,5,6,7,13,14,15},
		/* aa30 */ {1,2,3,5,6,7,9,10,11},
  		/* aa01 */ {4,6,7,8,10,11,12,14,15},
 		/* aa11 */ {0,2,3,8,10,11,12,14,15},
		/* aa21 */ {0,2,3,4,6,7,12,14,15},
		/* aa31 */ {0,2,3,4,6,7,8,10,11},
 		/* aa02 */ {4,5,7,8,9,11,12,13,15},
 		/* aa12 */ {0,1,3,8,9,11,12,13,15},
		/* aa22 */ {0,1,3,4,5,7,12,13,15},
		/* aa32 */ {0,1,3,4,5,7,8,9,11},
 		/* aa03 */ {4,5,6,8,9,10,12,13,14},
 		/* aa13 */ {0,1,2,8,9,10,12,13,14},
 		/* aa23 */ {0,1,2,4,5,6,12,13,14},
 		/* aa33 */ {0,1,2,4,5,6,8,9,10}
	};
	public Matrix4 inverse() throws SingularMatrixException {
		Matrix4 c = new Matrix4();
		double determinant4, determinant3;
		double[] v = new double[9];
		int m,n,sign;
    		for (int i=0 ; i < 16 ; i++ ){
         		m = (i+1)/4 + 1; n = (i+1)%4;
			if (n==0) { n = 4; m--; }
         		if ((m+n)%2 == 1) sign = -1; else sign = 1;
         		for (int j=0 ; j < 9 ; j++ ) v[j] = a[AA[i][j]];
			Matrix3 w = new Matrix3(v);
         		determinant3 = w.determinant();
         		c.a[i] = sign * determinant3;
    		}
    		determinant4 = 
			  a[0]*c.a[0]
	  		+ a[4]*c.a[1]
	  		+ a[8]*c.a[2]
	  		+ a[12]*c.a[3];
		if (Math.abs(determinant4) < EPSILON) 
			throw new SingularMatrixException();
		determinant4 = 1.0 / determinant4;
    		for (int i=0 ; i < 16 ; i++ ) c.a[i] *= determinant4;
		return c;
	}

	// 任意軸の周りの回転（３ｘ３行列の拡張）
	public Matrix4 rotate(Vector3 v, double theta){
		Matrix3 m3a = new Matrix3();
		Matrix3 m3b = m3a.rotate(v,theta);
		Matrix4 m = new Matrix4();
		m.a[0] = m3b.a[0];
		m.a[1] = m3b.a[1];
		m.a[2] = m3b.a[2];
		m.a[4] = m3b.a[3];
		m.a[5] = m3b.a[4];
		m.a[6] = m3b.a[5];
		m.a[8] = m3b.a[6];
		m.a[9] = m3b.a[7];
		m.a[10] = m3b.a[8];
		return m;
	}

	//4x4行列の印刷
	public void printMatrix(){
		System.out.println("|"+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+"|");
		System.out.println("|"+a[4]+" "+a[5]+" "+a[6]+" "+a[7]+"|");
		System.out.println("|"+a[8]+" "+a[9]+" "+a[10]+" "+a[11]+"|");
		System.out.println("|"+a[12]+" "+a[13]+" "+a[14]+" "+a[15]+"|");	}
	public void print(){
		System.out.println("Matrix4クラスの要素");
		printMatrix();
	}
	public void print(String s){
		System.out.println(s);
		printMatrix();
	}
}
