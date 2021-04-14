package com.wanderzhang.honeypot.service;

import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.entity.MessageMap;
import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.utils.KubernetesUtils;
import com.wanderzhang.honeypot.websocket.WebSocket;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 78445
 */
@Service
public class LogAndStatusServiceImpl implements LogAndStatusService {
    private static final Logger logger = LoggerFactory.getLogger(LogAndStatusServiceImpl.class);
    private final WebSocket webSocket;

    public LogAndStatusServiceImpl(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public Map<String, String> queryStatus() {
        return KubernetesUtils.queryStatus();
    }

    @Override
    public boolean updateStatus(Map.Entry<String, String> entry) throws IOException {
        return KubernetesUtils.updateStatus(entry);
    }

    @Override
    public List<Message> queryHistoryMessage() {
        return MessageMap.getInstance().getMessage();
    }

    @Override
    public boolean deleteMessage(Message message) {
        return MessageMap.getInstance().deleteMessage(message);
    }

    @Override
    public JSONObject queryLocation() {
        return MessageMap.getInstance().getLocation();
    }

    @Override
    @Async("asyncThreadExecutor")
    public void updateMessage() {
        Config config = new ConfigBuilder().withMasterUrl("http://localhost:8080").build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext.Builder()
                .withGroup("devices.kubeedge.io")
                .withScope("Namespaced")
                .withVersion("v1alpha1")
                .withPlural("devices")
                .build();
        try {
            Map<String, String> labels = new HashMap<>(2);
            labels.put("description", "honeypot");
            labels.put("manufacturer", "test");
            Watcher<String> watcher = new Watcher<String>() {
                @Override
                public void eventReceived(Action action, String s) {
                    if (s != null) {
                        Message message = KubernetesUtils.toMessage(s);
                        if (message != null) {
                            if (MessageMap.getInstance().addMessage(message)) {
                                logger.info("receive new message");
                                webSocket.sendMessage(message);
                            }
                        } else {
                            KubernetesUtils.updateStatusMap(s);
                        }
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    logger.debug("K8S连接异常: ====================", e);
                }
            };
            client.customResource(context).watch("default", "honeypot", labels, new ListOptions(), watcher);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
