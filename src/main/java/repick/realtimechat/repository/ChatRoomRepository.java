package repick.realtimechat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import repick.realtimechat.domain.ChatRoom;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUuid(UUID uuid);
    Page<ChatRoom> findAll(Pageable pageable);
}
