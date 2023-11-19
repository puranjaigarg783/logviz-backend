package com.logViz.dataservice.lexerparser;

import com.logViz.dataservice.model.LogEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogParser {
    //    private final List<Token> tokens;
//    private int currentTokenIndex = 0;
//
//    public LogParser(List<Token> tokens) {
//        this.tokens = tokens;
//    }
//
//    private Token nextToken() {
//        return tokens.get(currentTokenIndex++);
//    }
//
//    public LogEntry parse() {
//        LogEntry.LogEntryBuilder builder = LogEntry.builder();
//
//        while (currentTokenIndex < tokens.size()) {
//            Token token = nextToken();
//            switch (token.getType()) {
//                case DATE:
//                    // Assuming the date format is like "MMM dd", e.g., "Sep 11"
//                    builder.date(LocalDate.parse(token.getValue() + " " + LocalDate.now().getYear(),
//                            DateTimeFormatter.ofPattern("MMM dd yyyy")));
//                    break;
//                case TIME:
//                    builder.time(LocalTime.parse(token.getValue(), DateTimeFormatter.ofPattern("HH:mm:ss")));
//                    break;
//                case IP_ADDRESS:
//                    builder.ip(token.getValue());
//                    break;
//                case PROCESS_NAME:
//                    builder.processName(token.getValue());
//                    break;
//                case PROCESS_ID:
//                    builder.processId(Integer.parseInt(token.getValue()));
//                    break;
//                case MESSAGE:
//                    builder.message(token.getValue());
//                    break;
//                case LOG_LEVEL:
//                    builder.logLevel(token.getValue());
//                    break;
//                case THREAD_INFO:
//                    builder.threadInfo(token.getValue());
//                    break;
//                case EXCEPTION_NAME:
//                    builder.exceptionName(token.getValue());
//                    break;
//                // Add cases for any additional token types
//            }
//        }
//
//        return builder.build();
//    }
    private final List<Token> tokens;
    private int currentTokenIndex = 0;

    public LogParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token nextToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex++);
        }
        return null; // Indicate end of tokens
    }

    private Token peekToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        }
        return null; // Indicate end of tokens
    }

    public LogEntry parse() {
        return parseLogEntry();
    }

    private LogEntry parseLogEntry() {
        LogEntry.LogEntryBuilder builder = LogEntry.builder();
        Token token;

        token = nextToken(); // TIMESTAMP
        parseTimestamp(token, builder);

        token = nextToken(); // LOG_LEVEL
        parseLogLevel(token, builder);

        parseProcessInfo(builder); // PROCESS_NAME and PROCESS_ID

        token = peekToken();
        if (token != null && token.getType() == TokenType.IP_ADDRESS) {
            nextToken(); // Consume IP_ADDRESS token
            parseIpInfo(token, builder);
        }

        token = nextToken(); // MESSAGE
        parseMessage(token, builder);

        token = peekToken();
        if (token != null && token.getType() == TokenType.THREAD_INFO) {
            nextToken(); // Consume THREAD_INFO token
            parseThreadInfo(token, builder);
        }

        token = peekToken();
        if (token != null && token.getType() == TokenType.EXCEPTION_NAME) {
            nextToken(); // Consume EXCEPTION_NAME token
            parseExceptionInfo(token, builder);
        }

        return builder.build();
    }

    private void parseTimestamp(Token token, LogEntry.LogEntryBuilder builder) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] dateTime = token.getValue().split(" ");
        builder.date(LocalDate.parse(dateTime[0], dateFormatter));
        builder.time(LocalTime.parse(dateTime[1], dateFormatter));
    }

    private void parseLogLevel(Token token, LogEntry.LogEntryBuilder builder) {
        builder.logLevel(token.getValue().replace(":", ""));
    }

    private void parseProcessInfo(LogEntry.LogEntryBuilder builder) {
        builder.processName(nextToken().getValue()); // PROCESS_NAME
        builder.processId(Integer.parseInt(nextToken().getValue())); // PROCESS_ID
    }

    private void parseIpInfo(Token token, LogEntry.LogEntryBuilder builder) {
        builder.ip(token.getValue());
    }

    private void parseMessage(Token token, LogEntry.LogEntryBuilder builder) {
        builder.message(token.getValue());
    }

    private void parseThreadInfo(Token token, LogEntry.LogEntryBuilder builder) {
        builder.threadInfo(token.getValue());
    }

    private void parseExceptionInfo(Token token, LogEntry.LogEntryBuilder builder) {
        builder.exceptionName(token.getValue());
    }
}

