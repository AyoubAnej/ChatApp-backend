package org.example.chatapp2.repositories;

import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.Id = :userId")
    public List<Chat> findChatByUserId(@Param("userid") Integer userId);
    @Query("SELECT c FROM Chat c WHERE c.isGroup = false AND :user MEMBER OF c.users AND :reqUser MEMBER OF c.users")
    public Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
}
