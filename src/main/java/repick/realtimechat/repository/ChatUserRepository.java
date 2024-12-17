package repick.realtimechat.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import repick.realtimechat.domain.ChatUser;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ChatUser c SET c.username = :username WHERE c.id = :id")
    void updateNicknameById(@Param("id") Long id, @Param("username") String username);
}
