package com.wanderzhang.honeypot.service;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageMap;
import com.wanderzhang.honeypot.pojo.MessageQueue;
import com.wanderzhang.honeypot.threadpool.QueryThreadPool;
import com.wanderzhang.honeypot.utils.KubernetesUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 78445
 */
@Service
public class LogAndStatusServiceImpl implements LogAndStatusService {
    @Override
    public Map<String, String> queryStatus() {
        return KubernetesUtils.queryStatus();
    }

    @Override
    public void updateStatus(Map.Entry<String, String> entry) {
        KubernetesUtils.updateStatus(entry);
    }

    @Override
    public List<Message> queryRealTimeMessage() {
        return MessageQueue.getInstance().getMessage();
    }

    @Override
    public List<Message> queryHistoryMessage() {
        return MessageMap.getInstance().getMessage();
    }

    @Override
    public boolean queryThreadStatus() {
        return QueryThreadPool.queryStatus();
    }
}
