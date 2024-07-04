package org.example.chatapp2.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupChatRequest {

    private List<Integer> userIds;
    private String chatName;
    private String chatImage;
}
