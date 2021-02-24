package com.wanderzhang.honeypot.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 全局消息队列
 *
 * @author 78445
 */
public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);
    private static final Queue<Message> QUEUE = new ConcurrentLinkedQueue<>();
    private static MessageQueue messageQueue = new MessageQueue();

    private MessageQueue() {
        logger.info("MessageQueue initialized");
    }

    public static MessageQueue getInstance() {
        return messageQueue;
    }

    public void addMessage(Message message) {
        QUEUE.add(message);
    }

    public List<Message> getMessage() {
        Message msg;
        List<Message> list = new ArrayList<>();
        synchronized (QUEUE) {
            while ((msg = QUEUE.poll()) != null) {
                list.add(msg);
            }
        }
        return list;
    }
}
