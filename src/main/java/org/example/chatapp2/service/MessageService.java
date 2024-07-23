package org.example.chatapp2.service;

import org.example.chatapp2.dto.MessageDTO;
import org.example.chatapp2.entities.Message;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.MessageException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.SendMessageRequest;

import java.util.List;

public interface MessageService {

    // Changed to return MessageDTO for consistency
    MessageDTO sendMessage(SendMessageRequest req) throws UserException, ChatException;

    // Changed to return List<MessageDTO> for consistency
    List<MessageDTO> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;

    // Changed to return MessageDTO for consistency
    Message findMessageById(Integer messageId) throws MessageException;

    void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;

    MessageDTO convertToDTO(Message message);

    Message convertToEntity(MessageDTO messageDTO);
}
