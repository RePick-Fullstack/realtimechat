package repick.realtimechat.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import repick.realtimechat.domain.ChatRoom;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private String chatRoomName;
    private long ownerId;
    private String ownerName;

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .ownerId(ownerId)
                .ownerName(ownerName).build();
    }
}
