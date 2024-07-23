package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.ChatDTO;
import org.example.chatapp2.dto.MessageDTO;
import org.example.chatapp2.dto.UserDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.Message;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.MessageException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.repositories.MessageRepository;
import org.example.chatapp2.request.SendMessageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    @Override
    public MessageDTO sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.convertToEntity(userService.findUserById(req.getUserId()));
        Chat chat = chatService.convertToEntity(chatService.findChatById(req.getChatId()));

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimeStamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    @Override
    public List<MessageDTO> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.convertToEntity(chatService.findChatById(chatId));

        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("You are not related to this chat " + chat.getId());
        }

        List<Message> messages = messageRepository.findByChatId(chatId);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("Message not found with id " + messageId));
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);

        if (message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
        } else {
            throw new UserException("You can't delete another user's message " + reqUser.getFull_name());
        }
    }

    @Override
    public MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setSenderId(message.getUser().getId());
        messageDTO.setChatId(message.getChat().getId());
        messageDTO.setTimestamp(message.getTimeStamp().toString()); // Assuming timestamp is in ISO format
        return messageDTO;
    }

    @Override
    public Message convertToEntity(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setContent(messageDTO.getContent());
        message.setTimeStamp(LocalDateTime.parse(messageDTO.getTimestamp())); // Ensure timestamp is properly formatted

        User user = null;
        try {
            user = userService.convertToEntity(userService.findUserById(messageDTO.getSenderId()));
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        Chat chat = null;
        try {
            chat = chatService.convertToEntity(chatService.findChatById(messageDTO.getChatId()));
        } catch (UserException e) {
            throw new RuntimeException(e);
        } catch (ChatException e) {
            throw new RuntimeException(e);
        }

        message.setUser(user);
        message.setChat(chat);

        return message;
    }
}

