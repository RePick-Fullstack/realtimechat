package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Value("${kafka.consumer.group-id:default-group}")
    private String groupId;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Producer Message Sent : " + message);
    }

    public void initializeProducer() {
        kafkaTemplate.send("connectTest", groupId);
    }
}

