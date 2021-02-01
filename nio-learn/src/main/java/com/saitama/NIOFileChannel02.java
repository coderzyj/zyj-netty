package com.saitama;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zyj
 * @time: 2020/12/21  23:08
 * @description:
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d://01.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();

        System.out.println(new String(buffer.array()));
    }
}
