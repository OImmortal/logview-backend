package com.logview.logview.application.usecase;

import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.exception.InvalidFileException;
import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.port.FileStoragePort;
import com.logview.logview.domain.port.LogFileRepository;
import com.logview.logview.infrastructure.config.StorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadLogFileUseCaseTest {

    @Mock
    private LogFileRepository logFileRepository;

    @Mock
    private FileStoragePort fileStoragePort;

    @TempDir
    Path tempDir;

    private UploadLogFileUseCase uploadLogFileUseCase;
    private StorageProperties storageProperties;

    @BeforeEach
    void setUp() {
        storageProperties = new StorageProperties();
        storageProperties.getUpload().setMaxSize(10_485_760L);
        storageProperties.getStorage().setTempDir(tempDir.toString());
        uploadLogFileUseCase = new UploadLogFileUseCase(logFileRepository, fileStoragePort, storageProperties);
    }

    @Test
    void shouldUploadValidLogFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "app.log", "text/plain", "line1\nline2".getBytes());
        when(fileStoragePort.save(eq("12345678000199"), any(), any()))
                .thenReturn(tempDir.resolve("file.log"));
        when(logFileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = uploadLogFileUseCase.execute("12345678000199", file);

        assertThat(response.fileId()).isNotBlank();
        assertThat(response.status()).isEqualTo(LogFileStatus.UPLOADED);

        ArgumentCaptor<LogFile> captor = ArgumentCaptor.forClass(LogFile.class);
        verify(logFileRepository).save(captor.capture());
        assertThat(captor.getValue().getTenantCnpj()).isEqualTo("12345678000199");
        assertThat(captor.getValue().getOriginalFileName()).isEqualTo("app.log");
    }

    @Test
    void shouldRejectNonLogExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "app.txt", "text/plain", "content".getBytes());

        assertThatThrownBy(() -> uploadLogFileUseCase.execute("12345678000199", file))
                .isInstanceOf(InvalidFileException.class)
                .hasMessageContaining(".log");
    }

    @Test
    void shouldRejectOversizedFile() {
        storageProperties.getUpload().setMaxSize(10L);
        MockMultipartFile file = new MockMultipartFile(
                "file", "app.log", "text/plain", "this is too large".getBytes());

        assertThatThrownBy(() -> uploadLogFileUseCase.execute("12345678000199", file))
                .isInstanceOf(InvalidFileException.class)
                .hasMessageContaining("maximum");
    }

    @Test
    void shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "app.log", "text/plain", new byte[0]);

        assertThatThrownBy(() -> uploadLogFileUseCase.execute("12345678000199", file))
                .isInstanceOf(InvalidFileException.class)
                .hasMessageContaining("required");
    }
}
