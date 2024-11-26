package repick.realtimechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repick.realtimechat.domain.ChatUser;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
