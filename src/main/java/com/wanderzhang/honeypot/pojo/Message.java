package com.wanderzhang.honeypot.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(address, message.address) && Objects.equals(method, message.method) && Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, method, date);
    }
}
