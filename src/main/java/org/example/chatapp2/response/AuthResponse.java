package org.example.chatapp2.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthResponse {

    private String jwt;
    private boolean isAuth;

}
