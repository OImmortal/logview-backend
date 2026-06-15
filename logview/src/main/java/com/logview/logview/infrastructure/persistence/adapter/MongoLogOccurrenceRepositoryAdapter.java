package com.logview.logview.infrastructure.persistence.adapter;

import com.logview.logview.domain.model.LogOccurrence;
import com.logview.logview.domain.port.LogOccurrenceRepository;
import com.logview.logview.infrastructure.persistence.SpringDataLogOccurrenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoLogOccurrenceRepositoryAdapter implements LogOccurrenceRepository {

    private final SpringDataLogOccurrenceRepository repository;

    public MongoLogOccurrenceRepositoryAdapter(SpringDataLogOccurrenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LogOccurrence> saveAll(List<LogOccurrence> occurrences) {
        return repository.saveAll(occurrences);
    }
}
