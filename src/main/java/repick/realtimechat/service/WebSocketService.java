package repick.realtimechat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.DTO.ChatUserDTO;

import java.util.HashMap;

public interface WebSocketService {
    String getChatRoomId(WebSocketSession session);

    HashMap<WebSocketSession, ChatUserDTO> sessionGetChatRoomId(String chatRoomId);

    void StoreAdd(WebSocketSession session);

    boolean StoreRemove(WebSocketSession session);

    WebSocketSession getSession(String id);

    String tokenMessage(WebSocketSession session, JsonNode jsonNode) throws JsonProcessingException;

    String inputMessage(WebSocketSession session, JsonNode jsonNode) throws JsonProcessingException;
}
