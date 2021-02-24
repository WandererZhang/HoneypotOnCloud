package com.wanderzhang.honeypot.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 轮询K8S中的蜜罐信息
 *
 * @author 78445
 */
public class ScheduledExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutor.class);
    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("queryK8S-schedule-pool-%d").daemon(true).build());
    private static final QueryThread THREAD = new QueryThread();
    private static ScheduledFuture<?> future;
    private static volatile boolean status = false;

    private ScheduledExecutor() {
        logger.info("ScheduledThreadPool initialized");
    }

    public static void start() {
        if (!status) {
            future = executorService.scheduleAtFixedRate(THREAD, 0, 1000, TimeUnit.MILLISECONDS);
            logger.info("QueryThread is start");
            status = true;
        }
    }

    public static void end() {
        if (status) {
            future.cancel(true);
            status = false;
            logger.info("QueryThread is end");
        }
    }
}
