package repick.realtimechat.domain;

import jakarta.persistence.*;
import lombok.*;
import repick.realtimechat.config.CreateTImeEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    private long ownerId;
    private String ownerName;
}
