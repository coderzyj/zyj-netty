package com.saitama;

import java.nio.IntBuffer;

/**
 * @author: zyj
 * @time: 2020/11/28  20:10
 * @description:
 */
public class BasicBuffer {
    public static void main(String[] args) {



        //创建一个buffer，大小为5，即可存放5个int

        IntBuffer intBuffer = IntBuffer.allocate(5);

        for(int i = 0;i < intBuffer.capacity();i++){
            intBuffer.put(i*2);

        }

        //如何匆匆buffer读取数据
        //将buffer转换，读写切换
        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
