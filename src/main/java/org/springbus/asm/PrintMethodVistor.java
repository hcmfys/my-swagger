package org.springbus.asm;

import jdk.internal.org.objectweb.asm.util.Printer;
import org.objectweb.asm.Handle;
import  org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PrintMethodVistor extends MethodVisitor {

    public PrintMethodVistor(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.println(""+ Printer.OPCODES[opcode] );
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.println(""+Printer.OPCODES[opcode]  +" " +operand );
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        System.out.println(""+Printer.OPCODES[opcode]  +" " +var  );
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        System.out.println("visitInvokeDynamicInsn="+name  +" " +descriptor  );
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }
}
