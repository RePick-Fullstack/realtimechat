package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatRoomMessage;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.repository.ChatRoomMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomMessageServiceImpl implements ChatRoomMessageService {
    private final ChatRoomMessageRepository chatRoomMessageRepository;

    @Override
    public ChatRoomMessage createChatRoomMessage(ChatRoom chatRoom, ChatUser chatUser, String message) {
        return ChatRoomMessage.builder().chatRoom(chatRoom).sender(chatUser).message(message).build();
    }

    @Override
    public void saveChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        chatRoomMessageRepository.save(chatRoomMessage);
    }
}
