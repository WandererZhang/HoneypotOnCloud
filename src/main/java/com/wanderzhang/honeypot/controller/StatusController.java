package com.wanderzhang.honeypot.controller;

import com.wanderzhang.honeypot.service.LogAndStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.AbstractMap;
import java.util.Map;

/**
 * @author 78445
 */
@Controller
public class StatusController {
    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    final
    LogAndStatusService logAndStatusService;

    public StatusController(LogAndStatusService logAndStatusService) {
        this.logAndStatusService = logAndStatusService;
    }

    @RequestMapping("/status")
    public String showStatus(Model model) {
        model.addAttribute("statusMap", logAndStatusService.queryStatus());
        return "status";
    }

    @RequestMapping("/updateStatus/{name}/{status}")
    public String showStatus(Model model, @PathVariable("name") String name, @PathVariable("status") String status) {
        logger.info("Honeypot status update");
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(name, status);
        logAndStatusService.updateStatus(entry);
        return "redirect:/status";
    }
}
