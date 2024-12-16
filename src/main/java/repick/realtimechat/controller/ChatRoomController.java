package repick.realtimechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.*;
import repick.realtimechat.DTO.MessageDTO;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.Response.ChatRoomResponse;
import repick.realtimechat.Response.ChatUserResponse;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.repository.ChatRoomMessageRepository;
import repick.realtimechat.repository.ChatRoomRepository;
import repick.realtimechat.service.ChatRoomMessageService;
import repick.realtimechat.service.ChatRoomService;
import repick.realtimechat.service.ChatUserService;
import repick.realtimechat.service.HashTagService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;
    private final HashTagService hashTagService;
    private final ChatRoomMessageService chatRoomMessageService;

    @PostMapping
    public String createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        ChatUser chatUser = chatUserService.saveUserFromToken(chatRoomRequest.getToken());
        Set<HashTag> hashTags = new HashSet<>();
        if (chatRoomRequest.getHashTags() != null && !chatRoomRequest.getHashTags().isEmpty()) {
            for (String tagName : chatRoomRequest.getHashTags()) {
                Optional<HashTag> optionalHashTag = hashTagService.getHashTag(tagName);
                HashTag hashTag = optionalHashTag.orElseGet(() -> hashTagService.saveHashTag(tagName));
                hashTags.add(hashTag);
            }
        }
        chatRoomService.createChatRoom(chatRoomRequest, chatUser, hashTags);
       return "created";
    }

    @GetMapping("/{uuid}")
    public ChatRoomResponse findByUUID(@PathVariable UUID uuid) {
        return ChatRoomResponse.from(chatRoomService.findChatRoomByUUID(uuid));
    }

    @GetMapping("/chatroom")
    public Page<ChatRoomResponse> findChatRooms(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return chatRoomService.getChatRoom(page,size);
    }

    @GetMapping("/{uuid}/message")
    public Page<MessageDTO> findByUUIDMessages(
            @PathVariable UUID uuid,
            @RequestParam int page,
            @RequestParam int size
    ) {
        ChatRoom chatRoom = chatRoomService.findChatRoomByUUID(uuid);
        return chatRoomMessageService.getChatRoomId(chatRoom.getId(), page, size);
    }

    @GetMapping("/reset")
    public String resetChatRoom() {
        chatRoomService.resetChatRoom();
        return "reset completed";
    }

    @GetMapping("/verification")
    public ChatUserResponse userVerification(
            @RequestHeader String Authorization
    ) {
        return ChatUserResponse.from(chatUserService.saveUserFromToken(Authorization));
    }

}
