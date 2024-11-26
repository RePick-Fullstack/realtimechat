package repick.realtimechat.domain;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {
    String uuid;
    String nickName;
}
