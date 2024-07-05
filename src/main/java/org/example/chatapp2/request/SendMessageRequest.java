package org.example.chatapp2.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SendMessageRequest {

    private Integer userId;
    private Integer chatId;
    private String content;
}
