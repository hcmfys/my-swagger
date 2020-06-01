package org.springbus.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;


public class ByteBufferTest {


    public static  void test1() {
        ByteBuf buf = Unpooled.buffer(30);
        String h = "hello";
        System.out.println(buf.readerIndex() + "---" + buf.writerIndex());
        buf.writeBytes(h.getBytes());
        System.out.println("after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );

        System.out.println(buf.getByte(1));

        System.out.println(" get after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );
        buf.readerIndex(1);
        System.out.println(" get after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );
        buf.readerIndex(3);
        System.out.println(" get after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );
        buf.resetReaderIndex();
        System.out.println(" get after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );

        buf.readerIndex(2);
        byte[] bytes = buf.array();
        System.out.println(" get after readerIndex =" +
                buf.readerIndex() + "-writerIndex=--" + buf.writerIndex() + " arrayOffset=" + buf.arrayOffset()
                + " capacity=" + buf.capacity() + " -- maxCapacity=" + buf.maxCapacity()
        );
        System.out.println(" array=" + new String(bytes));
        String hx = ByteBufUtil.prettyHexDump(buf);
        System.out.println(hx);


    }

    public static  void test2(){

    }




    public  static  void  main(String[] args) {
        test1();
    }
}
