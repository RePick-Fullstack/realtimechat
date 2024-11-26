package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.repository.ChatRoomRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatroom = chatRoomRequest.toEntity();
        chatRoomRepository.save(chatroom);
        return chatroom;
    }

    @Override
    public ChatRoom findChatRoomByUUID(UUID uuid) {
        return chatRoomRepository.findByUuid(uuid);
    }

    @Override
    public Page<ChatRoom> getChatRoom(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatRoomRepository.findAll(pageable);
    }

}
