package com.logViz.dataservice.controller;


import com.logViz.dataservice.domain.RestResponseContainer;
import com.logViz.dataservice.model.LogAggregates;
import com.logViz.dataservice.service.LogAggregateService;
import com.logViz.dataservice.service.LogFileService;
import com.logViz.dataservice.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logviz")
public class LogVisualizationController {

    private final TestService testService;
    private final LogFileService logFileService;

    private final LogAggregateService logAggregateService;

    public LogVisualizationController(TestService testService, LogFileService logFileService, LogAggregateService logAggregateService) {
        this.testService = testService;
        this.logFileService = logFileService;
        this.logAggregateService = logAggregateService;
    }

    @PostMapping("/processLogFiles")
    public ResponseEntity<RestResponseContainer<String>> processLogFiles() {
        logFileService.processLogFiles();
        RestResponseContainer<String> response = RestResponseContainer.<String>builder()
                .data("Log Files Processed")
                .ok(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/processLogAggregates")
    public ResponseEntity<RestResponseContainer<String>> processLogAggregates() {
        logAggregateService.populateLogAggregates();
        RestResponseContainer<String> response = RestResponseContainer.<String>builder()
                .data("Log Aggregates Processed")
                .ok(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getLogAggregates")
    public ResponseEntity<RestResponseContainer<List<LogAggregates>>> getLogAggregates(
            @RequestParam String granularity,
            @RequestParam(required = false) String logLevel) {
        List<LogAggregates> logAggregates = logAggregateService.getLogAggregates(granularity, logLevel);
        RestResponseContainer<List<LogAggregates>> response = RestResponseContainer.<List<LogAggregates>>builder()
                .data(logAggregates)
                .ok(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
