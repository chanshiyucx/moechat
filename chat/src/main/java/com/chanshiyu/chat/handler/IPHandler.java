package com.chanshiyu.chat.handler;

import com.chanshiyu.chat.attribute.ChannelAttributes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/16 11:41
 */
@Slf4j
@ChannelHandler.Sharable
public class IPHandler extends ChannelInboundHandlerAdapter {

    public static final IPHandler INSTANCE = new IPHandler();

    private IPHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ipAddress = null;
        if (msg instanceof HttpRequest) {
            HttpRequest mReq = (HttpRequest) msg;
            ipAddress = mReq.headers().get("http_client_ip");
            if (isEmpty(ipAddress)) {
                ipAddress = mReq.headers().get("x-forwarded-for");
            }
            if (isEmpty(ipAddress)) {
                ipAddress = mReq.headers().get("X-Real-IP");
            }
            if (isEmpty(ipAddress)) {
                ipAddress = mReq.headers().get("Proxy-Client-IP");
            }
            if (isEmpty(ipAddress)) {
                ipAddress = mReq.headers().get("WL-Proxy-Client-IP");
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        }
        if (isEmpty(ipAddress)) {
            InetSocketAddress socket = (InetSocketAddress) ctx.channel().remoteAddress();
            ipAddress = socket.getAddress().getHostAddress();
        }
        log.info("新用户接入IP：[{}]", ipAddress);
        ctx.channel().attr(ChannelAttributes.IP).set(ipAddress);
        ctx.pipeline().remove(this);
        super.channelRead(ctx, msg);
    }

    private boolean isEmpty(String ipAddress) {
        return ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress);
    }

}
