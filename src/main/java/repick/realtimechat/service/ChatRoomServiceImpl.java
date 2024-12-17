package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.Response.ChatRoomResponse;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.repository.ChatRoomRepository;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void createChatRoom(ChatRoomRequest chatRoomRequest, ChatUser chatUser, Set<HashTag> hashTags) {
        ChatRoom chatroom = chatRoomRequest.toEntity(chatUser,hashTags);
        chatRoomRepository.save(chatroom);
    }

    @Override
    public void joinChatRoom(String uuid, Long id){
        Long chatRoomId = chatRoomRepository.getChatRoomUuid(UUID.fromString(uuid));
        if(chatRoomRepository.existsChatUserInChatRoom(chatRoomId, id) == 0){
        chatRoomRepository.addChatUserToChatRoom(chatRoomId, id);
        }
    }

    @Override
    public ChatRoom findChatRoomByUUID(UUID uuid) {
        return chatRoomRepository.findByUuid(uuid);
    }

    @Override
    public Page<ChatRoomResponse> getChatRoom(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoom> chatRooms = chatRoomRepository.findNotPrivateChatRooms(pageable);
        return chatRooms.map(ChatRoomResponse::from);
    }

    @Override
    public void resetChatRoom() {
        chatRoomRepository.deleteAll();
    }

}
