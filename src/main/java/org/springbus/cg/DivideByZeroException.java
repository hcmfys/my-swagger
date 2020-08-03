package org.springbus.cg;


// DivideByZeroException.java
// 零除异常处理类

public class DivideByZeroException extends ArithmeticException {

	// コンストラクタ
	public DivideByZeroException(){
		super("零除法发生");
	}
	public DivideByZeroException(String s){
		super(s);
	}
}
