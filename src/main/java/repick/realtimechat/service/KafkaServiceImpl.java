package repick.realtimechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentKafkaListenerContainerFactory<String, String> factory;
    private final Map<String, ConcurrentMessageListenerContainer<String, String>> containers = new HashMap<>();

    @Value("${kafka.consumer.group-id:default-group}")
    private String groupId;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Producer MessageDTO Sent : " + message);
    }

    @Override
    public void addContainer(String topic) {
        if (!containers.containsKey(topic)) {
            ConcurrentMessageListenerContainer<String, String> container =
                    createContainer(topic);
            containers.put(topic, container);
        }
    }

    @Override
    public void removeContainer(String topic) {
        ConcurrentMessageListenerContainer<String, String> container = containers.remove(topic);
        if (container != null) {
            container.stop();
        }
    }

    @Override
    public void createTopicAndListener(String chatRoomId) {
        new KafkaAdmin.NewTopics(new NewTopic(chatRoomId, 1, (short) 1));
        if (!containers.containsKey(chatRoomId)) {
            addContainer(chatRoomId);
        }
    }

    public ConcurrentMessageListenerContainer<String, String> createContainer(String topicName) {
        ContainerProperties containerProps = new ContainerProperties(topicName);
        containerProps.setMessageListener((MessageListener<String, String>) record -> {
            String message = record.value();
            System.out.println("Received message: " + message);
            try {
                Long id = Long.parseLong(message);
                WebSocketSession session = webSocketService.getUserIdSession(id);
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                webSocketService.deleteUserIdSession(id);
            } catch (NumberFormatException err) {
                for (WebSocketSession session : webSocketService.sessionGetChatRoomId(topicName).keySet()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(topicName);
        container.getContainerProperties().setMessageListener(containerProps.getMessageListener());
        container.getContainerProperties().setGroupId(groupId);

        // 컨테이너 시작
        container.start();

        return container;
    }

}
