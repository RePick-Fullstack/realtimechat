package repick.realtimechat.startuptask;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.repository.ChatRoomRepository;
import repick.realtimechat.repository.ChatUserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartupTask {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

        String[] uuid = {
                "38e05c99-d5c7-41bd-ae84-4c7f2d0de160",
                "866e2656-010d-49b4-ada7-e98c18832945",
                "40d287ed-70ee-4799-a387-6e13662bbb8e",
                "ac370ef1-9b64-4efc-a3eb-2ca4cdf1c99e",
                "720b61aa-75b0-4cd2-8b8f-f2d366d79b4b",
                "e4b0695f-6956-4e42-9caa-247bc71b1706",
                "59aa1d50-1ea6-4b87-aa6f-876d4862f812",
                "8e258401-3f5a-4f97-98c7-f03241d5fcbd",
                "e28f83e5-69f7-4edc-9c52-e06b4300590a",
                "096141a3-f282-40cc-a692-6af7e60c6c97",
                "deb884a2-7e66-4367-af1c-fd549b9f4eb4",
                "6f18b34f-d081-417b-98a4-bedba287b45d",
                "e4b5ed8a-e6dd-414c-aa3b-7de74defc75a",
                "39170b8b-a74f-41cb-b09f-eb9a94833a02"
        };

    String[] category = {
            "전체","에너지","소재","산업재","경기 관련 소비재","필수 소비재","건강관리","금융","IT",
            "커뮤니케이션 서비스","유틸리티","증권사","지주회사","기타"
    };


    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("커뮤니티 채팅방 생성");

        for(int i=0;i<uuid.length;i++){
            Set<HashTag> hashTags = new HashSet<>();
            Set<ChatUser> chatUsers = new HashSet<>();
            ChatUser chatUser = ChatUser.builder().id(1L).username("커뮤니티 관리자").build();
            chatUsers.add(chatUser);
            chatUserRepository.save(chatUser);
            ChatRoom chatRoom = ChatRoom.builder()
                    .uuid(UUID.fromString(uuid[i]))
                    .chatRoomName(category[i] + "채팅방")
                    .ownerUser(chatUser)
                    .chatUsers(chatUsers)
                    .hashTags(hashTags)
                    .isPrivate(true)
                    .build();
            chatRoomRepository.save(chatRoom);
        }
    }
}

