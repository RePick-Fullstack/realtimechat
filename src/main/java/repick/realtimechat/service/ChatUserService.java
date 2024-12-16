package repick.realtimechat.service;

import repick.realtimechat.domain.ChatUser;

public interface ChatUserService {
    ChatUser saveUserFromToken(String token);

    ChatUser getUserId(Long id);
}
