package repick.realtimechat.domain;


import io.netty.util.internal.StringUtil;
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
    public static HashTag of(String tag) {
        if(StringUtil.isNullOrEmpty(tag)) throw new IllegalArgumentException("tag cannot be null or empty");
        return HashTag.builder().tag(tag).build();
    }
}
