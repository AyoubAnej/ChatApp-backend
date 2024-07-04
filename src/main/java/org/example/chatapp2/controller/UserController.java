package org.example.chatapp2.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.UpdateUserRequest;
import org.example.chatapp2.response.ApiResponse;
import org.example.chatapp2.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserByProfile(token);

        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query){
        List<User> users = userService.searchUser(query);

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserByProfile(token);

        userService.updateUser(user.getId(), req);

        ApiResponse res = new ApiResponse("user updated successfully", true);

        return  new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }
}
