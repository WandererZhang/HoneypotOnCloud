package com.wanderzhang.honeypot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 调用api接口工具类
 *
 * @author 78445
 */
public class ApiUtils {
    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);
    private static int queryNumber = 0;

    public static synchronized String queryLocation(Message message) {
        String[] str = message.getAddress().split(":");
        String ip = str[0].substring(1);
        String httpUrl = "http://ip-api.com/json/" + ip + "?lang=zh-CN";
        BufferedReader reader;
        String result;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                stringBuilder.append(strRead);
                stringBuilder.append("\r\n");
            }
            reader.close();
        } catch (IOException e) {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e1) {
                logger.info("线程中断异常", e1);
            }
            if (queryNumber <= 5) {
                queryNumber++;
                return queryLocation(message);
            } else {
                queryNumber = 0;
                logger.info("超过api最大查询次数上限,message = " + message);
                return "未知";
            }
        }
        result = stringBuilder.toString();
        JSONObject object = JSON.parseObject(result);
        String location;
        location = object.getString("country") + " " + object.getString("regionName");
        return location;
    }
}
