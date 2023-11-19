package com.logViz.dataservice.util;

import com.logViz.dataservice.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogParserTest {

    @Test
    public void testParseLogEntry() {
        String logLine = "Sep 11 07:24:58 ip-172-31-0-178.ap-south-1.compute.internal java[2162368]: 2023-09-11 07:24:58,199 WARN  [http-nio-10100-exec-4] org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver: Failure in @ExceptionHandler com.terra.profileservice.config.RestExceptionHandler#handleUnknownError(Exception, HttpServletRequest)";

        LogEntry logEntry = LogParser.parseLogEntry(logLine);

        assertNotNull(logEntry);
        assertEquals(LocalDate.of(2023, 9, 11), logEntry.getDate());
        assertEquals(LocalTime.of(7, 24, 58), logEntry.getTime());
        assertEquals("ip-172-31-0-178.ap-south-1.compute.internal", logEntry.getIp());
        assertEquals("java", logEntry.getProcessName());
        assertEquals(2162368, logEntry.getProcessId());
        assertEquals("WARN", logEntry.getLogLevel());
        assertEquals("[http-nio-10100-exec-4]", logEntry.getThreadInfo());
        assertEquals("org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver: Failure in @ExceptionHandler com.terra.profileservice.config.RestExceptionHandler#handleUnknownError(Exception, HttpServletRequest)", logEntry.getMessage());
    }
}
