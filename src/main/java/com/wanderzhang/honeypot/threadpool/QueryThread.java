package com.wanderzhang.honeypot.threadpool;

import com.alibaba.fastjson.JSONObject;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageMap;
import com.wanderzhang.honeypot.pojo.MessageQueue;
import com.wanderzhang.honeypot.utils.KubernetesUtils;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

/**
 * 监听K8S任务线程
 *
 * @author 78445
 */
public class QueryThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(QueryThread.class);
    private static final Config CONFIG = new ConfigBuilder().withMasterUrl("http://127.0.0.1:8080").build();
    private static final KubernetesClient CLIENT = new DefaultKubernetesClient(CONFIG);
    private static final CustomResourceDefinitionContext CONTEXT = new CustomResourceDefinitionContext.Builder()
            .withGroup("devices.kubeedge.io")
            .withScope("Namespaced")
            .withVersion("v1alpha1")
            .withPlural("devices")
            .build();

    @Override
    public void run() {
        try {
            Map<String, Object> objectMap = CLIENT.customResource(CONTEXT).get("default", "honeypot");
            JSONObject jsonObject = new JSONObject(objectMap);
            JSONObject metadata = jsonObject.getJSONObject("metadata");
            String resourceVersion = metadata.getString("resourceVersion");
            Map<String, String> labels = new HashMap<>(2);
            labels.put("description", "honeypot");
            labels.put("manufacturer", "test");
            Watcher<String> watcher = new Watcher<String>() {
                @Override
                public void eventReceived(Action action, String s) {
                    Message message = KubernetesUtils.queryMessage();
                    if (message != null) {
                        MessageMap.getInstance().addMessage(message);
                        MessageQueue.getInstance().addMessage(message);
                        logger.info("receive new message");
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    QueryThreadPool.error();
                    logger.info("K8S query failed");
                }
            };
            CLIENT.customResource(CONTEXT).watch("default", "honeypot", labels, resourceVersion, watcher);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            QueryThreadPool.error();
            logger.info("K8S query failed");
        }
    }
}
