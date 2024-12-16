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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatuser_id")
    private ChatUser sender;

    private String message;
}
