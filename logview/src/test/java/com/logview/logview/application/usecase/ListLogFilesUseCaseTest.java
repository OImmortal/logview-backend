package com.logview.logview.application.usecase;

import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.enums.StatusProcessamento;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.port.LogFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListLogFilesUseCaseTest {

    @Mock
    private LogFileRepository logFileRepository;

    private ListLogFilesUseCase listLogFilesUseCase;

    @BeforeEach
    void setUp() {
        listLogFilesUseCase = new ListLogFilesUseCase(logFileRepository);
    }

    @Test
    void shouldListFilesByTenantCnpj() {
        String tenantCnpj = "12345678000199";
        Instant now = Instant.parse("2024-01-01T10:00:00Z");

        LogFile file = LogFile.builder()
                .id("file-1")
                .tenantCnpj(tenantCnpj)
                .originalFileName("app.log")
                .status(LogFileStatus.PROCESSED)
                .statusProcessamento(StatusProcessamento.PROCESSADO)
                .fileSizeBytes(1024)
                .criticalCount(5)
                .warningCount(10)
                .totalOccurrences(15)
                .createdAt(now)
                .processedAt(now)
                .build();

        when(logFileRepository.findAllByTenantCnpj(tenantCnpj)).thenReturn(List.of(file));

        var result = listLogFilesUseCase.execute(tenantCnpj);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).fileId()).isEqualTo("file-1");
        assertThat(result.get(0).originalFileName()).isEqualTo("app.log");
        assertThat(result.get(0).status()).isEqualTo(LogFileStatus.PROCESSED);
        assertThat(result.get(0).criticalCount()).isEqualTo(5);
        assertThat(result.get(0).warningCount()).isEqualTo(10);
    }

    @Test
    void shouldReturnEmptyListWhenNoFiles() {
        when(logFileRepository.findAllByTenantCnpj("12345678000199")).thenReturn(List.of());

        var result = listLogFilesUseCase.execute("12345678000199");

        assertThat(result).isEmpty();
    }
}
