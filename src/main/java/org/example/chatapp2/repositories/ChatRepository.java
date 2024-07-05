package org.example.chatapp2.repositories;

import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join c.users u where u.Id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);
    @Query("select c from Chat c where c.isGroup = false and :user member of c.users and :reqUser member of c.users")
    public Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
}
