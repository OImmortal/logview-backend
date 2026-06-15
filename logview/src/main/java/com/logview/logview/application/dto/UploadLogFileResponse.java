package com.logview.logview.application.dto;

import com.logview.logview.domain.enums.LogFileStatus;

public record UploadLogFileResponse(String fileId, LogFileStatus status) {
}
