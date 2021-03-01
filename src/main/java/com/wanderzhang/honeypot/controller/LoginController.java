package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.service.LoginServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 78445
 */
@Controller
public class LoginController {
    final
    LoginServiceImpl loginServiceImpl;

    public LoginController(LoginServiceImpl loginServiceImpl) {
        this.loginServiceImpl = loginServiceImpl;
    }

    @PostMapping("/user/login")
    public String login(String userName, String password, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
            loginServiceImpl.startQueryThreadPool();
            return "/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在");
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
        }
        return "/login";
    }

    @RequestMapping("/user/logout")
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg", "登出成功");
        return "/login";
    }
}
