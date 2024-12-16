package repick.realtimechat.service;

import org.springframework.data.domain.Page;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatRoomMessage;
import repick.realtimechat.domain.ChatUser;

public interface ChatRoomMessageService {
    ChatRoomMessage saveChatRoomMessage(ChatRoom chatRoom, ChatUser chatUser, String message);
    Page<MessageDTO> getChatRoomId(Long chatRoomId, int page, int size);
}
