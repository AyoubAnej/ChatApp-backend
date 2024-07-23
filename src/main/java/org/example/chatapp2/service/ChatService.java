package org.example.chatapp2.service;

import org.example.chatapp2.dto.ChatDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    public ChatDTO createChat(User reqUser, Integer userId2) throws UserException;
    public ChatDTO findChatById(Integer chatId) throws UserException, ChatException;
    public List<ChatDTO> findAllChatByUserId(Integer userId) throws UserException;
    public ChatDTO createGroup(GroupChatRequest req, User reqUser) throws UserException;
    public ChatDTO addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    public ChatDTO renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException;
    public ChatDTO removeFormGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;
    public void deleteChat(Integer chatId, Integer userId)  throws UserException, ChatException;

    public ChatDTO convertToDTO(Chat chat);
    public Chat convertToEntity(ChatDTO chatDTO) throws UserException;
}
