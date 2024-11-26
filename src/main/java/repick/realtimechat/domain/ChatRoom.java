package repick.realtimechat.domain;

import jakarta.persistence.*;
import lombok.*;
import repick.realtimechat.config.CreateTImeEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(indexes = {@Index(columnList = "uuid")})
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends CreateTImeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String chatRoomName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ChatUser ownerUser;

    @ManyToMany
    @JoinTable(
            name = "chatroom_chatuser", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "chatuser_id")
    )
    private Set<ChatUser> chatUsers;

    @ManyToMany
    @JoinTable(
            name = "chatroom_hashtag", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<HashTag> hashTags;
}
