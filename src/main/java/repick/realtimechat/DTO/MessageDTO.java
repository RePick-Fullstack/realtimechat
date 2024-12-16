package repick.realtimechat.DTO;

import repick.realtimechat.Response.ChatUserResponse;
import repick.realtimechat.domain.ChatRoomMessage;

import java.time.LocalDateTime;


public record MessageDTO(
        Long userId,
        String username,
        String message,
        String createAt
) {
    public static MessageDTO from(ChatRoomMessage chatRoomMessage) {
        return new MessageDTO(
                chatRoomMessage.getSender().getId(),
                chatRoomMessage.getSender().getUsername(),
                chatRoomMessage.getMessage(),
                chatRoomMessage.getCreatedAt().toString()
        );
    }
}
