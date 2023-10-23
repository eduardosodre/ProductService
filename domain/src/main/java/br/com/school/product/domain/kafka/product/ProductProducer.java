package br.com.school.product.domain.kafka.product;

import static br.com.school.product.domain.kafka.KafkaTopics.PRODUCT_TOPIC;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendProduct(ProductEvent productEvent) {
        try {
            String productJson = objectMapper.writeValueAsString(productEvent);
            kafkaTemplate.send(PRODUCT_TOPIC, productEvent.id(), productJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
