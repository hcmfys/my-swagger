package org.springbus.asm;

import jdk.nashorn.internal.codegen.types.Type;
import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.asm.Opcodes.ASM6;

public class ReadClassFileApp {


    static  class PrintVistor extends ClassVisitor {

        private  ClassVisitor cv;

        public PrintVistor(int api) {
            super(api);
        }


        public PrintVistor(int api,ClassVisitor cv) {
            super(api,cv );
        }

        @Override
        public void visitSource(String source, String debug) {
            System.out.println(source);
            super.visitSource(source, debug);

        }


        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            System.out.println(name);
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            System.out.println(name + " -> " + descriptor);
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return new PrintMethodVistor(ASM6, mv) ;

        }
    }

    public static void main(String[] args) {
        try {
            ClassReader reader = new ClassReader(Type.getInternalName(String.class));
          //  ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            reader.accept(new PrintVistor(ASM6), 0);
            reader.accept(new TraceClassVisitor( null,new PrintWriter(System.out)), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
