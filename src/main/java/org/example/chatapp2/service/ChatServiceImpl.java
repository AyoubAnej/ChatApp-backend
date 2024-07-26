package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.ChatDTO;
import org.example.chatapp2.dto.MessageDTO;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.repositories.ChatRepository;
import org.example.chatapp2.request.GroupChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Autowired
    private final ChatRepository chatRepository;
    @Autowired
    private final UserService userService;

    @Override
    public ChatDTO createChat(UserDTO reqUser, Integer userId2) throws UserException {
        User user1 = userService.convertToEntity(reqUser);
        User user2 = userService.convertToEntity(userService.findUserById(userId2));

        Chat existingChat = chatRepository.findSingleChatByUserIds(user1, user2);
        if (existingChat != null) {
            return convertToDTO(existingChat);
        }

        Chat chat = new Chat();
        chat.setCreatedBy(user1);
        chat.getUsers().add(user1);
        chat.getUsers().add(user2);
        chat.setGroup(false);
        chat = chatRepository.save(chat);
        return convertToDTO(chat);
    }

    @Override
    public ChatDTO findChatById(Integer chatId) throws ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        return convertToDTO(chat);
    }

    @Override
    public List<ChatDTO> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.convertToEntity(userService.findUserById(userId));
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ChatDTO createGroup(GroupChatRequest req, UserDTO reqUser) throws UserException {
        User user = userService.convertToEntity(reqUser);

        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(user);
        group.getAdmins().add(user);

        for (Integer userId : req.getUserIds()) {
            User groupUser = userService.convertToEntity(userService.findUserById(userId));
            group.getUsers().add(groupUser);
        }

        group = chatRepository.save(group);
        return convertToDTO(group);
    }

    @Override
    public ChatDTO addUserToGroup(Integer userId, Integer chatId, UserDTO reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        User user = userService.convertToEntity(userService.findUserById(userId));
        User requestingUser = userService.convertToEntity(reqUser);

        if (chat.getAdmins().contains(requestingUser)) {
            chat.getUsers().add(user);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else {
            throw new UserException("You don't have the admin privileges");
        }
    }

    @Override
    public ChatDTO renameGroup(Integer chatId, String groupName, UserDTO reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        User requestingUser = userService.convertToEntity(reqUser);

        if (chat.getUsers().contains(requestingUser)) {
            chat.setChatName(groupName);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else {
            throw new UserException("You are not a member of this group");
        }
    }

    @Override
    public ChatDTO removeFromGroup(Integer chatId, Integer userId, UserDTO reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        User user = userService.convertToEntity(userService.findUserById(userId));
        User requestingUser = userService.convertToEntity(reqUser);

        if (chat.getAdmins().contains(requestingUser)) {
            chat.getUsers().remove(user);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else if (chat.getUsers().contains(requestingUser) && requestingUser.equals(user)) {
            chat.getUsers().remove(user);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else {
            throw new UserException("You can't remove another user");
        }
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        chatRepository.deleteById(chat.getId());
    }

    @Override
    public ChatDTO convertToDTO(Chat chat) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setChatName(chat.getChatName());
        chatDTO.setChatImage(chat.getChatImage());
        chatDTO.setGroup(chat.isGroup());
        chatDTO.setCreatedBy(userService.convertToDTO(chat.getCreatedBy()));
        chatDTO.setUsers(chat.getUsers().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toSet()));
        chatDTO.setAdmins(chat.getAdmins().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toSet()));
        chatDTO.setMessages(chat.getMessages().stream()
                .map(message -> new MessageDTO(message.getId(), message.getContent(), message.getUser().getId(), message.getChat().getId(), message.getTimeStamp().toString()))
                .collect(Collectors.toList()));
        return chatDTO;
    }

    @Override
    public Chat convertToEntity(ChatDTO chatDTO) throws UserException {
        Chat chat = new Chat();
        chat.setId(chatDTO.getId());
        chat.setChatName(chatDTO.getChatName());
        chat.setChatImage(chatDTO.getChatImage());
        chat.setGroup(chatDTO.isGroup());
        chat.setCreatedBy(userService.convertToEntity(chatDTO.getCreatedBy()));
        chat.setUsers(chatDTO.getUsers().stream()
                .map(userService::convertToEntity)
                .collect(Collectors.toSet()));
        chat.setAdmins(chatDTO.getAdmins().stream()
                .map(userService::convertToEntity)
                .collect(Collectors.toSet()));
        return chat;
    }
}
