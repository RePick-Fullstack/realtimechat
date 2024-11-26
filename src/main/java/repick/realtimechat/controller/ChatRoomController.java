package repick.realtimechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import repick.realtimechat.Request.ChatRoomRequest;
import repick.realtimechat.Response.ChatRoomResponse;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.service.ChatRoomService;
import repick.realtimechat.service.TokenProvider;

import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public String createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        chatRoomService.createChatRoom(chatRoomRequest);
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

    @GetMapping("/test")
    public String test(
            @RequestParam String token
    ) {
        tokenProvider.getUserIdFromToken(token);
        return "ok";
    }

}
