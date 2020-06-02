package org.springbus.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class StringMessgeEncoder  extends MessageToByteEncoder<String > {


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getBytes());
    }
}
