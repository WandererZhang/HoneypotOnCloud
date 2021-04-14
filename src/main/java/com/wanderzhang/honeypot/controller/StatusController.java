package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.pojo.Result;
import com.wanderzhang.honeypot.service.LogAndStatusService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author 78445
 */
@RestController
@RequestMapping("/status")
public class StatusController {
    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    final
    LogAndStatusService logAndStatusService;

    public StatusController(LogAndStatusService logAndStatusService) {
        this.logAndStatusService = logAndStatusService;
    }

    @GetMapping("/query")
    @RequiresAuthentication
    public Result showStatus() {
        Map<String, String> map = logAndStatusService.queryStatus();
        if (!map.isEmpty()) {
            return Result.ok("蜜罐状态查询成功", map);
        }
        return Result.error("蜜罐状态查询失败");
    }

    @GetMapping("/update/{name}/{status}")
    @RequiresAuthentication
    public Result showStatus(@PathVariable("name") String name, @PathVariable("status") String status) throws IOException {
        logger.info("Honeypot status update");
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(name, status);
        if (logAndStatusService.updateStatus(entry)) {
            return Result.ok("蜜罐状态更新成功", entry);
        }
        return Result.ok("蜜罐状态更新失败或未改变");
    }
}
