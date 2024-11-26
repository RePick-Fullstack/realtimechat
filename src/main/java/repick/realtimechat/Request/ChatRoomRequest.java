package repick.realtimechat.Request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private String chatRoomName;
    private String token;
    private List<String> hashTags;

    public ChatRoom toEntity(ChatUser chatUser,Set<HashTag> hashTag) {
        Set<ChatUser> chatUserSet = new HashSet<>();
        chatUserSet.add(chatUser);
        return ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .ownerUser(chatUser)
                .chatUsers(chatUserSet)
                .hashTags(hashTag)
                .build();
    }
}
