package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.repositories.ChatRepository;
import org.example.chatapp2.request.GroupChatRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService{

    private ChatRepository chatRepository;
    private UserService userService;

    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {

        User user = userService.findUserById(userId2);

        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);

        if (isChatExist != null){
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);

        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws UserException {
        return null;
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        return null;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, Integer reqUser) throws UserException {
        return null;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId) throws UserException, ChatException {
        return null;
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws UserException, ChatException {
        return null;
    }

    @Override
    public Chat removeFormGroup(Integer chatId, Integer userId, Integer reqUser) throws UserException, ChatException {
        return null;
    }

    @Override
    public Chat deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
        return null;
    }
}
