package org.springbus.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class StringMessageDecoder extends ByteToMessageDecoder {



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int size = in.readableBytes();
        if (size >= 6) {
            ByteBuf bb = in.readBytes(6);
            byte[] bytes = new byte[6];
            bb.getBytes(0, bytes);
            String data = new String(bytes);
            out.add(data);
           // System.out.println("===> " + data +"   --hex={"+ ByteBufUtil.hexDump(bytes)+"}");

        }

    }
}
