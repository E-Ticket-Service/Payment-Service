package abb.tech.payment_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
class PaymentServiceApplicationTests {

	@MockitoBean
	KafkaTemplate<String, Object> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
