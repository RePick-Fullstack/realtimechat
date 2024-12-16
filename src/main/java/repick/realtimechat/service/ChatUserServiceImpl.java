package repick.realtimechat.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repick.realtimechat.domain.ChatUser;
import repick.realtimechat.repository.ChatUserRepository;

@RequiredArgsConstructor
@Service
public class ChatUserServiceImpl implements ChatUserService {

    private final ChatUserRepository chatUserRepository;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Override
    public ChatUser saveUserFromToken(String bearerToken) {
        String token = bearerToken.split(" ")[1];
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
        Long userId = claims.get("userId", Long.class);
        String nickName = claims.get("nickName", String.class);
        ChatUser chatuser = ChatUser.builder().id(userId).username(nickName).build();
        if (!chatUserRepository.existsById(chatuser.getId())) {
            chatUserRepository.save(chatuser);
        }
        return chatuser;
    }

    @Override
    public ChatUser getUserId(Long id) {
        return chatUserRepository.findById(id).orElse(null);
    }
}