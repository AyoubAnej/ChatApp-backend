package org.example.chatapp2.controller;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.ChatDTO;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.GroupChatRequest;
import org.example.chatapp2.request.SingleChatRequest;
import org.example.chatapp2.response.ApiResponse;
import org.example.chatapp2.service.ChatService;
import org.example.chatapp2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/single")
    public ResponseEntity<ChatDTO> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        ChatDTO chatDTO = chatService.createChat(userDTO, singleChatRequest.getUserId());
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatDTO> createGroupHandler(@RequestBody GroupChatRequest req, @RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        ChatDTO chatDTO = chatService.createGroup(req, userDTO);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> findChatByIdHandler(@PathVariable Integer chatId) throws ChatException {
        ChatDTO chatDTO = chatService.findChatById(chatId);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ChatDTO>> findAllChatByUserHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        List<ChatDTO> chats = chatService.findAllChatByUserId(userDTO.getId());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ChatDTO> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        ChatDTO chatDTO = chatService.addUserToGroup(userId, chatId, userDTO);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<ChatDTO> removeUserFromGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        ChatDTO chatDTO = chatService.removeFromGroup(chatId, userId, userDTO);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        UserDTO userDTO = userService.findUserByProfile(jwt);
        chatService.deleteChat(chatId, userDTO.getId());
        ApiResponse res = new ApiResponse("Chat is deleted successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
