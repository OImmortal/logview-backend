package com.logview.logview.infrastructure.persistence.adapter;

import com.logview.logview.domain.model.LogFile;
import com.logview.logview.domain.port.LogFileRepository;
import com.logview.logview.infrastructure.persistence.SpringDataLogFileRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoLogFileRepositoryAdapter implements LogFileRepository {

    private final SpringDataLogFileRepository repository;

    public MongoLogFileRepositoryAdapter(SpringDataLogFileRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogFile save(LogFile logFile) {
        return repository.save(logFile);
    }

    @Override
    public Optional<LogFile> findByIdAndTenantCnpj(String id, String tenantCnpj) {
        return repository.findByIdAndTenantCnpj(id, tenantCnpj);
    }

    @Override
    public List<LogFile> findAllByTenantCnpj(String tenantCnpj) {
        return repository.findByTenantCnpjOrderByCreatedAtDesc(tenantCnpj);
    }
}
