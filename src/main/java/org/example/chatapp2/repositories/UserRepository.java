package org.example.chatapp2.repositories;

import org.example.chatapp2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.full_name LIKE %:query% OR u.email LIKE %:query%")
    public List<User> searchUser(@Param("query") String query);
}
