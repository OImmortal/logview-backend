package com.logview.logview.infrastructure.parser;

import com.logview.logview.domain.enums.Severity;
import com.logview.logview.domain.parser.LogParserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogParserServiceImpl implements LogParserService {

    private static final List<String> CRITICAL_KEYWORDS = List.of(
            "ERROR", "ERRO", "FATAL", "EXCEPTION", "THROW", "THROWS", "THROWABLE",
            "STACKTRACE", "STACK TRACE", "UNHANDLED", "FAILED", "FAILURE", "CRASH",
            "SEGMENTATION FAULT", "OUTOFMEMORY", "OUT OF MEMORY", "NULLPOINTEREXCEPTION",
            "SQL EXCEPTION", "CONNECTION REFUSED", "EXIT CODE 1", "EXIT CODE 137", "EXIT CODE 255"
    );

    private static final List<String> WARNING_KEYWORDS = List.of(
            "WARN", "WARNING", "DEPRECATED", "RETRYING", "TIMEOUT", "SLOW QUERY",
            "HIGH LATENCY", "UNSTABLE", "MEMORY USAGE HIGH", "CPU USAGE HIGH"
    );

    private static final int MAX_MESSAGE_LENGTH = 2048;

    @Override
    public Optional<ParseResult> parseLine(int lineNumber, String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }

        String upperLine = line.toUpperCase();

        for (String keyword : CRITICAL_KEYWORDS) {
            if (upperLine.contains(keyword)) {
                return Optional.of(new ParseResult(lineNumber, truncate(line), Severity.CRITICAL));
            }
        }

        for (String keyword : WARNING_KEYWORDS) {
            if (upperLine.contains(keyword)) {
                return Optional.of(new ParseResult(lineNumber, truncate(line), Severity.WARNING));
            }
        }

        return Optional.empty();
    }

    private String truncate(String message) {
        if (message.length() <= MAX_MESSAGE_LENGTH) {
            return message;
        }
        return message.substring(0, MAX_MESSAGE_LENGTH);
    }
}
