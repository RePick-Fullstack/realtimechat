package repick.realtimechat.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Table(indexes = {@Index(columnList = "tag")})
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "hashTags")
    private Set<ChatRoom> chatRooms;
}
