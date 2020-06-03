package org.springbus.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class TestEchoServer {

    public   static  void main(String[] args ) throws InterruptedException {
        ServerBootstrap b=new ServerBootstrap();
        EventLoopGroup fatherGroup=new NioEventLoopGroup(2);
        EventLoopGroup childGroup=new NioEventLoopGroup(6);
        b.group(fatherGroup,childGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast("StringMessageDecoder", new StringMessageDecoder());
                // ch.pipeline().addLast(new StringMessgeEncoder());
                ch.pipeline().addLast("StringPrintDecoder", new StringPrintDecoder());
            }
        });
        ChannelFuture channelFuture=b.bind(8080);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                System.out.println("success to bind server");
            }
        });
        ChannelFuture c=channelFuture.sync();



    }
}
