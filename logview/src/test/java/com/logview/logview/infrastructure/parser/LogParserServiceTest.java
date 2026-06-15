package com.logview.logview.infrastructure.parser;

import com.logview.logview.domain.enums.Severity;
import com.logview.logview.domain.parser.LogParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class LogParserServiceTest {

    private LogParserService logParserService;

    @BeforeEach
    void setUp() {
        logParserService = new LogParserServiceImpl();
    }

    @Test
    void shouldDetectCriticalAndWarningCountsFromSampleLog() throws Exception {
        int criticalCount = 0;
        int warningCount = 0;
        int totalLines = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("sample-log.log").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                var result = logParserService.parseLine(totalLines, line);
                if (result.isPresent()) {
                    if (result.get().severity() == Severity.CRITICAL) {
                        criticalCount++;
                    } else if (result.get().severity() == Severity.WARNING) {
                        warningCount++;
                    }
                }
            }
        }

        assertThat(totalLines).isEqualTo(50);
        assertThat(criticalCount).isEqualTo(5);
        assertThat(warningCount).isEqualTo(10);
    }

    @Test
    void shouldDetectCriticalKeywordsCaseInsensitive() {
        var result = logParserService.parseLine(1, "error connection refused");

        assertThat(result).isPresent();
        assertThat(result.get().severity()).isEqualTo(Severity.CRITICAL);
    }

    @Test
    void shouldPrioritizeCriticalOverWarning() {
        var result = logParserService.parseLine(1, "ERROR with WARN in same line");

        assertThat(result).isPresent();
        assertThat(result.get().severity()).isEqualTo(Severity.CRITICAL);
    }

    @Test
    void shouldReturnEmptyForInfoLines() {
        var result = logParserService.parseLine(1, "2024-01-01 INFO Application started");

        assertThat(result).isEmpty();
    }
}
