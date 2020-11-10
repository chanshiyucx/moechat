package com.chanshiyu.chat;

import com.chanshiyu.chat.codec.Splitter;
import com.chanshiyu.chat.handler.AuthHandler;
import com.chanshiyu.chat.handler.request.LoginRequestHandler;
import com.chanshiyu.chat.handler.request.MessageRequestHandler;
import com.chanshiyu.chat.handler.request.SocketPacketCodecHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author SHIYU
 * @description Netty 服务端
 * @since 2020/11/9 15:47
 */
@Component
public class NettySocketServer {

    @Value("${netty.socket.port}")
    private int port;

    public void run() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Splitter());
                        ch.pipeline().addLast(new SocketPacketCodecHandler());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());

//                        ch.pipeline().addLast(SocketPacketCodecHandler.INSTANCE);
//                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
//                        ch.pipeline().addLast(AuthHandler.INSTANCE);
//                        ch.pipeline().addLast(MessageRequestHandler.INSTANCE);
//                        ch.pipeline().addLast(new SocketChannelInitializer());
                    }
                });
        bind(serverBootstrap, port);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }

}
