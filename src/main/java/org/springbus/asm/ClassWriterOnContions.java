package org.springbus.asm;

import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class ClassWriterOnContions {

    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader("org.springbus.asm.GenClass");
        List<ClassReader> ts=new ArrayList<>();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new AddFieldAdapter(cw, ACC_PUBLIC, "userName", "String"), 0);
        byte[] b = cw.toByteArray();

        try {
            FileCopyUtils.copy(b, new File("e:/123/tt.class"));
            String name = Type.getType(ts.getClass()).getDescriptor();
            System.out.println(name);
            name = Type.getType(ts.getClass()).getInternalName();
            System.out.println(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
