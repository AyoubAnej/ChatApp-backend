package org.example.chatapp2.service;

import jdk.jshell.spi.ExecutionControl;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;

import java.util.List;

public interface UserService {

    public User findUserById(Integer id) throws UserException;
    public User findUserByProfile(String jwt) throws UserException;
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException;
    public List<User> searchUser(String query);
}
