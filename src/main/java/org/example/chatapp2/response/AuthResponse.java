package org.example.chatapp2.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {

    private String jwt;
    private boolean isAuth;

}
