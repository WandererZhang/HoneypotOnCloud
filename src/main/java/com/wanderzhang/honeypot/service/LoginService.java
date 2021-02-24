package com.wanderzhang.honeypot.service;

import com.wanderzhang.honeypot.threadpool.ScheduledExecutor;
import org.springframework.stereotype.Service;

/**
 * @author 78445
 */
@Service
public class LoginService implements LoginServiceImpl{

    @Override
    public void startScheduledExecutor() {
        ScheduledExecutor.start();
    }

    @Override
    public void endScheduledExecutor() {
        ScheduledExecutor.end();
    }
}
