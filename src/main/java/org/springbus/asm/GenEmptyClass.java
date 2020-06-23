package org.springbus.asm;




import org.objectweb.asm.*;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;


public class GenEmptyClass {

    static class ModifyMethodAdapter extends MethodVisitor {

        public ModifyMethodAdapter(MethodVisitor mv ) {
            super(ASM5, mv);
        }
        @Override
        public void visitInsn(int opcode) {
            if (opcode != NOP) {
                //mv.visitInsn(opcode);
            }
        }

        @Override
        public void visitLineNumber(int line, Label start) {
           // mv.visitLineNumber(line, start);
        }

        @Override
        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
            //mv.visitLocalVariable(name, descriptor, signature, start, end, index);
        }

        @Override
        public void visitCode() {

            mv.visitCode();
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(1,l1);
            mv.visitIntInsn(SIPUSH, 1200);
            mv.visitIntInsn(ILOAD, 1);
            mv.visitInsn(IADD);
            mv.visitInsn(IRETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("a", "I", null, l1, l2, 1);

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }


    /**
     * 移除方法
     */

    static class RemoveMethodAdapter extends ClassVisitor {
        private String mName;
        private String mDesc;

        public RemoveMethodAdapter(ClassVisitor cv, String mName, String mDesc) {
            super(ASM5, cv);
            this.mName = mName;
            this.mDesc = mDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {
            if (name.equals(mName)) {
                // 不要委托至下一个访问器 -> 这样将移除该方法
                return null;
            }
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }

    private static boolean exitMethod;


    static class MyClassVisitor extends ClassVisitor {

        private ClassVisitor cv;
        private String method;

        public MyClassVisitor(int api, ClassVisitor cv, String method) {
            super(api, cv);
            this.cv = cv;
            this.method = method;
        }


        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals(method)) {
                exitMethod = true;
                mv = new ModifyMethodAdapter(mv);
            }
            return mv;
        }

    }


    static void genEmptyClass(String toPath) throws IOException {

        ClassReader cr = new ClassReader(Type.getInternalName(EmptyClass.class));
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new MyClassVisitor(ASM5, cw,"doIt"), 0);
       // cr.accept(new RemoveMethodAdapter(cw, "doIt", null), 0);
        byte[] b = cw.toByteArray();
        FileCopyUtils.copy(b, new File(toPath));

    }

    public static void main(String[] args) throws IOException {
        genEmptyClass("E:\\123\\org\\springbus\\asm\\EmptyClass.class");

    }
}
