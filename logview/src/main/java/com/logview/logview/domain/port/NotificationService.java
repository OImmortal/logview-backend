package com.logview.logview.domain.port;

import com.logview.logview.domain.model.Notification;

public interface NotificationService {

    Notification notifyProcessingComplete(String tenantCnpj, String fileId,
                                          int criticalCount, int warningCount);
}
