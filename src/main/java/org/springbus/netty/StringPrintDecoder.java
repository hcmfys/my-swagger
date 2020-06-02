package org.springbus.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class StringPrintDecoder  extends MessageToMessageDecoder<String> {




    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        System.out.println("==StirngPrintEncoder="+msg.replace("\r",
                "\\r").replace("\n", "\\n").replace(" ", "\\e"));
    }
}
