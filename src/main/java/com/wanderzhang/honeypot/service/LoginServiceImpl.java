package com.wanderzhang.honeypot.service;

import com.wanderzhang.honeypot.threadpool.QueryThreadPool;
import org.springframework.stereotype.Service;

/**
 * @author 78445
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public void startQueryThreadPool() {
        QueryThreadPool.start();
    }
}
