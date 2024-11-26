package repick.realtimechat.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    String uuid;
    String sessionId;
}
