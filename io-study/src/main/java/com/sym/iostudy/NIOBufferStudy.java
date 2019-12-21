package com.sym.iostudy;

import java.nio.IntBuffer;

public class NIOBufferStudy {

    public static void main(String[] args) {
        //创建一个数组大小为5的buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //随机 放入5个数值
        for(int i=0;i<intBuffer.capacity();i++){
            intBuffer.put(i<<10);
        }


        //读写切换，这时为写切换为读
        intBuffer.flip();
        //限制只能获取下标为前三的数组值
        intBuffer.limit(3);
        System.out.println(intBuffer.capacity());
        while (intBuffer.hasRemaining()){
            // 获取position位置的元素
            System.out.println(intBuffer.get());
        }
    }


}
