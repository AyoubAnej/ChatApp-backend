package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.ChatDTO;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    @Autowired
    private final MessageService messageService;

    @Override
    public ChatDTO createChat(User reqUser, Integer userId2) throws UserException {
        UserDTO userDTO = userService.findUserById(userId2);
        User user = userService.convertToEntity(userDTO);

        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);

        if (isChatExist != null) {
            return convertToDTO(isChatExist);
        }
        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
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
        UserDTO userDTO = userService.findUserById(userId);
        User user = userService.convertToEntity(userDTO);
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ChatDTO createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);

        for (Integer userId : req.getUserIds()) {
            UserDTO userDTO = userService.findUserById(userId);
            User user = userService.convertToEntity(userDTO);
            group.getUsers().add(user);
        }

        group = chatRepository.save(group);
        return convertToDTO(group);
    }

    @Override
    public ChatDTO addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        UserDTO userDTO = userService.findUserById(userId);
        User user = userService.convertToEntity(userDTO);

        if (chat.getAdmins().contains(reqUser)) {
            chat.getUsers().add(user);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else {
            throw new UserException("You don't have the admin privileges");
        }
    }

    @Override
    public ChatDTO renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));

        if (chat.getUsers().contains(reqUser)) {
            chat.setChatName(groupName);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else {
            throw new UserException("You are not a member of this group");
        }
    }

    @Override
    public ChatDTO removeFormGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));
        UserDTO userDTO = userService.findUserById(userId);
        User user = userService.convertToEntity(userDTO);

        if (chat.getAdmins().contains(reqUser)) {
            chat.getUsers().remove(user);
            chat = chatRepository.save(chat);
            return convertToDTO(chat);
        } else if (chat.getUsers().contains(reqUser)) {
            if (user.getId().equals(reqUser.getId())) {
                chat.getUsers().remove(user);
                chat = chatRepository.save(chat);
                return convertToDTO(chat);
            }
        }
        throw new UserException("You can't remove another user");
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

        // Convert createdBy to UserDTO
        chatDTO.setCreatedBy(userService.convertToDTO(chat.getCreatedBy()));

        // Convert users and admins to Set<UserDTO>
        chatDTO.setUsers(chat.getUsers().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toSet()));

        chatDTO.setAdmins(chat.getAdmins().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toSet()));

        // Convert messages to List<MessageDTO>
        // Assume a similar method exists in a MessageService for message conversion
        chatDTO.setMessages(chat.getMessages().stream()
                .map(messageService::convertToDTO)
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

        // Convert createdBy from UserDTO to User
        chat.setCreatedBy(userService.convertToEntity(chatDTO.getCreatedBy()));

        // Convert users and admins from Set<UserDTO> to Set<User>
        chat.setUsers(chatDTO.getUsers().stream()
                .map(userService::convertToEntity)
                .collect(Collectors.toSet()));

        chat.setAdmins(chatDTO.getAdmins().stream()
                .map(userService::convertToEntity)
                .collect(Collectors.toSet()));

        // Convert messages from List<MessageDTO> to List<Message>
        // Assume a similar method exists in a MessageService for message conversion
        chat.setMessages(chatDTO.getMessages().stream()
                .map(messageService::convertToEntity)
                .collect(Collectors.toList()));

        return chat;
    }
}
