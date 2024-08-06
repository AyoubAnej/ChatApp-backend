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
    @Autowired
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

    public void updateUser(Integer id, UpdateUserRequest request) throws UserException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found"));

        if (request.getFullName() != null && !request.getFullName().equals("undefined")) {
            user.setFullName(request.getFullName());
        }
        if (request.getProfilePicture() != null && !request.getProfilePicture().equals("undefined")) {
            user.setProfilePicture(request.getProfilePicture());
        }

        userRepository.save(user);
    }

    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getProfilePicture());
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setProfilePicture(userDTO.getProfilePicture());
        return user;
    }
}
