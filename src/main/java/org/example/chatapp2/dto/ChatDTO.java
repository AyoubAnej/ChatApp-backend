package org.example.chatapp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private Integer id;
    private String chatName;
    private String chatImage;
    private Set<UserDTO> admins;
    private boolean isGroup;
    private UserDTO createdBy;
    private Set<UserDTO> users;
    private List<MessageDTO> messages;
}
