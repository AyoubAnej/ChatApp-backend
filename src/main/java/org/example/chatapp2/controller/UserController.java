package org.example.chatapp2.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.chatapp2.config.TokenProvider;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;
import org.example.chatapp2.response.ApiResponse;
import org.example.chatapp2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {
    private UserService userService;
    private TokenProvider tokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        UserDTO userDTO = userService.findUserByProfile(token);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<UserDTO>> searchUserHandler(@PathVariable("query") String query) {
        List<UserDTO> users = userService.searchUser(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException {
        String email = tokenProvider.getEmailFromToken(token);
        UserDTO userDTO = userService.findUserByProfile(token);
        userService.updateUser(userDTO.getId(), req);
        ApiResponse res = new ApiResponse("User updated successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
