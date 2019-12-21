package com.sym.iostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOSelectorClientStudy {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1",9000));
        if (!connect){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做一些其他工作");
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap("This is a message!".getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
