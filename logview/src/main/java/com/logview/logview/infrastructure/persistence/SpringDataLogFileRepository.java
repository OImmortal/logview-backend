package com.logview.logview.infrastructure.persistence;

import com.logview.logview.domain.model.LogFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataLogFileRepository extends MongoRepository<LogFile, String> {

    Optional<LogFile> findByIdAndTenantCnpj(String id, String tenantCnpj);

    List<LogFile> findByTenantCnpjOrderByCreatedAtDesc(String tenantCnpj);
}
