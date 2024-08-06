package org.example.chatapp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Integer id;
    private String content;
    private Integer senderId;
    private Integer chatId;
    private String timestamp;
}
