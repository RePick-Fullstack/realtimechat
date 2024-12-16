package repick.realtimechat.Response;

import repick.realtimechat.domain.ChatUser;

public record ChatUserResponse(
        Long id,
        String nickName) {
    public static ChatUserResponse from(ChatUser chatUser) {
        return new ChatUserResponse(
                chatUser.getId(),
                chatUser.getUsername()
        );
    }
}
