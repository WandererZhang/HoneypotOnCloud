package com.wanderzhang.honeypot;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageQueue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class HoneypotApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        Map<String,String> map = new HashMap<>();
        map.put("httpStatus","ON");
        map.put("telnetStatus","ON");
        map.put("mysqlStatus","ON");
        map.put("redisStatus","ON");
        map.replace("redisStatus","OFF");
        for(Map.Entry<String, String> temp: map.entrySet())
        {
            System.out.println("Key: "+ temp.getKey()+ " Value: "+temp.getValue());
        }
    }

}
