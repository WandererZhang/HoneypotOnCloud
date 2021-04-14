package com.wanderzhang.honeypot.websocket;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.pojo.Message;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 通过websocket实时更新信息
 * 需配置WebSocket的token
 *
 * @author 78445
 */
@Data
@Component
@ServerEndpoint("/updateMessage/{id}")
public class WebSocket {
    private Session session;
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    private static final String TOKEN = "XXXXXXXX";

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String id) {
        Assert.isTrue(id.equals(TOKEN), "websocket鉴权异常");
        this.session = session;
        webSockets.add(this);
        sessionMap.put(id, session);
        logger.info("【websocket消息】有新的连接,总数为:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        logger.info("【websocket消息】连接断开,总数为:" + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("【websocket消息】收到客户端消息:" + message);
    }

    public void sendMessage(Message message) {
        logger.info("【websocket消息】广播消息:" + message);
        for (WebSocket webSocket : webSockets) {
            JSONObject msg = new JSONObject(true);
            msg.put("address", message.getAddress());
            msg.put("method", message.getMethod());
            msg.put("date", message.getDate());
            webSocket.session.getAsyncRemote().sendText(msg.toJSONString());
        }
    }
}
