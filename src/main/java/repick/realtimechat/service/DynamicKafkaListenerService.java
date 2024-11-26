package repick.realtimechat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.DTO.ChatUserDTO;
import repick.realtimechat.utils.SessionList;
import repick.realtimechat.utils.SessionStore;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DynamicKafkaListenerService {

    private final ConcurrentKafkaListenerContainerFactory<String, String> factory;
    private final SessionList sessionList;
    private final SessionStore sessionStore;
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.consumer.group-id:default-group}")
    private String groupId;

    public ConcurrentMessageListenerContainer<String, String> createContainer(String topicName) {
        // 컨테이너 프로퍼티 설정
        ContainerProperties containerProps = new ContainerProperties(topicName);
        // 메시지 리스너 설정
        containerProps.setMessageListener((MessageListener<String, String>) record -> {
            String message = record.value();
            System.out.println("Received message: " + message);
            if(message.contains("\"sessionId\":\"")){
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(message);
                    String uuid = jsonNode.get("uuid").asText();
                    String session = jsonNode.get("sessionId").asText();

                    System.out.println("Sending message: " + uuid);

                    var sessionHandler = sessionStore.getStore().get(session);

                    sessionHandler.sendMessage(new TextMessage("{\"uuid\":\"" + uuid + "\"}"));
                } catch (IOException e) {
                    throw new RuntimeException("Error processing message", e);
                }

            } else for(Map.Entry<WebSocketSession, ChatUserDTO> entry : sessionList.getSessions().get(topicName).entrySet()){
                WebSocketSession session = entry.getKey();
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }

        });
        // 동적 컨테이너 생성
        ConcurrentMessageListenerContainer<String, String> container =
                factory.createContainer(topicName);
        // 컨테이너 프로퍼티 설정
        container.getContainerProperties().setMessageListener(containerProps.getMessageListener());
        container.getContainerProperties().setGroupId(groupId);

        // 컨테이너 시작
        container.start();

        return container;
    }
}
