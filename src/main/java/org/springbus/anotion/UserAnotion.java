package org.springbus.anotion;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class UserAnotion {

    @MyAnnation(url = "http://127.0.0.1",value = "/request/v3")
    public void userAction(){

    }





    static  void use() throws  Exception {

        Class c=UserAnotion.class;
        Method method=c.getDeclaredMethod("userAction", new Class[]{});
        Annotation[] annotation= method.getDeclaredAnnotations();

        for( Annotation a :annotation) {
            System.out.println(a +"-->"+a.annotationType());
            Class   parentType = a.annotationType();
            if(! parentType.getName().startsWith("java.lang.annotation") ) {
                Annotation[] annotations = parentType.getDeclaredAnnotations();
                for (Annotation annotation1 : annotations) {
                    if (!annotation1.toString().startsWith("@java.lang.annotation")) {
                        System.out.println(" child an= " + annotation1.toString());
                    }
                }
            }
        }

        Annotation  annotations= AnnotationUtils.getAnnotation(method,UrlRequestBody.class);

            System.out.println( annotations);

    }

    public  static   void main(String[] args) throws  Exception {
        use();

    }
}
