package com.chanshiyu.chat;

import com.chanshiyu.chat.handler.*;
import com.chanshiyu.chat.handler.request.HeartBeatRequestHandler;
import com.chanshiyu.chat.handler.request.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author SHIYU
 * @description Websocket 服务端
 * @since 2020/11/13 11:00
 */
@Slf4j
@Component
public class NettyWebSocketServer {

    @Value("${netty.websocket.port}")
    private int port;

    public void run() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 64));
                        ch.pipeline().addLast(IPHandler.INSTANCE);
                        ch.pipeline().addLast(ConnectionCountHandler.INSTANCE);
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/chat"));
                        ch.pipeline().addLast(WebSocketPacketCodecHandler.INSTANCE);
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
                log.info("websocket 端口[{}]绑定成功!", port);
            } else {
                log.error("websocket 端口[{}]绑定失败!", port);
            }
        });
    }

}
