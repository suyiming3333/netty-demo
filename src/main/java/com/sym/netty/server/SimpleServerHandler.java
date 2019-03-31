package com.sym.netty.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

//事件监听处理类
public class SimpleServerHandler extends SimpleChannelInboundHandler<String>{

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    //当服务端有新的客户端接入时
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        //通知所有频道有新连接接入
        for(Channel channel:channels){
            channel.writeAndFlush("[Server]"+inComingChannel.remoteAddress()+"加入\n");
        }
        //添加到channelgroup
        channels.add(inComingChannel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        //通知所有频道有连接断开
        for(Channel channel:channels){
            channel.writeAndFlush("[Server]"+inComingChannel.remoteAddress()+"离开\n");
        }
        //到删除
        channels.remove(inComingChannel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"在线\n");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"掉线\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel inComingChannel = ctx.channel();
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"异常\n");
        cause.printStackTrace();
        inComingChannel.close();
    }

    //当服务端读取客户端发送过来的消息时
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        //获取当前发送消息的通道
        Channel inComingChannel = channelHandlerContext.channel();
        //向其他连接的通道进行消息转发
        for(Channel channel:channels){
            if(channel!=inComingChannel){
                channel.writeAndFlush("["+inComingChannel.remoteAddress()+"]"+s+"\n");
            }else{
                channel.writeAndFlush("[you]"+s+"\n");
            }
        }
    }
}
