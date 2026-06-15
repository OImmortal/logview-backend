package com.logview.logview.infrastructure.persistence;

import com.logview.logview.domain.model.LogOccurrence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataLogOccurrenceRepository extends MongoRepository<LogOccurrence, String> {
}
