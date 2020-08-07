package org.springbus.comutergraphics.CG.common;

// Index3类
//包含三角形索引的类
//程序3-1
// Index3构造函数

public class Index3 extends MyObject {

	public int v1, v2, v3;//三角形顶点索引

	// 构造函数
	public Index3(int v1, int v2, int v3){
		if (v1 < 0 || v2 < 0 || v3 < 0)
			throw new InternalError("索引错误");
		this.v1 = v1; this.v2 = v2; this.v3 = v3;
	}
	public Index3(Index3 v){ this(v.v1,v.v2,v.v3); }

	// 指标范围检查
	public boolean isOutOfBounds(int min, int max){
		if (v1 < min || v1 > max) return true;
		if (v2 < min || v2 > max) return true;
		if (v3 < min || v3 > max) return true;
		return false;
	}

	// 打印索引
	public void print(){
		System.out.println("Index3 = ("+v1+","+v2+","+v3+")");
	}
	public void print(String s){
		System.out.println(s);
		System.out.println("  (v1,v2,v3)=("+v1+","+v2+","+v3+")");
	}

}
