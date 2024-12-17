package repick.realtimechat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repick.realtimechat.domain.ChatRoom;

import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUuid(UUID uuid);
    Page<ChatRoom> findAll(Pageable pageable);
    @Query("SELECT c FROM ChatRoom c WHERE c.isPrivate = false ORDER BY c.id DESC")
    Page<ChatRoom> findNotPrivateChatRooms(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO chatroom_chatuser (chatroom_id, chatuser_id) VALUES (:chatRoomId, :chatUserId)", nativeQuery = true)
    void addChatUserToChatRoom(@Param("chatRoomId") Long chatRoomId, @Param("chatUserId") Long chatUserId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM chatroom_chatuser WHERE chatroom_id = :chatRoomId AND chatuser_id = :chatUserId)", nativeQuery = true)
    int existsChatUserInChatRoom(@Param("chatRoomId") Long chatRoomId, @Param("chatUserId") Long chatUserId);

    @Query(value = "select r.chatRoomName,r.ownerUser.username,count(r.chatUsers) from ChatRoom r where r.uuid = :uuid")
    String getChatRoomUuidRes(@Param("uuid") UUID uuid);

    @Query(value = "select r.id from ChatRoom r where r.uuid = :uuid")
    Long getChatRoomUuid(@Param("uuid") UUID uuid);
}
