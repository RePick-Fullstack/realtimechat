package repick.realtimechat.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import repick.realtimechat.domain.ChatUser;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class SessionList {
    private ConcurrentHashMap<String, HashMap<WebSocketSession,ChatUser>> sessions = new ConcurrentHashMap<>();
}

