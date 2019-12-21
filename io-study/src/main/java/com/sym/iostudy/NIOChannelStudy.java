package com.sym.iostudy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannelStudy {
    public static void main(String[] args) {
        copy();

    }

    public static void write(){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D://hello2.txt");
            // 从FileOutPutStream获取FileChannelImpl
            FileChannel channel = outputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String s = "hello,World";
            // 将byte数组放入缓冲区，buffer的position等于数组长度
            buffer.put(s.getBytes());
            // 读写翻转，limit=position，而position置0
            buffer.flip();
            //写入，将buffer读取到channel
            channel.write(buffer);
            outputStream.close();
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("D://hello.txt");
            // FileInputStream获取FileChannel，实际类型是FileChannelImpl
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            System.out.println(read);
            String s = new String(buffer.array(), 0, read,"utf-8");
            System.out.println(s);
            inputStream.close();
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(){
        try {
            FileInputStream fileInputStream = new FileInputStream("D://hello.txt");
            FileOutputStream fileOutputStream = new FileOutputStream("D://helloCopy2.txt");
            //获取通道
            FileChannel fileChannel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();

            //用一个2字节大小的buffer去读
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);

            while(true){
                //重置buffer,为例重新读写数据
                byteBuffer.clear();
                //将文件内容读出写入到buffer中，返回读取的字节的大小
                int read = fileChannel.read(byteBuffer);
                //判读是否读取结束
                if(read == -1){
                    break;
                }else{
                    //每读两个字节，就先写两个字节
                    //将读取到的内容直接输出
                    String s = new String(byteBuffer.array(), 0, read,"utf-8");
                    System.out.println(s);
                    //将buffer读到新的channel里面，主要要先进行翻转
                    byteBuffer.flip();
                    outChannel.write(byteBuffer);
                }
            }
            //关闭流
            fileInputStream.close();
            fileOutputStream.close();
            fileChannel.close();
            outChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
