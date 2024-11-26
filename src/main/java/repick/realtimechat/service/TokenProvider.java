package repick.realtimechat.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repick.realtimechat.domain.ChatUser;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    public ChatUser getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
        Long userId = claims.get("userId", Long.class);
        String nickName = claims.get("nickName", String.class);
        return ChatUser.builder().id(userId).username(nickName).build();
    }
}