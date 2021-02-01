package com.zyj.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: zyj
 * @time: 2021/2/1  22:16
 * @description:
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //客户端需要一个事件循环组

        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try{
            //创建一个启动对象
            //注意客户端创建的是bootstrap而不是ServerBootStrap
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数

            bootstrap.group(eventExecutors) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NioClientHandler());//加入自己的处理器
                        }
                    });

            System.out.println("客户端is OK..");
            //启动客户端去连接服务器端

            //关于ChannelFuture后面还要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6666).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }


    }

}
