package com.wanderzhang.honeypot.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局消息Map
 *
 * @author 78445
 */
public class MessageMap {
    private static final Logger logger = LoggerFactory.getLogger(MessageMap.class);
    private static final Map<String, Message> MAP = new ConcurrentHashMap<>();
    private static MessageMap messageMap = new MessageMap();

    private MessageMap() {
        logger.info("MessageMap initialized");
    }

    public static MessageMap getInstance() {
        return messageMap;
    }

    public void addMessage(Message message) {
        MAP.put(message.getResourceVersion(), message);
    }

    public List<Message> getMessage() {
        return new ArrayList<>(MAP.values());
    }
}
