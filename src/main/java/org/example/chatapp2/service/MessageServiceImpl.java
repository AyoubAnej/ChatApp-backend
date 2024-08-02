package org.example.chatapp2.service;

import lombok.AllArgsConstructor;
import org.example.chatapp2.dto.MessageDTO;
import org.example.chatapp2.entities.Chat;
import org.example.chatapp2.entities.Message;
import org.example.chatapp2.entities.User;
import org.example.chatapp2.exception.ChatException;
import org.example.chatapp2.exception.MessageException;
import org.example.chatapp2.exception.UserException;
import org.example.chatapp2.repositories.ChatRepository;
import org.example.chatapp2.repositories.MessageRepository;
import org.example.chatapp2.request.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final ChatRepository chatRepository;
    @Autowired
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public MessageDTO sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.convertToEntity(userService.findUserById(req.getUserId()));
        Chat chat = chatRepository.findById(req.getChatId())
                .orElseThrow(() -> new ChatException("Chat not found with id " + req.getChatId()));

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
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id " + chatId));

//        if (!chat.getUsers().contains(reqUser)) {
//            log.warn("User {} is not related to chat {}", reqUser.getId(), chat.getId());
//            throw new UserException("You are not related to this chat " + chat.getId());
//        }

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
    public Message convertToEntity(MessageDTO messageDTO) throws UserException, ChatException {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setContent(messageDTO.getContent());
        message.setTimeStamp(LocalDateTime.parse(messageDTO.getTimestamp())); // Ensure timestamp is properly formatted

        User user = userService.convertToEntity(userService.findUserById(messageDTO.getSenderId()));
        Chat chat = chatRepository.findById(messageDTO.getChatId())
                .orElseThrow(() -> new ChatException("Chat not found with id " + messageDTO.getChatId()));

        message.setUser(user);
        message.setChat(chat);

        return message;
    }
}
