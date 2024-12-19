package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatRoomMessage;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.repository.ChatRoomMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomMessageServiceImpl implements ChatRoomMessageService {
    private final ChatRoomMessageRepository chatRoomMessageRepository;

    @Override
    public ChatRoomMessage saveChatRoomMessage(ChatRoom chatRoom, ChatUser chatUser, String message) {
        ChatRoomMessage chatRoomMessage = ChatRoomMessage.from(chatRoom, chatUser, message);
        return chatRoomMessageRepository.save(chatRoomMessage);
    }

    @Override
    public Page<MessageDTO> getChatRoomId(Long chatRoomId, int page, int size){
        return chatRoomMessageRepository.findChatRoomId(chatRoomId, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))).map(MessageDTO::from);
    }
}
