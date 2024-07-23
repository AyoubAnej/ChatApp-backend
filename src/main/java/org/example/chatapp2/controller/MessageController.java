package org.example.chatapp2.controller;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.MessageDTO;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.MessageException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.SendMessageRequest;
import org.example.chatapp2.response.ApiResponse;
import org.example.chatapp2.service.MessageService;
import org.example.chatapp2.service.UserService;
import org.example.chatapp2.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<MessageDTO> sendMessageHandler(@RequestBody SendMessageRequest req, @RequestHeader("Authorization") String jwt) {
        try {
            UserDTO userDTO = userService.findUserByProfile(jwt);
            req.setUserId(userDTO.getId());

            // Convert UserDTO to User
            User user = userService.convertToEntity(userDTO);

            // Convert ChatDTO to Chat
            Chat chat = chatService.convertToEntity(chatService.findChatById(req.getChatId()));

            // Send message
            MessageDTO messageDTO = messageService.sendMessage(req);
            return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        } catch (UserException | ChatException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getChatsMessagesHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) {
        try {
            UserDTO userDTO = userService.findUserByProfile(jwt);
            // Convert UserDTO to User
            User user = userService.convertToEntity(userDTO);

            // Fetch messages
            List<MessageDTO> messages = messageService.getChatsMessages(chatId, user);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (UserException | ChatException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId, @RequestHeader("Authorization") String jwt) {
        try {
            UserDTO userDTO = userService.findUserByProfile(jwt);
            // Convert UserDTO to User
            User user = userService.convertToEntity(userDTO);

            messageService.deleteMessage(messageId, user);
            ApiResponse response = new ApiResponse("Message deleted successfully", false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserException | MessageException e) {
            ApiResponse response = new ApiResponse("Failed to delete message: " + e.getMessage(), true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
