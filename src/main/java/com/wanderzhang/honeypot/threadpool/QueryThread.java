package com.wanderzhang.honeypot.threadpool;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageMap;
import com.wanderzhang.honeypot.pojo.MessageQueue;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 轮询K8S任务线程
 *
 * @author 78445
 */
public class QueryThread implements Runnable{
    private static int count = 0;
    @Override
    public void run() {
        MessageQueue messageQueue = MessageQueue.getInstance();
        MessageMap messageMap = MessageMap.getInstance();
        //TODO 读取K8S蜜罐状态
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message message = new Message(String.valueOf(count),String.valueOf(count),"123",sdf.format(date),"ON","OFF","OFF","OFF");
        messageQueue.addMessage(message);
        messageMap.addMessage(message);
        count++;
    }
}
