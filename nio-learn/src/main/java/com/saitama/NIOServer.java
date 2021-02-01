package com.saitama;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: zyj
 * @time: 2020/12/25  20:46
 * @description:
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {
        //打开一个serversocketchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //选择非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //打开选择器
        Selector selector = Selector.open();
        
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        
        //注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        while(true){
            
            if(selector.select(1000) == 0){
                System.out.println("服务端在1秒内没有连接进来....");
                continue;
            }

            //获取轮询器事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    //如果是连接事件，说明是服务端的,获取客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);
                    //注册可读事件
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                
                if(key.isReadable()){
                    //如果是可读事件，说明客户端的数据准备好了,首先获取客户端通道
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = socketChannel.read(buffer);
                    System.out.println(new String(buffer.array(),0,read));



                    //注册写事件
                    SelectionKey writeKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
                    writeKey.attach("收到数据");
                }

                if(key.isWritable()){
                    //此时代表可以发送数据给客户端
//                    String clientData = "hello,nio client";
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    String attachment = (String) key.attachment();

                    System.out.println("输出内容"+attachment);
//                    socketChannel.write(ByteBuffer.wrap(attachment.getBytes()));
                    socketChannel.write(ByteBuffer.wrap(attachment.getBytes()));
                }
                iterator.remove();
            }
        }
        
    }
}
