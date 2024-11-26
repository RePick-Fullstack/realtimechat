package repick.realtimechat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends ChatUser {
    private String message;

    public ChatMessage(ChatUser chatUser, String messageStr) {
        this.message = messageStr;
        this.uuid = chatUser.getUuid();
        this.nickName = chatUser.getNickName();
    }
}
