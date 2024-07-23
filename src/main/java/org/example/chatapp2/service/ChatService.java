package org.example.chatapp2.service;

import org.example.chatapp2.dto.ChatDTO;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    ChatDTO createChat(UserDTO reqUser, Integer userId2) throws UserException;
    ChatDTO findChatById(Integer chatId) throws ChatException;
    List<ChatDTO> findAllChatByUserId(Integer userId) throws UserException;
    ChatDTO createGroup(GroupChatRequest req, UserDTO reqUser) throws UserException;
    ChatDTO addUserToGroup(Integer userId, Integer chatId, UserDTO reqUser) throws UserException, ChatException;
    ChatDTO renameGroup(Integer chatId, String groupName, UserDTO reqUser) throws UserException, ChatException;
    ChatDTO removeFromGroup(Integer chatId, Integer userId, UserDTO reqUser) throws UserException, ChatException;
    void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException;

    ChatDTO convertToDTO(Chat chat);
    Chat convertToEntity(ChatDTO chatDTO) throws UserException;
}
