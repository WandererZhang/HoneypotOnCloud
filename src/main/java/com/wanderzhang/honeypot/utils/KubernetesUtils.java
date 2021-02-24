package com.wanderzhang.honeypot.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询K8S工具类
 *
 * @author 78445
 */
public class KubernetesUtils {
    private static final String ON_STATUS = "ON";
    private static final String OFF_STATUS = "OFF";
    private static Map<String,String> statusMap = new HashMap<>(4);
    static {
        //statusMap = new HashMap<>(queryStatus());
        statusMap.put("httpStatus","ON");
        statusMap.put("telnetStatus","ON");
        statusMap.put("mysqlStatus","ON");
        statusMap.put("redisStatus","ON");
    }
    public static Map<String,String> queryStatus(){
        //TODO K8S读取蜜罐状态
        return statusMap;
    }
    public static void updateStatus(Map.Entry<String,String> entry){
        //TODO K8S修改蜜罐状态
        if (ON_STATUS.equals(entry.getValue())|| OFF_STATUS.equals(entry.getValue())) {
            statusMap.replace(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, String> temp: statusMap.entrySet())
        {
            System.out.println("Key: "+ temp.getKey()+ " Value: "+temp.getValue());
        }
    }
}
