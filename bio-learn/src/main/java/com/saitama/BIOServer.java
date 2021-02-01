package com.saitama;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zyj
 * @time: 2020/11/21  18:26
 * @description:
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        //线程池机制
        // 1. 创建一个线程池
        // 2. 如果有客户端链接 就创建一个线程与之通讯

        ExecutorService executorService = Executors.newCachedThreadPool();

        //创建ServerSocket

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while(true) {
            final Socket socket = serverSocket.accept();

            System.out.println("连接到一个客户端");

            //创建一个线程与之通信
            executorService.execute(()->{
                handler(socket);
            });

         }
    }


    public static void handler(Socket socket){

        System.out.println("当前线程id为："+Thread.currentThread().getId()+"名字为："+Thread.currentThread().getName());
        byte[] bytes = new byte[1024];

        //通过socket获取输入流
        try{
            InputStream inputStream = socket.getInputStream();
            System.out.println("当前线程id为："+Thread.currentThread().getId()+"名字为："+Thread.currentThread().getName());
            //循环读取客户端发送的数据
            while(true){
                int read = inputStream.read(bytes);
                if(read == -1){
                    break;
                }else {
                    System.out.println("输出数据");
                    System.out.println(new String(bytes,0,read)); //输出客户端发送的数据
                    System.out.println("完毕");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client链接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
