package com.wanderzhang.honeypot.service;

import com.wanderzhang.honeypot.pojo.Message;

import java.util.List;
import java.util.Map;

/**
 * 蜜罐日志与状态服务
 *
 * @author 78445
 */
public interface LogAndStatusService {
    /**
     * 查询蜜罐状态
     *
     * @return 蜜罐开关状态
     */
    Map<String, String> queryStatus();

    /**
     * 更新蜜罐状态
     *
     * @param entry <String(蜜罐名称),String('ON' or 'OFF')>
     */
    void updateStatus(Map.Entry<String, String> entry);

    /**
     * 查询实时蜜罐信息
     *
     * @return 蜜罐信息列表
     */
    List<Message> queryRealTimeMessage();

    /**
     * 查询历史蜜罐信息
     *
     * @return 蜜罐信息列表
     */
    List<Message> queryHistoryMessage();

    /**
     * 查询用于监听K8S线程状态
     *
     * @return Boolean
     */
    boolean queryThreadStatus();
}
