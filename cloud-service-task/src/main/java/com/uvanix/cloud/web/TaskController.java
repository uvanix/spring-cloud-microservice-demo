package com.uvanix.cloud.web;

import com.uvanix.cloud.task.SpringDynamicCronTask;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yvan
 * @date 2018/7/5
 */
@RestController
public class TaskController {

    @GetMapping("/updateTaskCron")
    public String updateTaskCron(@RequestParam String cron) {
        SpringDynamicCronTask.cron = cron;
        SpringDynamicCronTask.cron2 = cron;

        return HttpStatus.OK.getReasonPhrase();
    }
}
