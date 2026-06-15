package com.logview.logview.application.usecase;

import com.logview.logview.application.dto.LogFileSummaryResponse;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.port.LogFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListLogFilesUseCase {

    private final LogFileRepository logFileRepository;

    public ListLogFilesUseCase(LogFileRepository logFileRepository) {
        this.logFileRepository = logFileRepository;
    }

    public List<LogFileSummaryResponse> execute(String tenantCnpj) {
        return logFileRepository.findAllByTenantCnpj(tenantCnpj).stream()
                .map(this::toSummary)
                .toList();
    }

    private LogFileSummaryResponse toSummary(LogFile logFile) {
        return new LogFileSummaryResponse(
                logFile.getId(),
                logFile.getOriginalFileName(),
                logFile.getStatus(),
                logFile.getStatusProcessamento(),
                logFile.getFileSizeBytes(),
                logFile.getCriticalCount(),
                logFile.getWarningCount(),
                logFile.getTotalOccurrences(),
                logFile.getCreatedAt(),
                logFile.getProcessedAt());
    }
}
