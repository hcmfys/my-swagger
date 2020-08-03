package org.springbus.cg;


// SingularMatrixException.java
// 正則でない行列の例外処理クラス

public class SingularMatrixException extends ArithmeticException {

	// コンストラクタ
	public SingularMatrixException(){
		super("正則でない行列です");
	}
	public SingularMatrixException(String s){
		super(s);
	}
}
