package com.saitama;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zyj
 * @time: 2021/1/5  20:36
 * @description:  MappedByteBuffer 可让文件直接在内存（堆外内存）修改，操作系统不需要再拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("d://01.txt", "rw");
        //获取对应的通道
        FileChannel channel = rw.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用过的读写模式
         * 参数2：0：起始位置 ：可以直接修改的起始位置
         * 参数3：5： 映射到内存的大小
         * 可以修改的范围就是0-5
         */
       MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
       mappedByteBuffer.put(0,(byte)'T');
       mappedByteBuffer.put(4,(byte)'T');
       rw.close();
    }
}
