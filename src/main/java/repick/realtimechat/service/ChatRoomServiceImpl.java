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
import repick.realtimechat.repository.ChatUserRepository;
import repick.realtimechat.repository.HashTagRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final HashTagRepository hashTagRepository;
    private final TokenProvider tokenProvider;
    private final ChatUserRepository chatUserRepository;

    @Override
    public ChatRoom createChatRoom(ChatRoomRequest chatRoomRequest) {
        System.out.printf(chatRoomRequest.getHashTags().toString());
        Set<HashTag> hashTags = new HashSet<>();
        if (chatRoomRequest.getHashTags() != null && !chatRoomRequest.getHashTags().isEmpty()) {
            for (String tagName : chatRoomRequest.getHashTags()) {
                Optional<HashTag> optionalHashTag = hashTagRepository.findByTag(tagName);
                HashTag hashTag = optionalHashTag.orElseGet(() -> hashTagRepository.save(
                        HashTag.builder().tag(tagName).build()
                ));
                hashTags.add(hashTag);
            }
        }

        ChatUser chatUser = tokenProvider.getUserIdFromToken(chatRoomRequest.getToken());
        chatUserRepository.save(chatUser);
        // ChatRoom 객체 생성
        ChatRoom chatroom = chatRoomRequest.toEntity(chatUser,hashTags);

        chatRoomRepository.save(chatroom);
        return chatroom;
    }


    @Override
    public ChatRoom findChatRoomByUUID(UUID uuid) {
        return chatRoomRepository.findByUuid(uuid);
    }

    @Override
    public Page<ChatRoomResponse> getChatRoom(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
        return chatRooms.map(ChatRoomResponse::from);
    }

}
