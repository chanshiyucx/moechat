package com.chanshiyu.chat.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:11
 */
@Configuration
public class DisruptorWaitStrategyConfig {

    @Bean
    @ConditionalOnMissingBean(WaitStrategy.class)
    public WaitStrategy getWaitStrategy() {
        // 如果 CPU 比较叼的话，可以用 YieldingWaitStrategy
        return new BlockingWaitStrategy();
    }

}
