package repick.realtimechat.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@RequiredArgsConstructor
public class KafkaContainerManager {

    private final Map<String, ConcurrentMessageListenerContainer<String, String>> containers = new HashMap<>();

    private final DynamicKafkaListenerService listenerService;

    public void addContainer(String topic) {
        if (!containers.containsKey(topic)) {
            ConcurrentMessageListenerContainer<String, String> container =
                    listenerService.createContainer(topic);
            containers.put(topic, container);
        }
    }

    public void removeContainer(String topic) {
        ConcurrentMessageListenerContainer<String, String> container = containers.remove(topic);
        if (container != null) {
            container.stop();
        }
    }
}
