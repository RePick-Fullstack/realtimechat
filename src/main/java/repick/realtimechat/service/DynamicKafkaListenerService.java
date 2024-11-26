package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.utils.SessionList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DynamicKafkaListenerService {

    private final ConcurrentKafkaListenerContainerFactory<String, String> factory;
    private final SessionList sessionList;

    @Value("${kafka.consumer.group-id:default-group}")
    private String groupId;

    public ConcurrentMessageListenerContainer<String, String> createContainer(String topicName) {
        // 컨테이너 프로퍼티 설정
        ContainerProperties containerProps = new ContainerProperties(topicName);
        // 메시지 리스너 설정
        containerProps.setMessageListener((MessageListener<String, String>) record -> {
            System.out.println("Received message: " + record.value());
            for(Map.Entry<WebSocketSession, ChatUser> entry : sessionList.getSessions().get(topicName).entrySet()){
                WebSocketSession session = entry.getKey();
                String username = entry.getValue().getNickName();
                String uuid = entry.getValue().getUuid();
                    try {
                        session.sendMessage(new TextMessage(record.value()));
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
