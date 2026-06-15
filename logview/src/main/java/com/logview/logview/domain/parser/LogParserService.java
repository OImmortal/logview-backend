package com.logview.logview.domain.parser;

import com.logview.logview.domain.enums.Severity;

import java.util.Optional;

public interface LogParserService {

    Optional<ParseResult> parseLine(int lineNumber, String line);

    record ParseResult(int lineNumber, String message, Severity severity) {
    }
}
