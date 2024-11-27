package repick.realtimechat.service;

import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatRoomMessage;
import repick.realtimechat.domain.ChatUser;

public interface ChatRoomMessageService {
    ChatRoomMessage createChatRoomMessage(ChatRoom chatRoom, ChatUser chatUser, String message);
    void saveChatRoomMessage(ChatRoomMessage chatRoomMessage);
}
