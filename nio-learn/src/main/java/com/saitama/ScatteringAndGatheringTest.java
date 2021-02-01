package com.saitama;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author: zyj
 * @time: 2021/1/5  20:49
 * @description:
 *
 * Scattering: 将数据写入到buffer时，可以采用buffer数组中，依次写入【分散】
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读取【聚集】
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {

        //使用ServerSocketChannel 和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] =  ByteBuffer.allocate(3);

        //等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;
        //循环读取
        while (true){
            int byteRead = 0;

            while (byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead"+byteRead);
                //使用流打印，看看当前这个buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position="+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
            }
            //将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());

            //将数据读出，显示到客户端
            long byteWrite = 0;
            while(byteWrite < messageLength){
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;;
            }

            //将所有buffer复位
            Arrays.asList(byteBuffers).forEach(buffer->buffer.clear());

            System.out.println("byteRead="+byteRead+",byteWrite="+byteWrite);
        }

    }
}
