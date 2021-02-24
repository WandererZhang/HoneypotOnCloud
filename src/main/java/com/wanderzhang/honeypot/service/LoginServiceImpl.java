package com.wanderzhang.honeypot.service;

/**
 * @author 78445
 */
public interface LoginServiceImpl {
    /**
     * 开启K8S轮询任务
     *
     * @author 78445
     */
    void startScheduledExecutor();

    /**
     * 关闭K8S轮询任务
     *
     * @author 78445
     */
    void endScheduledExecutor();
}
