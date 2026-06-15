package com.logview.logview.application.dto;

import com.logview.logview.domain.enums.LogFileStatus;

public record ProcessLogFileResponse(
        String fileId,
        LogFileStatus status,
        int criticalCount,
        int warningCount,
        int totalLines,
        String summary) {
}
