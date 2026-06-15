package com.logview.logview.application.usecase;

import com.logview.logview.application.dto.UploadLogFileResponse;
import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.enums.StatusProcessamento;
import com.logview.logview.domain.exception.InvalidFileException;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.port.FileStoragePort;
import com.logview.logview.domain.port.LogFileRepository;
import com.logview.logview.infrastructure.config.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class UploadLogFileUseCase {

    private final LogFileRepository logFileRepository;
    private final FileStoragePort fileStoragePort;
    private final StorageProperties storageProperties;

    public UploadLogFileUseCase(LogFileRepository logFileRepository,
                                FileStoragePort fileStoragePort,
                                StorageProperties storageProperties) {
        this.logFileRepository = logFileRepository;
        this.fileStoragePort = fileStoragePort;
        this.storageProperties = storageProperties;
    }

    public UploadLogFileResponse execute(String tenantCnpj, MultipartFile file) throws IOException {
        validateFile(file);

        String fileId = UUID.randomUUID().toString();
        var savedPath = fileStoragePort.save(tenantCnpj, fileId, file.getInputStream());

        LogFile logFile = LogFile.builder()
                .id(fileId)
                .tenantCnpj(tenantCnpj)
                .originalFileName(file.getOriginalFilename())
                .storagePath(savedPath.toString())
                .fileSizeBytes(file.getSize())
                .status(LogFileStatus.UPLOADED)
                .statusProcessamento(StatusProcessamento.PENDENTE)
                .createdAt(Instant.now())
                .build();

        logFileRepository.save(logFile);

        return new UploadLogFileResponse(fileId, LogFileStatus.UPLOADED);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is required");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".log")) {
            throw new InvalidFileException("Only .log files are allowed");
        }

        if (file.getSize() > storageProperties.getUpload().getMaxSize()) {
            throw new InvalidFileException("File exceeds maximum allowed size");
        }
    }
}
