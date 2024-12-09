package repick.realtimechat.service;

import org.springframework.data.domain.Page;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.Response.ChatRoomResponse;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;

import java.util.Set;
import java.util.UUID;

public interface ChatRoomService {
    void createChatRoom(ChatRoomRequest chatRoomRequest, ChatUser chatUser, Set<HashTag> hashTags);
    ChatRoom findChatRoomByUUID(UUID uuid);
    Page<ChatRoomResponse> getChatRoom(int page, int size);
    void resetChatRoom();
}
