package com.logview.logview.infrastructure.notification;

import com.logview.logview.domain.enums.CanalNotificacao;
import com.logview.logview.domain.enums.StatusNotificacao;
import com.logview.logview.domain.model.Notification;
import com.logview.logview.domain.port.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    private InMemoryNotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new InMemoryNotificationService(notificationRepository);
    }

    @Test
    void shouldBuildCorrectSummary() {
        String summary = notificationService.buildSummary(5, 10);

        assertThat(summary).contains("Arquivo processado");
        assertThat(summary).contains("5 erros críticos encontrados");
        assertThat(summary).contains("10 alertas encontrados");
    }

    @Test
    void shouldPersistNotificationWithCorrectCounts() {
        when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Notification result = notificationService.notifyProcessingComplete(
                "12345678000199", "file-123", 5, 10);

        assertThat(result.getTenantCnpj()).isEqualTo("12345678000199");
        assertThat(result.getFileId()).isEqualTo("file-123");
        assertThat(result.getQuantidadeErros()).isEqualTo(5);
        assertThat(result.getQuantidadeAlertas()).isEqualTo(10);
        assertThat(result.getCanal()).isEqualTo(CanalNotificacao.IN_MEMORY);
        assertThat(result.getStatus()).isEqualTo(StatusNotificacao.ENVIADA);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());
        assertThat(captor.getValue().getMensagem()).contains("5 erros críticos encontrados");
    }
}
