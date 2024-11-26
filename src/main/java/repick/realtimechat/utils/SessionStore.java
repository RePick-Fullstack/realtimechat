package repick.realtimechat.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Component
@Getter
public class SessionStore {
    private HashMap<String, WebSocketSession> store = new HashMap<>();
}
