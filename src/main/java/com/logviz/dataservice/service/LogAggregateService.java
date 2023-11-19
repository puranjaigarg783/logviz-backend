package com.logViz.dataservice.service;

import com.logViz.dataservice.model.LogAggregateKey;
import com.logViz.dataservice.model.LogAggregates;
import com.logViz.dataservice.model.LogEntry;
import com.logViz.dataservice.repository.LogAggregateRepository;
import com.logViz.dataservice.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LogAggregateService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private LogAggregateRepository logAggregateRepository;

    public void populateLogAggregates() {
        List<LogEntry> logEntries = logEntryRepository.fetchAll();


        Map<LogAggregateKey, List<LogEntry>> groupedLogsByHour = logEntries.parallelStream()
                .collect(Collectors.groupingBy(this::convertToHourAggregateKey));
        computeAndPersistAggregates(groupedLogsByHour);


        Map<LogAggregateKey, List<LogEntry>> groupedLogsByDay = logEntries.parallelStream()
                .collect(Collectors.groupingBy(this::convertToDayAggregateKey));
        computeAndPersistAggregates(groupedLogsByDay);
    }

    private void computeAndPersistAggregates(Map<LogAggregateKey, List<LogEntry>> groupedLogs) {
        List<LogAggregates> aggregates = groupedLogs.entrySet().parallelStream()
                .peek(entry -> System.out.println("Aggregating in thread: " + Thread.currentThread().getName()))
                .map(entry -> buildLogAggregates(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        aggregates.forEach(logAggregateRepository::insert);
    }
    private LogAggregates buildLogAggregates(LogAggregateKey key, List<LogEntry> entries) {
        LogAggregates logAggregate = new LogAggregates();
        logAggregate.setDate(key.getDate());
        logAggregate.setHour(key.getHour());
        logAggregate.setLogLevel(key.getLogLevel());

        logAggregate.setCount(entries.size());

        Map<String, Long> ipCounts = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getIp, Collectors.counting()));
        String mostFrequentIp = ipCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
        logAggregate.setMostFrequentIp(mostFrequentIp);

        double avgLength = entries.stream()
                .mapToInt(e -> e.getMessage().length())
                .average().orElse(0);
        int maxLength = entries.stream()
                .mapToInt(e -> e.getMessage().length())
                .max().orElse(0);
        logAggregate.setAvgMessageLength((int) avgLength);
        logAggregate.setMaxMessageLength(maxLength);

        long uniqueProcesses = entries.stream()
                .map(LogEntry::getProcessName)
                .distinct().count();
        logAggregate.setUniqueProcessesCount((int) uniqueProcesses);

        Map<String, Long> exceptionCounts = entries.stream()
                .map(e -> e.getMessage().split(" ")[0])
                .filter(e -> e.endsWith("Exception"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        String mostCommonException = exceptionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
        logAggregate.setMostCommonException(mostCommonException);

        Map<String, Long> threadCounts = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getThreadInfo, Collectors.counting()));
        String mostActiveThread = threadCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
        logAggregate.setMostActiveThread(mostActiveThread);

        return logAggregate;
    }

    private LogAggregateKey convertToHourAggregateKey(LogEntry logEntry) {
        LogAggregateKey key = new LogAggregateKey();
        key.setDate(logEntry.getDate());
        key.setHour(logEntry.getTime().getHour());
        key.setLogLevel(logEntry.getLogLevel());
        return key;
    }

    private LogAggregateKey convertToDayAggregateKey(LogEntry logEntry) {
        LogAggregateKey key = new LogAggregateKey();
        key.setDate(logEntry.getDate());
        key.setHour(null);
        key.setLogLevel(logEntry.getLogLevel());
        return key;
    }

    public List<LogAggregates> getLogAggregates(String granularity, String logLevel) {
        return logAggregateRepository.getAggregates(granularity, logLevel);
    }
}
