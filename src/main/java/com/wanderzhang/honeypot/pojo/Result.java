package com.wanderzhang.honeypot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装响应结果
 *
 * @author 78445
 */
@Data
@NoArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    private Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result ok(String msg) {
        return new Result(200, msg);
    }

    public static Result error(String msg) {
        return new Result(400, msg);
    }

    public static Result error() {
        return new Result(400, "异常错误");
    }

    public static Result create(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result create(Integer code, String msg) {
        return new Result(code, msg);
    }
}
