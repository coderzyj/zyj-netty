package com.zyj.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author: zyj
 * @time: 2021/2/1  21:59
 * @description:
 *
 * 说明
 * 1. 我们自定义一个handler需要继承netty规定好的某个handler适配器
 * 2. 这时我们自定义一个handler，才能称为一个handler
 */
public class NioServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据实际（这里我们可以读取客户端发送的信息）
     *
     * 1.ChannelHandlerContext ctx:上下文对象，含有管道pipeline，通道channel，地址
     * 2.Object msg：就是客户端发送的数据 默认Object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = "+ctx);

        //将msg转化成bytebuf
        //ByteBuf是netty提供的，不是nio的bytebuffer
        ByteBuf buf = ((ByteBuf) msg);
        System.out.println("客户端发送的消息是:"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }


    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush是write + flush
        //将数据写道缓冲 ，并刷新
        //一般讲，我们对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
