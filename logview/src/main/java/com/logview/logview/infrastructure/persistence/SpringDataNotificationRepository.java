package com.logview.logview.infrastructure.persistence;

import com.logview.logview.domain.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataNotificationRepository extends MongoRepository<Notification, String> {
}
