package org.example.chatapp2.service;

import jdk.jshell.spi.ExecutionControl;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;

import java.util.List;

public interface UserService {

    UserDTO findUserById(Integer id) throws UserException;
    UserDTO findUserByProfile(String token) throws UserException;
    List<UserDTO> searchUser(String query);
    void updateUser(Integer id, UpdateUserRequest request) throws UserException;
    UserDTO convertToDTO(User user);
    User convertToEntity(UserDTO userDTO);
}
