package com.sym.iostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOSelectorServerStudy {
    public static void main(String[] args) throws IOException {
        //建立一个服务器连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建一个selector
        Selector selector = Selector.open();

        //绑定服务地址
        serverSocketChannel.bind(new InetSocketAddress(9000));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //将服务连接注册到selector，并设置selector关心的动作
        //这里设置为监听一个连接动作
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            //监听阻塞5秒，如果没有动作，则返回
            if(selector.select(5000)==0){
                System.out.println("监听了五秒都没有连接进来");
                continue;//继续监听
            }

            Set<SelectionKey> selectionKeySet = selector.selectedKeys();//获取selectedKeys
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            //如果监听到了事件
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //如果是可以连接的，(第一次为连接)
                if(selectionKey.isAcceptable()){
                    //等待一个连接建立
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //将channel注册到selector,关注的事件为读操作,第三个参数为客户端传来的数据
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //建立连接后，变为读
                if(selectionKey.isReadable()){
                    //获取channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    int read = socketChannel.read(buffer);
                    System.out.println("客户端 : "+new String(buffer.array(),0,read));

                }

                //处理动作后删除
                iterator.remove();
            }
        }


    }
}
