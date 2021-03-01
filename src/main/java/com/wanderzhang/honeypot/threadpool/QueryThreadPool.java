package com.wanderzhang.honeypot.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 轮询K8S中的蜜罐信息
 *
 * @author 78445
 */
public class QueryThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(QueryThreadPool.class);
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_NUM;
    private static final int MAXIMUM_POOL_SIZE = CPU_NUM * 2;
    private static final long KEEP_ALIVE_TIME = 10;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(CPU_NUM);
    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, WORK_QUEUE, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    private static volatile boolean Status = false;

    private QueryThreadPool() {
        logger.info("ScheduledThreadPool initialized");
    }

    public static void start() {
        if (!Status) {
            THREAD_POOL.execute(new QueryThread());
            logger.info("QueryThread is start");
            Status = true;
        }
    }

    public static void error() {
        Status = false;
        logger.info("QueryThread is end");
    }

    public static boolean queryStatus() {
        return Status;
    }
}
