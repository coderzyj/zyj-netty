package com.saitama;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: zyj
 * @time: 2021/1/13  15:50
 * @description:
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final int PORT = 6667;


    public GroupChatServer(){
        try{
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            serverSocketChannel.configureBlocking(false);
            //注册连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (Exception e){

        }


    }

    public void listen(){
        try{
            while(true){
                int count = selector.select(2000);
                if(count > 0){
                    //如果有事件发生，就开始处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            //将客户端channnel注册到selector，监听read事件
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            //输出客户端上线
                            System.out.println("客户端"+socketChannel.getRemoteAddress()+"上线！");
                        }

                        if(key.isReadable()){
                            readData(key);
                        }
                        //删除该key
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待中.....");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }


    public void readData(SelectionKey key){
        //如果通道可读
        SocketChannel  channel = null;
        try{
            channel= (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            if(channel.read(buffer) > 0){
                String message = new String(buffer.array());
                System.out.println("接收到客户端"+channel.getRemoteAddress()+"的消息"+message);
                //转发消息给其他客户端
                sendInfoToOtherClient(message,channel);
            }
        }catch (IOException e){
            try {
                System.out.println("远程客户端："+channel.getRemoteAddress()+"断开连接");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

    }

    public void sendInfoToOtherClient(String message,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中....");
        Set<SelectionKey> keys = selector.keys();
        for(SelectionKey key:keys){
            Channel channel = key.channel();

            if(channel instanceof SocketChannel && channel != self){

                //转型
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                //将buffer的数据写入通道中
                socketChannel.write(buffer);
            }
        }
    }
    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
