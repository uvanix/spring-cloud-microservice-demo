package com.uvanix.cloud.web;

import com.uvanix.cloud.service.biz.ScheduleJobService;
import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author uvanix
 * @date 2018/7/7
 */
@RestController
@RequestMapping("/job")
public class ScheduleJobController {

    private final ScheduleJobService scheduleJobService;

    public ScheduleJobController(ScheduleJobService scheduleJobService) {
        this.scheduleJobService = scheduleJobService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllJob() throws SchedulerException {
        List<ScheduleJob> jobList = scheduleJobService.getAllJob();
        Map<String, Object> result = new HashMap<>();
        result.put("total", jobList.size());
        result.put("list", jobList);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/running")
    public ResponseEntity<Map<String, Object>> getRunningJob() throws SchedulerException {
        List<ScheduleJob> jobList = scheduleJobService.getRunningJob();
        Map<String, Object> result = new HashMap<>();
        result.put("total", jobList.size());
        result.put("list", jobList);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // TODO check param

        scheduleJobService.addJob(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        scheduleJobService.pauseJob(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resumeJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        scheduleJobService.resumeJob(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        boolean result = scheduleJobService.stopJob(scheduleJob);

        return ResponseEntity.ok(String.valueOf(result));
    }

    @PostMapping("/start")
    public ResponseEntity<String> startJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        scheduleJobService.startJob(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteJob(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        boolean result = scheduleJobService.deleteJob(scheduleJob);

        return ResponseEntity.ok(String.valueOf(result));
    }

    @PostMapping("/runOnce")
    public ResponseEntity<String> runJobOnce(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        scheduleJobService.runJobOnce(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @PostMapping("/updateCron")
    public ResponseEntity<String> updateJobCronExpression(@RequestBody ScheduleJob scheduleJob) throws SchedulerException {
        // TODO check param

        scheduleJobService.updateJobCronExpression(scheduleJob);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    @GetMapping("/scheduleMetadata")
    public ResponseEntity<SchedulerMetaData> getSchedulerMetaData() throws SchedulerException {
        return ResponseEntity.ok(scheduleJobService.getSchedulerMetaData());
    }
}
