package com.logViz.dataservice.util;

import com.logViz.dataservice.model.LogEntry;
import com.logViz.dataservice.model.LogContext;
import com.logViz.dataservice.model.LogAggregates;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    public static LogEntry parseLogEntry(String logLine) {
        String pattern = "^(\\w+ \\d+) (\\d+:\\d+:\\d+) ([\\w\\.-]+) (\\w+)\\[(\\d+)\\]: (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (\\w+)\\s+\\[(.+)\\]\\s+(.+)$";
        Matcher matcher = Pattern.compile(pattern).matcher(logLine);


        if (!matcher.find()) {
            return null;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        LocalDate date = LocalDate.parse(matcher.group(1) + " " + LocalDate.now().getYear(), dateFormatter);
        LocalTime time = LocalTime.parse(matcher.group(2));

        LogEntry logEntry = LogEntry.builder()
                .date(date)
                .time(time)
                .ip(matcher.group(3))
                .processName(matcher.group(4))
                .processId(Integer.parseInt(matcher.group(5)))
                .message(matcher.group(9))
                .logLevel(matcher.group(7))
                .threadInfo("[" + matcher.group(8) + "]")
                .build();

        return logEntry;
    }

    public static LogContext parseLogContext(String logLine) {
        LogContext logContext = new LogContext();
        // Parse logLine to populate logContext
        return logContext;
    }

    public static LogAggregates parseLogAggregates(String logLine) {
        LogAggregates logAggregates = new LogAggregates();
        // Parse logLine to populate logAggregates
        return logAggregates;
    }
}
