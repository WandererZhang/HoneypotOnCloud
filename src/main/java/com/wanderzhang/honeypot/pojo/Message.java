package com.wanderzhang.honeypot.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息实体类
 *
 * @author 78445
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String resourceVersion;
    private String address;
    private String method;
    private String date;
    private String httpStatus;
    private String telnetStatus;
    private String redisStatus;
    private String mysqlStatus;
}
