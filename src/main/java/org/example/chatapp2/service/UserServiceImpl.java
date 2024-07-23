package org.example.chatapp2.service;

import org.example.chatapp2.config.TokenProvider;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;
import org.example.chatapp2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private TokenProvider tokenProvider;


    @Override
    public UserDTO findUserById(Integer id) throws UserException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id " + id));
        return convertToDTO(user);
    }
    @Override
    public UserDTO findUserByProfile(String token) throws UserException {
        // Extract email from token and find user
        String email = tokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found");
        }
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateUser(Integer id, UpdateUserRequest request) throws UserException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found"));
        // Update fields
        user.setFull_name(request.getFull_name());
        user.setProfile_picture(request.getProfile_picture());
        userRepository.save(user);
    }

    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getFull_name(), user.getEmail(), user.getProfile_picture());
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFull_name(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setProfile_picture(userDTO.getProfilePicture());
        return user;
    }
}
