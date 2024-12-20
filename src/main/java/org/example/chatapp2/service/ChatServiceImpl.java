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
import java.util.Optional;

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
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if (chat.isPresent()){
            return chat.get();
        }
        throw new ChatException("Chat not found with id "+ chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {

        User user = userService.findUserById(userId);

        List<Chat> chats = chatRepository.findChatByUserId(user.getId());

        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {

        Chat group =  new Chat();
        group.setGroup(true);
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);

        for (Integer userId:req.getUserIds()){
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }

        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {

        Optional<Chat> opt = chatRepository.findById(chatId);

        User user = userService.findUserById(userId);

        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)){
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }
            else{
                throw new UserException("You don't have the admin privileges");
            }
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {

        Optional<Chat> opt = chatRepository.findById(chatId);
        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getUsers().contains(reqUser)){
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are not a member of this group");
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat removeFormGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {

        Optional<Chat> opt = chatRepository.findById(chatId);

        User user = userService.findUserById(userId);

        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)){
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            }
            else if(chat.getUsers().contains(reqUser)){
                if(user.getId().equals(reqUser.getId())){
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }
            throw new UserException("You can't remove another user");

        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {

        Optional<Chat> opt = chatRepository.findById(chatId);

        if (opt.isPresent()){
            Chat chat = opt.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
