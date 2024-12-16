package repick.realtimechat.controller;

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


    //웹소켓 연결시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("new connect :" + session.getId());
        kafkaService.createTopicAndListener(webSocketService.getChatRoomId(session));
    }

    //웹소켓 메세지를 보낼시
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messagePayLoad = message.getPayload(); //1
        String chatRoomId = webSocketService.getChatRoomId(session);
        System.out.println("Message from chat room " + chatRoomId + ": " + messagePayLoad);
        try {
            Long value = Long.parseLong(messagePayLoad);
            webSocketService.tokenMessage(session, value);
            kafkaService.sendMessage(chatRoomId, messagePayLoad);
        } catch (NumberFormatException e) {
            kafkaService.sendMessage(chatRoomId, webSocketService.inputMessage(session, messagePayLoad));
        }

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
