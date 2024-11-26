package repick.realtimechat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import repick.realtimechat.DTO.TokenDTO;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.DTO.ChatUserDTO;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.utils.SessionList;
import repick.realtimechat.utils.SessionStore;

import java.util.*;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionList sessionList;
    private final SessionStore sessionStore;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaContainerManager containerManager;
    private final ChatRoomMessageServiceImpl chatRoomMessageService;
    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    private String getChatRoomId(WebSocketSession session){
        return Objects.requireNonNull(session.getUri()).getPath().split("/")[2];
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("new connect user :" + session.getId());
        String chatRoomId = getChatRoomId(session);
        sessionStore.getStore().put(session.getId(), session);
        new KafkaAdmin.NewTopics(new NewTopic(chatRoomId,1,(short) 1));
        if(!containerManager.getContainers().containsKey(chatRoomId)) {
            containerManager.addContainer(chatRoomId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session);
        String messageStr = message.getPayload();
        String chatRoomId = getChatRoomId(session);
        System.out.println("MessageDTO from chat room " + chatRoomId + ": " + messageStr);

        if (messageStr.contains("{\"token\":\"")) {
            JsonNode jsonNode = objectMapper.readTree(messageStr);
            String token = jsonNode.get("token").asText();
            String uuid = jsonNode.get("uuid").asText();
            ChatUser chatUser = tokenProvider.getUserIdFromToken(token);
            sessionList.getSessions()
                    .computeIfAbsent(chatRoomId, k -> new HashMap<>())
                    .put(session, new ChatUserDTO(uuid, chatUser.getId(), chatUser.getUsername()));
            messageStr = objectMapper.writeValueAsString(new TokenDTO(uuid, session.getId()));
        } else {

            JsonNode jsonNode = objectMapper.readTree(messageStr);
            String token = jsonNode.get("input").asText();
            ChatUserDTO chatUserDTO = sessionList.getSessions().get(chatRoomId).get(session);
            ChatUser chatUser = ChatUser
                    .builder()
                    .id(chatUserDTO.getId())
                    .username(chatUserDTO.getNickName())
                    .build();
            ChatRoom chatRoom = chatRoomService.findChatRoomByUUID(UUID.fromString(chatRoomId));
            jsonNode = objectMapper.readTree(messageStr);
            String input = jsonNode.get("input").asText();
            chatRoomMessageService.saveChatRoomMessage(chatRoomMessageService.createChatRoomMessage(chatRoom,chatUser,input));
            messageStr = objectMapper.writeValueAsString(new MessageDTO(chatUserDTO, token));
        }
        kafkaProducerService.sendMessage(chatRoomId, messageStr);


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = getChatRoomId(session);
        sessionStore.getStore().remove(session.getId());
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
