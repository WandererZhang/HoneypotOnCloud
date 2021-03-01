package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.service.LogAndStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 78445
 */
@Controller
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    final
    LogAndStatusService logAndStatusService;

    public LogController(LogAndStatusService logAndStatusService) {
        this.logAndStatusService = logAndStatusService;
    }

    @ResponseBody
    @RequestMapping("/log/ajax")
    public List<Message> showRealTimeMessage() {
        logger.info("Send message by ajax");
        return logAndStatusService.queryRealTimeMessage();
    }

    @RequestMapping("/history")
    public String showHistoryMessage(Model model) {
        logger.info("Send messageMap");
        model.addAttribute("messageMap", logAndStatusService.queryHistoryMessage());
        return "history";
    }
}
