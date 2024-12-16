package repick.realtimechat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.Response.ChatUserResponse;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final ChatRoomMessageService chatRoomMessageService;
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatUserService chatUserService;
    private final Map<String, HashMap<WebSocketSession, ChatUserResponse>> sessions = new ConcurrentHashMap<>();
    private final Map<Long, WebSocketSession> useridSessions = new ConcurrentHashMap<>();

    @Override
    public String getChatRoomId(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath().split("/websocket/")[1];
    }

    @Override
    public HashMap<WebSocketSession, ChatUserResponse> sessionGetChatRoomId(String chatRoomId) {
        return sessions.get(chatRoomId);
    }

    @Override
    public boolean StoreRemove(WebSocketSession session) {
        String chatRoomId = getChatRoomId(session);
        return sessions.computeIfPresent(chatRoomId, (key, sessionList) -> {
            sessionList.remove(session);
            return sessionList.isEmpty() ? null : sessionList;
        }) == null;
    }

    @Override
    public WebSocketSession getUserIdSession(Long id) {
        return useridSessions.get(id);
    }

    @Override
    public void deleteUserIdSession(Long id) {
        useridSessions.remove(id);
    }

    @Override
    public void tokenMessage(WebSocketSession session, Long id) {
        String chatRoomId = getChatRoomId(session);
        ChatUser chatUser = chatUserService.getUserId(id);
        sessions.computeIfAbsent(chatRoomId, k -> new HashMap<>()).put(session, ChatUserResponse.from(chatUser));
        useridSessions.put(id, session);

    }

    @Override
    public String inputMessage(WebSocketSession session, String messagePayLoad) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(messagePayLoad);
        String chatRoomId = getChatRoomId(session);
        String input = jsonNode.get("input").asText();
        ChatUserResponse chatUserResponse = sessions.get(chatRoomId).get(session);
        ChatUser chatUser = chatUserService.getUserId(chatUserResponse.id());
        ChatRoom chatRoom = chatRoomService.findChatRoomByUUID(UUID.fromString(chatRoomId));
        chatRoomMessageService.saveChatRoomMessage(chatRoomMessageService.createChatRoomMessage(chatRoom, chatUser, input));
        return objectMapper.writeValueAsString(MessageDTO.from(ChatUserResponse.from(chatUser), input));
    }
}
