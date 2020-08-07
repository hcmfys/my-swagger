package org.springbus.comutergraphics.CG.common;

// DivideByZeroException.java
//零除异常处理类

public class DivideByZeroException extends ArithmeticException {

	// 构造函数
	public DivideByZeroException(){
		super("零除法发生");
	}
	public DivideByZeroException(String s){
		super(s);
	}
}
