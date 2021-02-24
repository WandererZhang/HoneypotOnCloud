package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.pojo.Message;
import com.wanderzhang.honeypot.pojo.MessageMap;
import com.wanderzhang.honeypot.pojo.MessageQueue;
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

    @ResponseBody
    @RequestMapping("/log/ajax")
    public List<Message> updateMessage() {
        logger.info("Send message by ajax");
        return MessageQueue.getInstance().getMessage();
    }

    @RequestMapping("/history")
    public String showHistory(Model model) {
        logger.info("Send messageMap");
        model.addAttribute("messageMap", MessageMap.getInstance().getMessage());
        return "/history";
    }
}
