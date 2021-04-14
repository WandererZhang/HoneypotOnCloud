package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.Result;
import com.wanderzhang.honeypot.service.LogAndStatusService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 78445
 */
@RestController
@RequestMapping("/log")
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    final
    LogAndStatusService logAndStatusService;

    public LogController(LogAndStatusService logAndStatusService) {
        this.logAndStatusService = logAndStatusService;
    }

    @GetMapping("/history")
    @RequiresAuthentication
    public Result showHistoryMessage() {
        logger.info("Send messageMap");
        return Result.ok("history", logAndStatusService.queryHistoryMessage());
    }

    @GetMapping("/location")
    @RequiresAuthentication
    public Result getLocation() {
        logger.info("Send locationMap");
        return Result.ok("location", logAndStatusService.queryLocation());
    }

    @PostMapping("/delete")
    @RequiresAuthentication
    public Result deleteMessage(@Validated @RequestBody Message message) {
        if (logAndStatusService.deleteMessage(message)) {
            return Result.create(233, "message", message);
        }
        return Result.error("删除失败!");
    }
}
