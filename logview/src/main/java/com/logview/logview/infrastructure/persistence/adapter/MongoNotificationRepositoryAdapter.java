package com.logview.logview.infrastructure.persistence.adapter;

import com.logview.logview.domain.model.Notification;
import com.logview.logview.domain.port.NotificationRepository;
import com.logview.logview.infrastructure.persistence.SpringDataNotificationRepository;
import org.springframework.stereotype.Component;

@Component
public class MongoNotificationRepositoryAdapter implements NotificationRepository {

    private final SpringDataNotificationRepository repository;

    public MongoNotificationRepositoryAdapter(SpringDataNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {
        return repository.save(notification);
    }
}
