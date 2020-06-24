package org.springbus.asm;


import org.objectweb.asm.MethodVisitor;

import static aj.org.objectweb.asm.Opcodes.*;
import static org.springframework.asm.Opcodes.ASM4;

public class AddTimerMethodAdapter extends MethodVisitor {
    private    String owner;
    public AddTimerMethodAdapter(MethodVisitor mv,  String owner ) {
        super(ASM4, mv);
    }

    @Override
    public void visitCode() {
        mv.visitCode();

        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                "currentTimeMillis", "()J");
        mv.visitInsn(LSUB);
        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J");
            mv.visitInsn(LADD);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 4, maxLocals);
    }

}

