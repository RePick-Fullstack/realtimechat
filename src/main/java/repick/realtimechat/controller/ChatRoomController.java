package repick.realtimechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.repository.ChatRoomRepository;
import repick.realtimechat.service.ChatRoomService;
import repick.realtimechat.service.TokenProvider;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ChatRoom createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
       return chatRoomService.createChatRoom(chatRoomRequest);
    }

    @GetMapping("/{uuid}")
    public ChatRoom findByUUID(@PathVariable UUID uuid) {
        return chatRoomService.findChatRoomByUUID(uuid);
    }

    @GetMapping("/chatroom")
    public Page<ChatRoom> findChatRooms() {
        return chatRoomService.getChatRoom(0,20);
    }

    @GetMapping("/test")
    public String test(
            @RequestParam String token
    ) {
        tokenProvider.getUserIdFromToken(token);
        return "ok";
    }

}
