package com.logview.logview.infrastructure.notification;

import com.logview.logview.domain.enums.CanalNotificacao;
import com.logview.logview.domain.enums.StatusNotificacao;
import com.logview.logview.domain.model.Notification;
import com.logview.logview.domain.port.NotificationRepository;
import com.logview.logview.domain.port.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class InMemoryNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryNotificationService.class);

    private final NotificationRepository notificationRepository;

    public InMemoryNotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification notifyProcessingComplete(String tenantCnpj, String fileId,
                                                 int criticalCount, int warningCount) {
        String summary = buildSummary(criticalCount, warningCount);
        Instant now = Instant.now();

        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .tenantCnpj(tenantCnpj)
                .fileId(fileId)
                .canal(CanalNotificacao.IN_MEMORY)
                .status(StatusNotificacao.ENVIADA)
                .titulo("Processamento de log concluído")
                .mensagem(summary)
                .quantidadeErros(criticalCount)
                .quantidadeAlertas(warningCount)
                .createdAt(now)
                .enviadaEm(now)
                .build();

        log.info("Notification for tenant {} file {}: {}", tenantCnpj, fileId, summary);

        return notificationRepository.save(notification);
    }

    public String buildSummary(int criticalCount, int warningCount) {
        return String.format(
                "Arquivo processado.%n%d erros críticos encontrados.%n%d alertas encontrados.",
                criticalCount, warningCount);
    }
}
