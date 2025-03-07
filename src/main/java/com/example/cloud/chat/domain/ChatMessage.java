package com.example.cloud.chat.domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_messages")
public class ChatMessage {
    private String id;
    private String roomId;
    private String sender;
    private String messageContent;
    private LocalDateTime timestamp;

    public ChatMessage(String roomId, String sender, String message, LocalDateTime timestamp) {
        this.roomId = roomId;
        this.sender = sender;
        this.messageContent = message;
        this.timestamp = timestamp;
    }

}