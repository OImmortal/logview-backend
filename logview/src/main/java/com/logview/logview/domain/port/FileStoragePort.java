package com.logview.logview.domain.port;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileStoragePort {

    Path save(String tenantCnpj, String fileId, InputStream content) throws IOException;

    byte[] read(String storagePath) throws IOException;
}
