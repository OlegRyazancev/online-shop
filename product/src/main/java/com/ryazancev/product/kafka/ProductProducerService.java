package com.ryazancev.product.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducerService {

    private final KafkaTemplate<String, RegistrationRequestDto>
            adminKafkaTemplate;
    private final KafkaTemplate<String, Long> reviewKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.review}")
    private String reviewTopic;


    public ProductProducerService(
            @Qualifier("adminKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> adminKafkaTemplate,
            @Qualifier("reviewKafkaTemplate")
            KafkaTemplate<String, Long> reviewKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.reviewKafkaTemplate = reviewKafkaTemplate;
    }

    public void sendMessageToAdminTopic(RegistrationRequestDto requestDto) {
        try {
            adminKafkaTemplate.send(adminTopic, requestDto);
            log.info("Message to {} was successfully send", adminTopic);
        } catch (Exception e) {
            log.info("Message to {} was not send", adminTopic);
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
