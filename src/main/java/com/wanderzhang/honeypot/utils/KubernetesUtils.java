package com.wanderzhang.honeypot.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageMap;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询K8S工具类
 *
 * @author 78445
 */
public class KubernetesUtils {
    private static String statusVersion = "";
    private static String messageVersion = "";
    private static final String ON_STATUS = "ON";
    private static final String OFF_STATUS = "OFF";
    private static final String DEFAULT_HOST_VALUE = "edge";
    private static final Logger logger = LoggerFactory.getLogger(KubernetesUtils.class);
    private static Map<String, String> statusMap = new ConcurrentHashMap<>(4);
    private static final Config CONFIG = new ConfigBuilder().withMasterUrl("http://127.0.0.1:8080").build();
    private static final KubernetesClient CLIENT = new DefaultKubernetesClient(CONFIG);
    private static final CustomResourceDefinitionContext CONTEXT = new CustomResourceDefinitionContext.Builder()
            .withGroup("devices.kubeedge.io")
            .withScope("Namespaced")
            .withVersion("v1alpha1")
            .withPlural("devices")
            .build();

    static {
        statusMap.put("httpStatus", "OFF");
        statusMap.put("telnetStatus", "OFF");
        statusMap.put("mysqlStatus", "OFF");
        statusMap.put("redisStatus", "OFF");
        queryStatus();
    }

    public static synchronized Map<String, String> queryStatus() {
        try {
            Map<String, Object> objectMap = CLIENT.customResource(CONTEXT).get("default", "honeypot");
            JSONObject jsonObject = new JSONObject(objectMap);
            JSONObject metadata = jsonObject.getJSONObject("metadata");
            String resourceVersion = metadata.getString("resourceVersion");
            if (!resourceVersion.equals(statusVersion)) {
                JSONObject status = jsonObject.getJSONObject("status");
                JSONArray twins = status.getJSONArray("twins");
                for (int i = 0; i < twins.size(); i++) {
                    if (i > 2) {
                        JSONObject twinArray = (JSONObject) twins.get(i);
                        String value = twinArray.getJSONObject("reported").getString("value");
                        String propertyName = twinArray.getString("propertyName");
                        statusMap.put(propertyName, value);
                        logger.info("honeypotStatus: propertyName=" + propertyName + ", value=" + value);
                    }
                }
                statusVersion = resourceVersion;
                logger.info("Query honeypot status was successful, version=" + statusVersion);
            }
            return statusMap;
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    public static synchronized Message queryMessage() {
        try {
            Message message = new Message();
            Map<String, Object> objectMap = CLIENT.customResource(CONTEXT).get("default", "honeypot");
            JSONObject jsonObject = new JSONObject(objectMap);
            JSONObject metadata = jsonObject.getJSONObject("metadata");
            String resourceVersion = metadata.getString("resourceVersion");
            if (!resourceVersion.equals(messageVersion)) {
                JSONObject status = jsonObject.getJSONObject("status");
                JSONArray twins = status.getJSONArray("twins");
                for (Object twin : twins) {
                    JSONObject twinArray = (JSONObject) twin;
                    String value = twinArray.getJSONObject("reported").getString("value");
                    String propertyName = twinArray.getString("propertyName");
                    logger.info("honeypotStatus: propertyName=" + propertyName + ", value=" + value);
                    switch (propertyName) {
                        case "address":
                            if (value.equals(DEFAULT_HOST_VALUE)) {
                                return null;
                            }
                            message.setAddress(value);
                            break;
                        case "method":
                            message.setMethod(value);
                            break;
                        case "date":
                            DateFormat toDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                            DateFormat toStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date date = toDateFormat.parse(value);
                                message.setDate(toStrFormat.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                }
                messageVersion = resourceVersion;
                message.setResourceVersion(resourceVersion);
                if (MessageMap.getInstance().getMessage().contains(message)){
                    return null;
                }
                logger.info("Query honeypot message was successful, version=" + messageVersion + ", message: " + message);
                return message;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    public static void updateStatus(Map.Entry<String, String> entry) {
        if (ON_STATUS.equals(entry.getValue()) || OFF_STATUS.equals(entry.getValue())) {
            try {
                Map<String, Object> objectMap = CLIENT.customResource(CONTEXT).get("default", "honeypot");
                JSONObject jsonObject = new JSONObject(objectMap);
                JSONObject status = jsonObject.getJSONObject("status");
                JSONArray twins = status.getJSONArray("twins");
                for (Object twin : twins) {
                    JSONObject twinArray = (JSONObject) twin;
                    JSONObject desired = twinArray.getJSONObject("desired");
                    String propertyName = twinArray.getString("propertyName");
                    if (propertyName.equals(entry.getKey())) {
                        desired.put("value", entry.getValue());
                    }
                }
                jsonObject.put("status", status);
                CLIENT.customResource(CONTEXT).edit("default", "honeypot", jsonObject);
                statusMap.replace(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }
}
