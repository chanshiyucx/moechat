package com.chanshiyu.chat.disruptor;

import com.chanshiyu.chat.disruptor.consumer.MessageConsumer;
import com.chanshiyu.chat.disruptor.producer.MessageProducer;
import com.chanshiyu.chat.disruptor.wapper.TranslatorDataWrapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:11
 */
@Component
public class RingBufferWorkerPoolFactory {

    @Autowired
    private WaitStrategy waitStrategy;

    private final Map<Byte, MessageProducer> producers = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    public void initAndStart(MessageConsumer[] messageConsumers) {
        //1. 构建 ringBuffer 对象
        ringBuffer = RingBuffer.create(ProducerType.MULTI,
                TranslatorDataWrapper::new,
                1024 * 1024,
                waitStrategy);
        //2. 通过 ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        //3. 创建多个消费者数组
        WorkerPool<TranslatorDataWrapper> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(), messageConsumers);
        //4. 设置多个消费者的 sequence 序号，用于单独统计消费进度, 并且设置到 ringBuffer 中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //5. 启动我们的工作池
        workerPool.start(Executors.newFixedThreadPool(16));
    }

    public MessageProducer getMessageProducer(Byte command) {
        MessageProducer messageProducer = producers.get(command);
        if (messageProducer == null) {
            messageProducer = new MessageProducer(command, ringBuffer);
            producers.put(command, messageProducer);
        }
        return messageProducer;
    }

    @Slf4j
    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWrapper> {
        @Override
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWrapper event) {
            log.error("handleEventException -> ex:{}  sequence:{} event:{}", ex.getMessage(), sequence, event.getClass().toString());
            ex.printStackTrace();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("handleOnStartException -> ex:{}", ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("handleOnShutdownException -> ex:{} ", ex.getMessage());
            ex.printStackTrace();
        }
    }

}
