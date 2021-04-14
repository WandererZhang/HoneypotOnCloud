package com.wanderzhang.honeypot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * spring线程池配置类
 *
 * @author 78445
 */
@Configuration
@EnableAsync
public class AsyncExecutorConfig {
    private final Integer threadNumber = Runtime.getRuntime().availableProcessors();

    @Bean("asyncThreadExecutor")
    public Executor asyncThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadNumber);
        executor.setQueueCapacity(threadNumber * 5);
        executor.setMaxPoolSize(threadNumber * 2);
        executor.setKeepAliveSeconds(30);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

