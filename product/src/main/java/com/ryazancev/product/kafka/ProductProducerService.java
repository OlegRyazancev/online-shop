package com.ryazancev.product.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducerService {

    private final KafkaTemplate<String, RegistrationRequestDTO>
            adminKafkaTemplate;
    private final KafkaTemplate<String, Long> reviewKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.review}")
    private String reviewTopic;


    public ProductProducerService(
            @Qualifier("adminKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDTO> adminKafkaTemplate,
            @Qualifier("reviewKafkaTemplate")
            KafkaTemplate<String, Long> reviewKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.reviewKafkaTemplate = reviewKafkaTemplate;
    }

    public void sendMessageToAdminTopic(RegistrationRequestDTO requestDTO) {
        try {
            adminKafkaTemplate.send(adminTopic, requestDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToReviewTopic(Long productId) {
        try {
            reviewKafkaTemplate.send(reviewTopic, productId);
            log.info("Message to {} was successfully send", reviewTopic);
        } catch (Exception e) {
            log.info("Message to {} was not send", reviewTopic);
            e.printStackTrace();

        }
    }

}
