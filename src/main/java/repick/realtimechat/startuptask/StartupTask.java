package repick.realtimechat.startuptask;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.repository.ChatRoomRepository;
import repick.realtimechat.repository.ChatUserRepository;
import repick.realtimechat.service.ChatRoomService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartupTask {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("커뮤니티 채팅방 생성");

        Set<HashTag> hashTags = new HashSet<>();
        Set<ChatUser> chatUsers = new HashSet<>();
        ChatUser chatUser = ChatUser.builder().id(1L).username("커뮤니티 관리자").build();
        chatUsers.add(chatUser);
        chatUserRepository.save(chatUser);
        ChatRoom chatRoom = ChatRoom.builder()
                .uuid(UUID.fromString("38e05c99-d5c7-41bd-ae84-4c7f2d0de160"))
                .chatRoomName("커뮤니티 채팅방")
                .ownerUser(chatUser)
                .chatUsers(chatUsers)
                .hashTags(hashTags)
                .build();
        chatRoomRepository.save(chatRoom);
    }
}

