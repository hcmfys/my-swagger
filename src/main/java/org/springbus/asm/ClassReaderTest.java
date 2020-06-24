package org.springbus.asm;


import org.jooq.meta.derby.sys.Sys;
import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

import static org.objectweb.asm.Opcodes.ASM5;

public class ClassReaderTest {

    static class ClassPrinter extends ClassVisitor {

        public ClassPrinter(int api) {
            super(api);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            System.out.println(name  +"    ---descriptor  =" +descriptor  +"  signature->"+signature);
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            System.out.println( "visitField -->"+ name  +"    ---descriptor  =" +descriptor  +"  signature->"+signature);

            return super.visitField(access, name, descriptor, signature, value);
        }
    }

    public static void main(String[] args) throws IOException {
        ClassReader reader=new  ClassReader("org.springbus.asm.GenClass");
        reader.accept(new ClassPrinter(ASM5), 0);

        reader.accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);

        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visitEnd();
        reader.accept(cw, 0);
        byte[] b=cw.toByteArray();


    }
}
