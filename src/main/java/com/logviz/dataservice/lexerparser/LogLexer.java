package com.logViz.dataservice.lexerparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogLexer {
    public List<Token> tokenize(String logLine) {
        List<Token> tokens = new ArrayList<>();

        // Define the pattern to match different parts
        Pattern pattern = Pattern.compile("^(\\w+ \\d+) (\\d+:\\d+:\\d+) ([\\w\\.-]+) (\\w+)\\[(\\d+)\\]: (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (\\w+)\\s+\\[(.+)\\]\\s+(.+)$");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            // Extract each token value and create a token
            tokens.add(new Token(TokenType.DATE, matcher.group(1)));           // Date
            tokens.add(new Token(TokenType.TIME, matcher.group(2)));           // Time
            tokens.add(new Token(TokenType.IP_ADDRESS, matcher.group(3)));     // IP Address
            tokens.add(new Token(TokenType.PROCESS_NAME, matcher.group(4)));   // Process Name
            tokens.add(new Token(TokenType.PROCESS_ID, matcher.group(5)));     // Process ID
            // Group 6 appears to be another date-time, which isn't in your token types. It's skipped here.
            tokens.add(new Token(TokenType.LOG_LEVEL, matcher.group(7)));      // Log Level
            tokens.add(new Token(TokenType.THREAD_INFO, matcher.group(8)));    // Thread Info
            tokens.add(new Token(TokenType.MESSAGE, matcher.group(9)));        // Message
            // EXCEPTION_NAME is not captured in your regex. If present, add another group to capture it.
        }

        return tokens;
    }
}
