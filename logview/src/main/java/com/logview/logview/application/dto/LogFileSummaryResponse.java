package com.logview.logview.application.dto;

import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.enums.StatusProcessamento;

import java.time.Instant;

public record LogFileSummaryResponse(
        String fileId,
        String originalFileName,
        LogFileStatus status,
        StatusProcessamento statusProcessamento,
        long fileSizeBytes,
        int criticalCount,
        int warningCount,
        int totalOccurrences,
        Instant createdAt,
        Instant processedAt) {
}
