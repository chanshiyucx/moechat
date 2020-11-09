package com.chanshiyu.api;

import com.chanshiyu.chat.NettySocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 16:10
 */
@Component
public class NettyBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Socket
     */
    private final NettySocketServer nettySocketServer;

    @Autowired
    public NettyBootstrap(NettySocketServer nettySocketServer) {
        this.nettySocketServer = nettySocketServer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent crt) {
        if (crt.getApplicationContext().getParent() == null) {
            this.nettySocketServer.run();
        }
    }

}
