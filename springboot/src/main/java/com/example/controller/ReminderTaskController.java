package com.example.controller;

import com.example.service.ReminderTaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/reminder")
public class ReminderTaskController {

    @Resource
    private ReminderTaskService reminderTaskService;

    @RequestMapping("/check")
    public void checkReminders() {
        reminderTaskService.checkReminders();
    }
}
