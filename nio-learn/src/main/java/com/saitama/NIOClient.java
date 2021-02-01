package com.saitama;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: zyj
 * @time: 2020/12/25  20:58
 * @description:
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();




        if(!socketChannel.connect(new InetSocketAddress("127.0.0.1",8888))){
            //代表没有连接成功
            while(!socketChannel.finishConnect()){
                //如果没有完成连接的话
                System.out.println("客户端还没连接成功");
            }
        }


        String data = "hello,nio";

        ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes());

        socketChannel.write(byteBuffer);



        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read;
        if ((read = socketChannel.read(buffer)) != -1){
            System.out.println("服务端写回数据："+ new String(buffer.array(),0,read));
        }
        System.in.read();
    }
}
