package repick.realtimechat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import repick.realtimechat.domain.ChatRoomMessage;


@Repository
public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long> {

    @Query(value = "select m from ChatRoomMessage m where m.chatRoom.id = :chatRoomId order by m.createdAt DESC ")
    Page<ChatRoomMessage> findChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}
