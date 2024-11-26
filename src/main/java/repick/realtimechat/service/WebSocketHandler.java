package repick.realtimechat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import repick.realtimechat.domain.ChatMessage;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.utils.SessionList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionList sessionList;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaContainerManager containerManager;

    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("new connect user :" + session.getId());
        String chatRoomId = Objects.requireNonNull(session.getUri()).getPath().split("/")[2];
        new KafkaAdmin.NewTopics(new NewTopic(chatRoomId,1,(short) 1));
        if(!containerManager.getContainers().containsKey(chatRoomId)) {
            containerManager.addContainer(chatRoomId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session);
        String messageStr = message.getPayload();
        String chatRoomId = Objects.requireNonNull(session.getUri()).getPath().split("/")[2];
        System.out.println("Message from chat room " + chatRoomId + ": " + messageStr);

        if (messageStr.contains("{\"token\":\"")) {
            JsonNode jsonNode = objectMapper.readTree(messageStr);
            String token = jsonNode.get("token").asText();
            String uuid = jsonNode.get("uuid").asText();

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String nickName = claims.get("nickName", String.class);
            sessionList.getSessions()
                    .computeIfAbsent(chatRoomId, k -> new HashMap<>())
                    .put(session, new ChatUser(uuid, nickName));
            String jsonUuid = "{\"uuid\":\"" + uuid + "\"}";
            messageStr = jsonUuid;
        } else {
            JsonNode jsonNode = objectMapper.readTree(messageStr);
            String token = jsonNode.get("input").asText();
            ChatUser chatUser = sessionList.getSessions().get(chatRoomId).get(session);
            messageStr = objectMapper.writeValueAsString(new ChatMessage(chatUser, token));
        }
        kafkaProducerService.sendMessage(chatRoomId, messageStr);


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = Objects.requireNonNull(session.getUri()).getPath().split("/")[2];

        boolean isEmpty = sessionList.getSessions().computeIfPresent(chatRoomId, (key, sessionList) -> {
            sessionList.remove(session);
            // 채팅방 세션이 없으면 null 반환하여 채팅방 삭제
            return sessionList.isEmpty() ? null : sessionList;
        }) == null;

        if (isEmpty) {
            containerManager.removeContainer(chatRoomId);
        }

        System.out.println("Disconnected from chat room: " + chatRoomId);
    }

}
