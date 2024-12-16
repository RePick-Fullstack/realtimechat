package repick.realtimechat.DTO;

import repick.realtimechat.Response.ChatUserResponse;


public record MessageDTO(
        Long userId,
        String username,
        String message) {
    public static MessageDTO from(ChatUserResponse chatUserResponse, String message) {
        return new MessageDTO(
                chatUserResponse.id(),
                chatUserResponse.nickName(),
                message
        );
    }
}
