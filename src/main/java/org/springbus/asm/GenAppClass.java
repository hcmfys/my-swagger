package org.springbus.asm;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class GenAppClass {


    static void writeAddMethod(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "add", "(II)I", null, null);
        mv.visitCode();
        Label l1 = new Label();
        mv.visitLabel(l1);

        mv.visitIntInsn(ILOAD, 1);
        mv.visitIntInsn(ILOAD, 2);
        mv.visitInsn(IADD);
        mv.visitInsn(IRETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", "Lorg/springbus/asm/GenClass;", null, l1, l2, 0);
        mv.visitLocalVariable("a", "I", null, l1, l2, 1);
        mv.visitLocalVariable("b", "I", null, l1, l2, 2);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    static void writeInit(ClassWriter cw) {
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(3, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lorg/springbus/asm/GenClass;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
    }

    //main(String[] args)
    static void writeMain(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitParameter("args", 0);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(11, l0);
        mv.visitTypeInsn(NEW, "org/springbus/asm/GenClass");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/springbus/asm/GenClass", "<init>", "()V", false);
        mv.visitInsn(ICONST_4);
        mv.visitIntInsn(BIPUSH, 6);
        mv.visitMethodInsn(INVOKESPECIAL, "org/springbus/asm/GenClass", "add", "(II)I", false);
        mv.visitVarInsn(ISTORE, 1);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(12, l1);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(13, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l3, 0);
        mv.visitLocalVariable("t", "I", null, l1, l3, 1);
        mv.visitMaxs(3, 2);
        mv.visitEnd();
    }


    static void writeClass() {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_6, ACC_PUBLIC, "org/springbus/asm/GenClass", null, "java/lang/Object", null);
        writeInit(cw);
        writeAddMethod(cw);
        writeMain(cw);
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        try {
            FileCopyUtils.copy(b, new File("E:\\123\\org\\springbus\\asm\\GenClass.class"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        writeClass();
    }
}
