package repick.realtimechat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {
    @Id
    private long id;
    private String username;

    @ManyToMany(mappedBy = "chatUsers")
    private Set<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "sender")
    private Set<ChatRoomMessage> messages;
}
