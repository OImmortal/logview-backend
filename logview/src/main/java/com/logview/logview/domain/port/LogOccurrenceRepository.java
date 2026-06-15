package com.logview.logview.domain.port;

import com.logview.logview.domain.model.LogOccurrence;

import java.util.List;

public interface LogOccurrenceRepository {

    List<LogOccurrence> saveAll(List<LogOccurrence> occurrences);
}
