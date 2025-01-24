package com.example.cloud.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageDTO implements Serializable {
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

}