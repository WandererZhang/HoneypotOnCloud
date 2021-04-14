package com.wanderzhang.honeypot.service;

import com.alibaba.fastjson.JSONObject;
import com.wanderzhang.honeypot.pojo.Message;

import java.io.IOException;
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
     * @return boolean 更新成功时返回true
     * @throws IOException 抛出IO异常
     */
    boolean updateStatus(Map.Entry<String, String> entry) throws IOException;

    /**
     * 查询历史蜜罐信息
     *
     * @return List<Message> <攻击者信息>
     */
    List<Message> queryHistoryMessage();

    /**
     * 删除MessageMap中选定的信息
     *
     * @param message 需要删除的Message对象
     * @return boolean 删除成功时返回true,失败返回false
     */
    boolean deleteMessage(Message message);

    /**
     * 查询地理位置信息和攻击次数
     *
     * @return Map<String, Integer> <位置信息,攻击次数>
     */
    JSONObject queryLocation();

    /**
     * 启动实时更新蜜罐消息线程
     */
    void updateMessage();
}
