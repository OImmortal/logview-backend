package com.logview.logview.domain.port;

import com.logview.logview.domain.model.Notification;

public interface NotificationRepository {

    Notification save(Notification notification);
}
