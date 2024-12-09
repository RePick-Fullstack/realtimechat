package repick.realtimechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.Response.ChatRoomResponse;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.service.ChatRoomService;
import repick.realtimechat.service.ChatUserService;
import repick.realtimechat.service.HashTagService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;
    private final HashTagService hashTagService;

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
    public Page<ChatRoomResponse> findChatRooms() {
        return chatRoomService.getChatRoom(0,20);
    }

    @GetMapping("/reset")
    public String resetChatRoom() {
        chatRoomService.resetChatRoom();
        return "reset completed";
    }

    @GetMapping("/test")
    public String test(
            @RequestParam String token
    ) {
        chatUserService.saveUserFromToken(token);
        return "ok";
    }

}
