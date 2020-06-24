package org.springbus.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

public class GenAddTimerClass {


    static void genEmptyClass(String toPath) throws IOException {

        ClassReader cr = new ClassReader(Type.getInternalName(EmptyClass.class));
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new AddTimerAdapter(cw), 0);

        byte[] b = cw.toByteArray();
        FileCopyUtils.copy(b, new File(toPath));
    }

    public static void main(String[] args) throws IOException {
        genEmptyClass("E:\\123\\org\\springbus\\asm\\EmptyClass.class");

    }
}
