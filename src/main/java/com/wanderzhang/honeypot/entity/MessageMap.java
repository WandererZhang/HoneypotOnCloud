package com.wanderzhang.honeypot.entity;

import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.utils.ApiUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局消息Map
 *
 * @author 78445
 */
@Data
public class MessageMap {
    private static final Logger logger = LoggerFactory.getLogger(MessageMap.class);
    private static Map<Message, String> storeMap = new ConcurrentHashMap<>();
    private static Map<String, Integer> locationMap = new ConcurrentHashMap<>();
    private static MessageMap messageMap = new MessageMap();
    private static int httpNumber = 0;
    private static int telnetNumber = 0;
    private static int redisNumber = 0;
    private static int mysqlNumber = 0;

    private MessageMap() {
        logger.info("MessageMap initialized");
    }

    public static MessageMap getInstance() {
        return messageMap;
    }

    public boolean addMessage(Message message) {
        if (storeMap.containsKey(message)) {
            return false;
        }
        String location = ApiUtils.queryLocation(message);
        if (!location.equals("未知")) {
            if (location.startsWith("中国")) {
                String region = location.split(" ")[1];
                if (!locationMap.containsKey(region)) {
                    locationMap.put(region, 1);
                } else {
                    locationMap.put(region, locationMap.get(region) + 1);
                }
            } else if (location.startsWith("台湾") || location.startsWith("香港") || location.startsWith("澳门")) {
                String region = location.split(" ")[0];
                if (!locationMap.containsKey(region)) {
                    locationMap.put(region, 1);
                } else {
                    locationMap.put(region, locationMap.get(region) + 1);
                }
            } else {
                if (!locationMap.containsKey("海外")) {
                    locationMap.put("海外", 1);
                } else {
                    locationMap.put("海外", locationMap.get("海外") + 1);
                }
            }
        }
        message.setLocation(location);
        storeMap.put(message, message.getVersion());
        if (message.getMethod().startsWith("Http")) {
            httpNumber++;
        } else if (message.getMethod().startsWith("Mysql")) {
            mysqlNumber++;
        } else if (message.getMethod().startsWith("Redis")) {
            redisNumber++;
        } else if (message.getMethod().startsWith("Telnet")) {
            telnetNumber++;
        }
        return true;
    }

    public Map<String, String> getAttackNumber() {
        Map<String, String> map = new HashMap<>(4);
        map.put("httpNumber", String.valueOf(httpNumber));
        map.put("telnetNumber", String.valueOf(telnetNumber));
        map.put("redisNumber", String.valueOf(redisNumber));
        map.put("mysqlNumber", String.valueOf(mysqlNumber));
        return map;
    }

    public List<Message> getMessage() {
        LinkedList<Message> list = new LinkedList<>();
        for (Map.Entry<Message, String> entry : storeMap.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public JSONObject getLocation() {
        JSONObject object = new JSONObject(true);
        if (!locationMap.isEmpty()) {
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(locationMap.entrySet());
            entryList.sort((o1, o2) -> o2.getValue() - o1.getValue());
            for (Map.Entry<String, Integer> entry : entryList) {
                object.put(entry.getKey(), entry.getValue());
            }
        }
        return object;
    }

    public boolean deleteMessage(Message message) {
        return storeMap.keySet().removeIf(key -> key.equals(message));
    }
}
