package repick.realtimechat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.DTO.ChatUserDTO;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.DTO.TokenDTO;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final ChatRoomMessageService chatRoomMessageService;
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HashMap<String, WebSocketSession> store = new HashMap<>();
    private final ChatUserService chatUserService;
    private final HashMap<String, HashMap<WebSocketSession, ChatUserDTO>> sessions = new HashMap<>();

    @Override
    public String getChatRoomId(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath().split("/")[2];
    }

    @Override
    public HashMap<WebSocketSession, ChatUserDTO> sessionGetChatRoomId(String chatRoomId) {
        return sessions.get(chatRoomId);
    }

    @Override
    public void StoreAdd(WebSocketSession session) {
        store.put(session.getId(), session);
    }

    @Override
    public boolean StoreRemove(WebSocketSession session) {
        String chatRoomId = getChatRoomId(session);
        store.remove(session.getId());
        return sessions.computeIfPresent(chatRoomId, (key, sessionList) -> {
            sessionList.remove(session);
            return sessionList.isEmpty() ? null : sessionList;
        }) == null;
    }

    @Override
    public WebSocketSession getSession(String id) {
        return store.get(id);
    }

    @Override
    public String tokenMessage(WebSocketSession session, JsonNode jsonNode) throws JsonProcessingException {
        String chatRoomId = getChatRoomId(session);
        String token = jsonNode.get("token").asText();
        String uuid = jsonNode.get("uuid").asText();
        ChatUser chatUser = chatUserService.saveUserFromToken(token);
        sessions.computeIfAbsent(chatRoomId, k -> new HashMap<>())
                .put(session, new ChatUserDTO(uuid, chatUser.getId(), chatUser.getUsername()));
        return objectMapper.writeValueAsString(new TokenDTO(uuid, session.getId()));
    }

    @Override
    public String inputMessage(WebSocketSession session, JsonNode jsonNode) throws JsonProcessingException {
        String chatRoomId = getChatRoomId(session);
        String input = jsonNode.get("input").asText();
        ChatUserDTO chatUserDTO = sessions.get(chatRoomId).get(session);
        ChatUser chatUser = ChatUser
                .builder()
                .id(chatUserDTO.getId())
                .username(chatUserDTO.getNickName())
                .build();
        ChatRoom chatRoom = chatRoomService.findChatRoomByUUID(UUID.fromString(chatRoomId));
        chatRoomMessageService.saveChatRoomMessage(chatRoomMessageService.createChatRoomMessage(chatRoom, chatUser, input));
        return objectMapper.writeValueAsString(new MessageDTO(chatUserDTO, input));
    }
}
