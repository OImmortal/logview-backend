package com.logview.logview.application.usecase;

import com.logview.logview.application.dto.ProcessLogFileResponse;
import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.enums.Severity;
import com.logview.logview.domain.enums.StatusProcessamento;
import com.logview.logview.domain.exception.LogFileNotFoundException;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.model.LogOccurrence;
import com.logview.logview.domain.parser.LogParserService;
import com.logview.logview.domain.port.FileStoragePort;
import com.logview.logview.domain.port.LogFileRepository;
import com.logview.logview.domain.port.LogOccurrenceRepository;
import com.logview.logview.domain.port.NotificationService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProcessLogFileUseCase {

    private final LogFileRepository logFileRepository;
    private final LogOccurrenceRepository logOccurrenceRepository;
    private final FileStoragePort fileStoragePort;
    private final LogParserService logParserService;
    private final NotificationService notificationService;

    public ProcessLogFileUseCase(LogFileRepository logFileRepository,
                                 LogOccurrenceRepository logOccurrenceRepository,
                                 FileStoragePort fileStoragePort,
                                 LogParserService logParserService,
                                 NotificationService notificationService) {
        this.logFileRepository = logFileRepository;
        this.logOccurrenceRepository = logOccurrenceRepository;
        this.fileStoragePort = fileStoragePort;
        this.logParserService = logParserService;
        this.notificationService = notificationService;
    }

    public ProcessLogFileResponse execute(String tenantCnpj, String fileId) throws IOException {
        LogFile logFile = logFileRepository.findByIdAndTenantCnpj(fileId, tenantCnpj)
                .orElseThrow(() -> new LogFileNotFoundException(fileId));

        logFile.setStatus(LogFileStatus.PROCESSING);
        logFileRepository.save(logFile);

        try {
            byte[] content = fileStoragePort.read(logFile.getStoragePath());
            int criticalCount = 0;
            int warningCount = 0;
            int totalLines = 0;
            List<LogOccurrence> occurrences = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;
                    var parseResult = logParserService.parseLine(totalLines, line);
                    if (parseResult.isPresent()) {
                        var result = parseResult.get();
                        if (result.severity() == Severity.CRITICAL) {
                            criticalCount++;
                        } else if (result.severity() == Severity.WARNING) {
                            warningCount++;
                        }

                        occurrences.add(LogOccurrence.builder()
                                .id(UUID.randomUUID().toString())
                                .fileId(fileId)
                                .tenantCnpj(tenantCnpj)
                                .lineNumber(result.lineNumber())
                                .message(result.message())
                                .severity(result.severity())
                                .detectedAt(Instant.now())
                                .build());
                    }
                }
            }

            if (!occurrences.isEmpty()) {
                logOccurrenceRepository.saveAll(occurrences);
            }

            logFile.setStatus(LogFileStatus.PROCESSED);
            logFile.setStatusProcessamento(StatusProcessamento.PROCESSADO);
            logFile.setTotalOccurrences(occurrences.size());
            logFile.setCriticalCount(criticalCount);
            logFile.setWarningCount(warningCount);
            logFile.setProcessedAt(Instant.now());
            logFileRepository.save(logFile);

            var notification = notificationService.notifyProcessingComplete(
                    tenantCnpj, fileId, criticalCount, warningCount);
            String summary = notification.getMensagem();

            return new ProcessLogFileResponse(
                    fileId,
                    LogFileStatus.PROCESSED,
                    criticalCount,
                    warningCount,
                    totalLines,
                    summary);
        } catch (Exception ex) {
            logFile.setStatus(LogFileStatus.FAILED);
            logFile.setStatusProcessamento(StatusProcessamento.FALHA);
            logFile.setMensagemFalha(ex.getMessage());
            logFileRepository.save(logFile);
            throw ex;
        }
    }
}
