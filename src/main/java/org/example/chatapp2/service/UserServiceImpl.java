package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.config.TokenProvider;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.repositories.UserRepository;
import org.example.chatapp2.request.UpdateUserRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new UserException("User not found with id "+ id);
    }

    @Override
    public User findUserByProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if (email == null){
            throw new BadCredentialsException("Recieved invalid token---");
        }
        User user = userRepository.findByEmail(email);

        if (user == null){
            throw new UserException("user not found with email "+email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);

        if (req.getFull_name() != null){
            user.setFull_name(req.getFull_name());
        }

        if (req.getProfile_picture() != null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
