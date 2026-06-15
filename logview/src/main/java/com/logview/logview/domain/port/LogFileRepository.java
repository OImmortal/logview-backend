package com.logview.logview.domain.port;

import com.logview.logview.domain.model.LogFile;

import java.util.List;
import java.util.Optional;

public interface LogFileRepository {

    LogFile save(LogFile logFile);

    Optional<LogFile> findByIdAndTenantCnpj(String id, String tenantCnpj);

    List<LogFile> findAllByTenantCnpj(String tenantCnpj);
}
