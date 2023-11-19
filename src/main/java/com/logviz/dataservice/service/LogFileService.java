package com.logViz.dataservice.service;

import com.logViz.dataservice.model.LogEntry;
import com.logViz.dataservice.repository.LogEntryRepository;
import com.logViz.dataservice.util.LogParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogFileService {

    @Autowired
    private LogEntryRepository logEntryRepository;


    private static final String LOG_DIRECTORY = "./input";

    public void processLogFiles() {
        List<File> logFiles = collectLogFiles(new File(LOG_DIRECTORY));
//        System.out.println(logFiles);
        processLogFilesConcurrently(logFiles);
    }

    private List<File> collectLogFiles(File directory) {
        List<File> logFiles = new ArrayList<>();
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    logFiles.addAll(collectLogFiles(file));
                } else if (file.getName().endsWith(".txt")) {
                    logFiles.add(file);
                }
            }
        }
        return logFiles;
    }

    private void processLogFilesConcurrently(List<File> logFiles) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (File logFile : logFiles) {
            executor.submit(() -> processLogFile(logFile));
        }
        executor.shutdown();
    }

    private void processLogFile(File logFile) {
        System.out.println("Processing file: " + logFile.getName() + " in thread: " + Thread.currentThread().getName());
        try {
            List<String> lines = Files.readAllLines(logFile.toPath());
            for (String logLine : lines) {
                LogEntry logEntry = LogParser.parseLogEntry(logLine);
                if (logEntry != null) {
                    logEntryRepository.insert(logEntry);
                }
            }
        } catch (MalformedInputException e) {
            System.err.println("Error reading file " + logFile + " due to charset issues: " + e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
