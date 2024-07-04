package org.example.chatapp2.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetail {

    private String error;
    private String message;
    private LocalDateTime timStamp;
}
