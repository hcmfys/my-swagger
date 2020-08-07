package org.springbus.comutergraphics.CG.common;
// SingularMatrixException.java
//非常规矩阵的异常处理类

public class SingularMatrixException extends ArithmeticException {


	public SingularMatrixException(){
		super("非正规矩阵");
	}
	public SingularMatrixException(String s){
		super(s);
	}
}
