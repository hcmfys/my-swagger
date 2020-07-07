package org.springbus.asm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class MyInvokerProxy  implements InvocationHandler {
     private Object obj;
    public MyInvokerProxy(Object obj){
        this.obj=obj;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(obj,args);
    }
}
