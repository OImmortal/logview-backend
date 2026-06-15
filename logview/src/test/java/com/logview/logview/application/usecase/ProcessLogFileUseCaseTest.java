package com.logview.logview.application.usecase;

import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.exception.LogFileNotFoundException;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.model.Notification;
import com.logview.logview.domain.port.FileStoragePort;
import com.logview.logview.domain.port.LogFileRepository;
import com.logview.logview.domain.port.LogOccurrenceRepository;
import com.logview.logview.domain.port.NotificationService;
import com.logview.logview.infrastructure.parser.LogParserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessLogFileUseCaseTest {

    @Mock
    private LogFileRepository logFileRepository;

    @Mock
    private LogOccurrenceRepository logOccurrenceRepository;

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private NotificationService notificationService;

    private ProcessLogFileUseCase processLogFileUseCase;

    @BeforeEach
    void setUp() {
        processLogFileUseCase = new ProcessLogFileUseCase(
                logFileRepository,
                logOccurrenceRepository,
                fileStoragePort,
                new LogParserServiceImpl(),
                notificationService);
    }

    @Test
    void shouldProcessSampleLogWithCorrectCounts() throws Exception {
        String fileId = "test-file-id";
        String tenantCnpj = "12345678000199";
        byte[] content = new ClassPathResource("sample-log.log").getContentAsByteArray();

        LogFile logFile = LogFile.builder()
                .id(fileId)
                .tenantCnpj(tenantCnpj)
                .storagePath("/tmp/sample.log")
                .build();

        when(logFileRepository.findByIdAndTenantCnpj(fileId, tenantCnpj)).thenReturn(Optional.of(logFile));
        when(fileStoragePort.read("/tmp/sample.log")).thenReturn(content);
        when(logOccurrenceRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logFileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationService.notifyProcessingComplete(any(), any(), any(Integer.class), any(Integer.class)))
                .thenReturn(Notification.builder().mensagem("summary").build());

        var response = processLogFileUseCase.execute(tenantCnpj, fileId);

        assertThat(response.status()).isEqualTo(LogFileStatus.PROCESSED);
        assertThat(response.criticalCount()).isEqualTo(5);
        assertThat(response.warningCount()).isEqualTo(10);
        assertThat(response.totalLines()).isEqualTo(50);

        ArgumentCaptor<List> occurrencesCaptor = ArgumentCaptor.forClass(List.class);
        verify(logOccurrenceRepository).saveAll(occurrencesCaptor.capture());
        assertThat(occurrencesCaptor.getValue()).hasSize(15);

        verify(notificationService).notifyProcessingComplete(tenantCnpj, fileId, 5, 10);
    }

    @Test
    void shouldThrowWhenFileNotFound() {
        when(logFileRepository.findByIdAndTenantCnpj("missing", "12345678000199"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> processLogFileUseCase.execute("12345678000199", "missing"))
                .isInstanceOf(LogFileNotFoundException.class);
    }
}
