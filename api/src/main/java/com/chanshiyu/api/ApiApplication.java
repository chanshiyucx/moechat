package com.chanshiyu.api;

import com.chanshiyu.chat.disruptor.RingBufferWorkerPoolFactory;
import com.chanshiyu.chat.disruptor.consumer.MessageConsumer;
import com.chanshiyu.common.util.SpringUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.Security;

/**
 * @author SHIYU
 * @description 启动器
 * @since 2020/11/9 9:35
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.chanshiyu"})
@MapperScan(basePackages = "com.chanshiyu.mbg.mapper")
@EnableScheduling
public class ApiApplication {

    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);

        // 导入支持AES/CBC/PKCS7Padding的Provider
        Security.addProvider(new BouncyCastleProvider());

        // 启动 disruptor
        MessageConsumer[] consumers = new MessageConsumer[16];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumer();
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory factory = SpringUtil.getBean(RingBufferWorkerPoolFactory.class);
        factory.initAndStart(consumers);
    }

}