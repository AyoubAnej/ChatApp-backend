package org.example.chatapp2.service;

import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    public Chat createChat(User reqUser, Integer userId2) throws UserException;
    public Chat findChatById(Integer chatId) throws UserException;
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    public Chat createGroup(GroupChatRequest req, Integer reqUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId,  String groupName, Integer reqUserId) throws UserException, ChatException;
    public Chat removeFormGroup(Integer chatId, Integer userId, Integer reqUser) throws UserException, ChatException;
    public Chat deleteChat(Integer chatId, Integer userId)  throws UserException, ChatException;
}
