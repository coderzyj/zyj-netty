package com.saitama;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * @author: zyj
 * @time: 2021/1/13  17:29
 * @description:
 */
public class GroupChatClient {

    private final String ip = "127.0.0.1";

    private final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;


    private String username;
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器

        socketChannel = SocketChannel.open(new InetSocketAddress(ip,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册事件
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"is ok...");

    }

    //发送消息给服务器
    public void sendMessage(String message){
        message = username + "说:"+message;

        try{
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        }catch (Exception e){
            System.out.println("写入失败");
        }
    }

    //读取服务器消息
    public void read(){
        try{
                int read = selector.select();
                if(read > 0){
                    //说明通道有事件发生
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        if(key.isReadable()){
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            socketChannel.read(byteBuffer);
                            //把读到的数据转为字符串
                            String data = new String(byteBuffer.array());
                            System.out.println(data.trim());

                        }
                    }
                }else{
                    System.out.println("没有事件发生");
                }

        }catch (Exception e){

        }
    }
    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();

        new Thread(()->{
            while (true){
                chatClient.read();
                try{
                    Thread.sleep(3000);
                }catch (Exception e){

                }
            }

        }).start();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendMessage(s);
        }
    }
}
