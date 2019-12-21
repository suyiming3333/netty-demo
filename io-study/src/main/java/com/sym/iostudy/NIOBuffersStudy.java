package com.sym.iostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NIOBuffersStudy {

    public static void main(String[] args) throws IOException {
        /**
         * Scatting：将数据写入到buffer时，可以使用buffer数组，依次写入
         * Gathering：从buffer读取数据时，可以采用buffer数组，依次读取
         */
        //打开一个服务器连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(socketAddress);

        // 创建buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(8);
        int messageLength = 10;
        // 等待连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength){
                //用buffers数组去读取客户端传送的数据
                long l = socketChannel.read(buffers);
                byteRead += l;
                System.out.println("byteRead = "+byteRead);
                //将每个数组的相关信息输出
                Arrays.asList(buffers).stream().map(buffer -> "position = "+buffer.position() +",limit = "+buffer.limit()).forEach(System.out::println);
            }
            //对每个数组进行翻转，准备读buffers
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.flip());
            int byteWrite = 0;
//            while (byteWrite < messageLength){
//                //将内容返回给客户端
//                long l = socketChannel.write(buffers);
//                byteWrite += l;
//            }
            //清空数组，用于下一次的接受数据
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead = "+byteRead + ",byteWrite = "+byteWrite +",messageLength = "+messageLength);
    }
}
}
