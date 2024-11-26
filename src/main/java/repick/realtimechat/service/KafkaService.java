package repick.realtimechat.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaContainerManager containerManager;
    @PostConstruct
    public void initializeKafkaComponents() {
        try {
            containerManager.addContainer("testConnect");
            Thread.sleep(1000);
            kafkaProducerService.initializeProducer();
            System.out.println("Kafka components have been initialized.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Kafka components: " + e.getMessage());
        }
    }
}
