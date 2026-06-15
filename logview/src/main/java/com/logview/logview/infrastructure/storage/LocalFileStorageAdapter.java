package com.logview.logview.infrastructure.storage;

import com.logview.logview.domain.port.FileStoragePort;
import com.logview.logview.infrastructure.config.StorageProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    private final StorageProperties storageProperties;

    public LocalFileStorageAdapter(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public Path save(String tenantCnpj, String fileId, InputStream content) throws IOException {
        Path tenantDir = Path.of(storageProperties.getStorage().getTempDir(), tenantCnpj);
        Files.createDirectories(tenantDir);
        Path filePath = tenantDir.resolve(fileId + ".log");
        Files.copy(content, filePath);
        return filePath;
    }

    @Override
    public byte[] read(String storagePath) throws IOException {
        return Files.readAllBytes(Path.of(storagePath));
    }
}
