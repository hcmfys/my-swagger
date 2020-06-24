package org.springbus.asm;

import org.objectweb.asm.*;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


import static org.objectweb.asm.Opcodes.*;

public class NewClass {



    static  void genMain(ClassWriter cw) {

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitParameter("args", 0);
        mv.visitCode();
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitTypeInsn(Opcodes.NEW, "org/springbus/asm/Hello");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/springbus/asm/Hello", "<init>", "()V", false);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/springbus/asm/Hello", "doIt", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l1, l2, 0);
        mv.visitLocalVariable("t", "Lorg/springbus/asm/Hello;", null, l1, l2, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    static  void  genPrivateFiledNme(ClassWriter cw,String fieldName,String fileValue ,String  desc,Object value) {

        FieldVisitor fieldVisitor= cw.visitField(ACC_PRIVATE, fieldName, desc, null, value);
        AnnotationVisitor av=fieldVisitor.visitAnnotation(Type.getDescriptor(org.springframework.beans.factory.annotation.Value.class), true);
        av.visitAnnotation("value", fileValue);
        av.visitEnd();
        fieldVisitor.visitEnd();
    }

    static  void  genInit(ClassWriter cw) {

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(3, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "Lorg/springbus/asm/Hello;", null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

    }

    static  void genDoItMethod(ClassWriter cw) {

        MethodVisitor mv= cw.visitMethod(ACC_PUBLIC , "doIt", "()V", null, null);
        mv.visitCode();
        Label l1 = new Label();
        mv.visitLabel(l1);


        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System","out","Ljava/io/PrintStream;");
        mv.visitLdcInsn("hello asm");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"
        , false);
        mv.visitInsn(Opcodes.RETURN);

        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", "Lorg/springbus/asm/Hello;", null, l1, l2, 0);
        //mv.visitLocalVariable("a", "I", null, l1, l2, 1);
        mv.visitEnd();
        mv.visitMaxs(1, 1);

    }


    static void genHelloClass(String path) {


        ClassWriter cw = new ClassWriter(null, ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_5, ACC_PUBLIC, "org/springbus/asm/Hello", null,
                "java/lang/Object", null);
        genInit(cw);
        genMain(cw);
        genDoItMethod(cw);
        genPrivateFiledNme(cw,"age", "年龄","I", 12);
        genPrivateFiledNme(cw,"name", "姓名","Ljava/lang/String;", "asm");
        genPrivateFiledNme(cw,"usrList", "用户列表",Type.getDescriptor(HashMap.class), null);
        byte[] b = cw.toByteArray();
        try {
            FileCopyUtils.copy(b, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) throws IOException {
        genHelloClass("E:\\123\\org\\springbus\\asm\\Hello.class");

    }
}
