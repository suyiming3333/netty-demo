package com.sym.netty.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.TimeUnit;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel inComingChannel = ctx.channel();
        System.out.println("客户端有数据进入");
//        System.out.println("正在处理业务");
//        Thread.sleep(5000);//这里出现阻塞，不太好
//        System.out.println("业务处理完成，准备发送给客户端");

        //1.采用用户程序自定义的普通任务处理
        ctx.channel().eventLoop().execute(()->{
            System.out.println("正在处理业务");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("业务处理完成，准备发送给客户端");
        });
        //2.用户自定义定时任务处理(延时处理)
        ctx.channel().eventLoop().schedule(()->{
            System.out.println("正在处理业务");
            try {
                Thread.sleep(5000);//这里出现阻塞，不太好
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("业务处理完成，准备发送给客户端");
        },5, TimeUnit.SECONDS);
        //3.非当前reactor线程调用channel的方法

        inComingChannel.writeAndFlush("message from server\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        inComingChannel.writeAndFlush("服务器读完了\n");
        System.out.println("业务处理完成，准备发送给客户端");
    }
}
