package com.logViz.dataservice.lexerparser;

import com.logViz.dataservice.model.LogEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogParser {
    private final List<Token> tokens;
    private int currentTokenIndex = 0;

    public LogParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token nextToken() {
        return tokens.get(currentTokenIndex++);
    }

    public LogEntry parse() {
        LogEntry.LogEntryBuilder builder = LogEntry.builder();

        while (currentTokenIndex < tokens.size()) {
            Token token = nextToken();
            switch (token.getType()) {
                case DATE:
                    // Assuming the date format is like "MMM dd", e.g., "Sep 11"
                    builder.date(LocalDate.parse(token.getValue() + " " + LocalDate.now().getYear(),
                            DateTimeFormatter.ofPattern("MMM dd yyyy")));
                    break;
                case TIME:
                    builder.time(LocalTime.parse(token.getValue(), DateTimeFormatter.ofPattern("HH:mm:ss")));
                    break;
                case IP_ADDRESS:
                    builder.ip(token.getValue());
                    break;
                case PROCESS_NAME:
                    builder.processName(token.getValue());
                    break;
                case PROCESS_ID:
                    builder.processId(Integer.parseInt(token.getValue()));
                    break;
                case MESSAGE:
                    builder.message(token.getValue());
                    break;
                case LOG_LEVEL:
                    builder.logLevel(token.getValue());
                    break;
                case THREAD_INFO:
                    builder.threadInfo(token.getValue());
                    break;
                case EXCEPTION_NAME:
                    builder.exceptionName(token.getValue());
                    break;
                // Add cases for any additional token types
            }
        }

        return builder.build();
    }
}
