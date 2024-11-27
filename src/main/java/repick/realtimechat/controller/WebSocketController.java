package repick.realtimechat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import repick.realtimechat.service.KafkaService;
import repick.realtimechat.service.WebSocketService;

@Component
@RequiredArgsConstructor
public class WebSocketController extends TextWebSocketHandler {

    private final WebSocketService webSocketService;
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    //웹소켓 연결시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("new connect :" + session.getId());
        webSocketService.StoreAdd(session);
        kafkaService.createTopicAndListener(webSocketService.getChatRoomId(session));
    }

    //웹소켓 메세지를 보낼시
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messagePayLoad = message.getPayload();
        String chatRoomId = webSocketService.getChatRoomId(session);
        System.out.println("MessageDTO from chat room " + chatRoomId + ": " + messagePayLoad);
        JsonNode jsonNode = objectMapper.readTree(messagePayLoad);
        if (jsonNode.has("token")) {
            messagePayLoad = webSocketService.tokenMessage(session, jsonNode);
        } else {
            messagePayLoad = webSocketService.inputMessage(session, jsonNode);
        }
        kafkaService.sendMessage(chatRoomId, messagePayLoad);
    }

    //웹소켓 연결 해제시
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = webSocketService.getChatRoomId(session);
        if (webSocketService.StoreRemove(session)) {
            kafkaService.removeContainer(chatRoomId);
        }
        System.out.println("Disconnected from chat room: " + chatRoomId);
    }
}
