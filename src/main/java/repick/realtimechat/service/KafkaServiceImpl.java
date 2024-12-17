package repick.realtimechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.DTO.UpdateUserNickName;
import repick.realtimechat.domain.ChatUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebSocketService webSocketService;
    private final ChatUserService chatUserService;
    private final ConcurrentKafkaListenerContainerFactory<String, byte[]> factory;
    private final Map<String, ConcurrentMessageListenerContainer<String, byte[]>> containers = new HashMap<>();

    @Value("${kafka.consumer.group-id:default-group}")
    private String groupId;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Producer MessageDTO Sent : " + message);
    }

    @KafkaListener(id = "realtimechat", topics = "updateusernickname")
    public void listen(UpdateUserNickName updateUserNickName){
        System.out.println("User NickName Changed: " + updateUserNickName.getNickName());
        chatUserService.UpdateUserNickName(updateUserNickName);
    }

    @Override
    public void addContainer(String topic) {
        if (!containers.containsKey(topic)) {
            ConcurrentMessageListenerContainer<String, byte[]> container =
                    createContainer(topic);
            containers.put(topic, container);
        }
    }

    @Override
    public void removeContainer(String topic) {
        ConcurrentMessageListenerContainer<String, byte[]> container = containers.remove(topic);
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

    public ConcurrentMessageListenerContainer<String, byte[]> createContainer(String topicName) {
        ContainerProperties containerProps = new ContainerProperties(topicName);
        containerProps.setMessageListener((MessageListener<String, byte[]>) record -> {
            String message = new String(record.value());
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
        ConcurrentMessageListenerContainer<String, byte[]> container = factory.createContainer(topicName);
        container.getContainerProperties().setMessageListener(containerProps.getMessageListener());
        container.getContainerProperties().setGroupId(groupId);

        // 컨테이너 시작
        container.start();

        return container;
    }

}
