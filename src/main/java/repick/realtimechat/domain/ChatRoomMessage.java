package repick.realtimechat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import repick.realtimechat.config.CreateTImeEntity;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessage extends CreateTImeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "chatuser_id")
    private ChatUser sender;

    private String message;

    public static ChatRoomMessage from(ChatRoom chatRoom, ChatUser chatUser, String message) {
        return ChatRoomMessage
                .builder()
                .chatRoom(chatRoom)
                .sender(chatUser)
                .message(message)
                .build();
    }
}
