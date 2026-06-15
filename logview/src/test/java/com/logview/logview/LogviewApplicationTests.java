package com.logview.logview;

import com.logview.logview.infrastructure.persistence.SpringDataLogFileRepository;
import com.logview.logview.infrastructure.persistence.SpringDataLogOccurrenceRepository;
import com.logview.logview.infrastructure.persistence.SpringDataNotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class LogviewApplicationTests {

	@MockitoBean
	private SpringDataLogFileRepository springDataLogFileRepository;

	@MockitoBean
	private SpringDataLogOccurrenceRepository springDataLogOccurrenceRepository;

	@MockitoBean
	private SpringDataNotificationRepository springDataNotificationRepository;

	@Test
	void contextLoads() {
	}

}
