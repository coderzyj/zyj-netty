package com.zyj.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: zyj
 * @time: 2021/2/1  21:39
 * @description:
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //创建bossGroup 和 workerGroup
            //说明
            // 1. 创建两个线程组 bossGroup和workerGroup
            //2. bossGroup 只是处理连接请求， 真正的与客户端业务处理会交给workerGroup来完成，自己不做处理
            //3. 两个都是无线循环


            //创建服务启动启动配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            serverBootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象（匿名对象）
                        //想pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new NioServerHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器


            System.out.println("服务器准备好了....");
            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture sync = serverBootstrap.bind(6666).sync();

            //对关闭通道进行监听
            sync.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
        }

    }
}
