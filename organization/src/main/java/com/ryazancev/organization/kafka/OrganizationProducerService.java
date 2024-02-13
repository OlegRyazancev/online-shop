package com.ryazancev.organization.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationProducerService {

    private final KafkaTemplate<String, RegistrationRequestDto>
            adminKafkaTemplate;

    private final KafkaTemplate<String, Long> productKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.product}")
    private String productTopic;

    public OrganizationProducerService(
            @Qualifier("adminKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> adminKafkaTemplate,
            @Qualifier("productKafkaTemplate")
            KafkaTemplate<String, Long> productKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.productKafkaTemplate = productKafkaTemplate;
    }

    public void sendMessageToAdminTopic(RegistrationRequestDto requestDTO) {
        try {
            adminKafkaTemplate.send(adminTopic, requestDTO);
            log.info("Message to {} was successfully send", adminTopic);
        } catch (Exception e) {
            log.info("Message to {} was not send", adminTopic);
            e.printStackTrace();
        }
    }

    public void sendMessageToProductTopic(Long organizationId) {
        try {
            productKafkaTemplate.send(productTopic, organizationId);
            log.info("Message to {} was successfully send", productTopic);
        } catch (Exception e) {
            log.info("Message to {} was not send", productTopic);
            e.printStackTrace();

        }
    }
}
