package org.springbus.cglib;


import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class UserAction {

    public UserAction() {
    }

    public UserAction(String action) {
    }

    public boolean add(String string, int i) {
        System.out.println("This is add method: " + string + ", " + i);
        return true;
    }

    public void update() {
        System.out.println("This is update method");
    }


    /**
     * 使用反射类
     * @throws Exception
     */
    static  void userProxy() throws Exception {
        Class delegateClass = UserAction.class;

        // Java Reflect
        // 反射构造类
        Constructor delegateConstructor = delegateClass.getConstructor(String.class);
        // 创建委托类实例
        UserAction delegateInstance = (UserAction) delegateConstructor.newInstance("Tom");

        // 反射方法类
        Method addMethod = delegateClass.getMethod("add", String.class, int.class);
        // 调用方法
        addMethod.invoke(delegateInstance, "Tom", 30);

        Method updateMethod = delegateClass.getMethod("update");
        updateMethod.invoke(delegateInstance);
    }

    static  void  useFastClass() throws InvocationTargetException {
        FastClass fastClass=  FastClass.create(UserAction.class);
        UserAction  userAction=(UserAction)fastClass.newInstance(new Class[]{String.class}, new Object[]{"jask"});
        userAction.add("hsck", 25);
        fastClass.invoke("add", new Class[]{ String.class,int.class}, userAction,
        new Object[]{"fast class invoke jslx",26});
    }

    static  void useFastClassInvoke() throws  Exception {
        // CGLib FastClass

        // FastClass动态子类实例
        FastClass fastClass = FastClass.create(UserAction.class);

        // 创建委托类实例
        UserAction fastInstance = (UserAction) fastClass.newInstance(
                new Class[] {String.class}, new Object[]{"Jack"});

        // 调用委托类方法
        fastClass.invoke("add", new Class[]{ String.class, int.class}, fastInstance,
                new Object[]{ "Jack", 25});

        fastClass.invoke("update", new Class[]{}, fastInstance, new Object[]{});
    }

    public static void main(String[] args) throws Exception {
        // 保留生成的FastClass类文件
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\Temp\\CGLib\\FastClass");
        useFastClass();
    }
}
