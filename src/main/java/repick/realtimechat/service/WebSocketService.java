package repick.realtimechat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.Response.ChatUserResponse;

import java.util.HashMap;

public interface WebSocketService {
    String getChatRoomId(WebSocketSession session);

    HashMap<WebSocketSession, ChatUserResponse> sessionGetChatRoomId(String chatRoomId);

    boolean StoreRemove(WebSocketSession session);

    WebSocketSession getUserIdSession(Long id);

    void deleteUserIdSession(Long id);

    void tokenMessage(WebSocketSession session, Long id);

    String inputMessage(WebSocketSession session, String messagePayLoad) throws JsonProcessingException;
}
