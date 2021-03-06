package com.chanshiyu.chat;

import com.chanshiyu.chat.handler.*;
import com.chanshiyu.chat.codec.Splitter;
import com.chanshiyu.chat.handler.request.HeartBeatRequestHandler;
import com.chanshiyu.chat.handler.request.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author SHIYU
 * @description Socket 服务端
 * @since 2020/11/9 15:47
 */
@Slf4j
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
                        ch.pipeline().addLast(IPHandler.INSTANCE);
                        ch.pipeline().addLast(ConnectionCountHandler.INSTANCE);
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Splitter());
                        ch.pipeline().addLast(SocketPacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        ch.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);
                    }
                });
        bind(serverBootstrap, port);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("socket 端口[{}]绑定成功!", port);
            } else {
                log.error("socket 端口[{}]绑定失败!", port);
            }
        });
    }

}
