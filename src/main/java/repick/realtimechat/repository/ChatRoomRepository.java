package repick.realtimechat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import repick.realtimechat.domain.ChatRoom;

import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUuid(UUID uuid);
    Page<ChatRoom> findAll(Pageable pageable);
    @Query("SELECT c FROM ChatRoom c WHERE c.isPrivate = false ORDER BY c.id DESC")
    Page<ChatRoom> findNotPrivateChatRooms(Pageable pageable);

}
