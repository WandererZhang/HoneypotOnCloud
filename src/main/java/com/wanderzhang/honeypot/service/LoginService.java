package com.wanderzhang.honeypot.service;

/**
 * 登录服务
 *
 * @author 78445
 */
public interface LoginService {
    /**
     * 开启K8S监听任务
     *
     * @author 78445
     */
    void startQueryThreadPool();
}
