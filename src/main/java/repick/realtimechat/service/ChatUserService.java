package repick.realtimechat.service;

import repick.realtimechat.DTO.UpdateUserNickName;
import repick.realtimechat.domain.ChatUser;

public interface ChatUserService {
    ChatUser saveUserFromToken(String token);

    ChatUser getUserId(Long id);
    void UpdateUserNickName(UpdateUserNickName updateUserNickName);
}
