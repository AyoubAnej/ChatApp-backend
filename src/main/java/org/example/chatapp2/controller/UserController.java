package org.example.chatapp2.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.chatapp2.config.TokenProvider;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;
import org.example.chatapp2.response.ApiResponse;
import org.example.chatapp2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        UserDTO userDTO = userService.findUserByProfile(token);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUserHandler(@RequestParam("name") String query) {
        List<UserDTO> users = userService.searchUser(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse> updateUserHandler(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateUserRequest req,
            @RequestHeader("Authorization") String token) throws UserException {

        // Validate token and get user details
        String email = tokenProvider.getEmailFromToken(token);
        UserDTO userDTO = userService.findUserByProfile(token);

        // Make sure the user ID in the request matches the ID from the token (if required)
        if (!userDTO.getId().equals(id)) {
            throw new UserException("User ID mismatch.");
        }

        // Update user
        userService.updateUser(id, req);
        ApiResponse res = new ApiResponse("User updated successfully", true);
        return ResponseEntity.ok(res);
    }

}
