package com.sym.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

//增加多个的处理类到 ChannelPipeline
public class SimpleClientInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

//        ByteBuf buf = Unpooled.copiedBuffer("_corn".getBytes());
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, buf));
        //拆包发送
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new SimpleClientHandler());

        System.out.println("SimpleChatClient:"+socketChannel.remoteAddress() +"连接上");
    }
}
