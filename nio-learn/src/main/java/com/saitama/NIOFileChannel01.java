package com.saitama;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zyj
 * @time: 2020/12/21  22:18
 * @description:
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = " hello,zyj";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d://01.txt");

        //通过输出流获取对应的FileChannel
        FileChannel channel = fileOutputStream.getChannel();

        //创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }
}
