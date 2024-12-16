package repick.realtimechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repick.realtimechat.domain.ChatRoomMessage;


@Repository
public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long> {
}
