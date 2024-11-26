package repick.realtimechat.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO extends ChatUserDTO {
    private String message;

    public MessageDTO(ChatUserDTO chatUserDTO, String messageStr) {
        this.message = messageStr;
        this.uuid = chatUserDTO.getUuid();
        this.nickName = chatUserDTO.getNickName();
    }
}
