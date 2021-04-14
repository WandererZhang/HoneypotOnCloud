package com.wanderzhang.honeypot.controller;

import cn.hutool.core.lang.Assert;
import com.wanderzhang.honeypot.pojo.Result;
import com.wanderzhang.honeypot.pojo.User;
import com.wanderzhang.honeypot.service.LogAndStatusService;
import com.wanderzhang.honeypot.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 需配置username和password
 *
 * @author 78445
 */
@RestController
@RequestMapping("/user")
public class LoginController {
    private static final Long USERID = 2017211210L;
    private static final String USERNAME = "XXXXXX";
    private static final String PASSWORD = "XXXXXX";

    final
    LogAndStatusService logAndStatusService;
    final
    JwtUtils jwtUtils;

    public LoginController(JwtUtils jwtUtils, LogAndStatusService logAndStatusService) {
        this.jwtUtils = jwtUtils;
        this.logAndStatusService = logAndStatusService;
    }

    @PostMapping("/login")
    public Result login(@Validated @RequestBody User user, HttpServletResponse response) {
        Assert.isTrue(user.getUsername().equals(USERNAME), "用户不存在");
        Assert.isTrue(user.getPassword().equals(PASSWORD), "账号或密码错误");
        String jwt = jwtUtils.generateToken(USERID);
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        logAndStatusService.updateMessage();
        return Result.ok("登陆成功", user.getUsername());
    }

    @GetMapping("/logout")
    @RequiresAuthentication
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok("登出成功");
    }

}
