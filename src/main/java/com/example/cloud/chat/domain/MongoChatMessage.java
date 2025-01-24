package com.example.cloud.chat.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Document(collection = "chat_messages")
public class MongoChatMessage {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;

    public MongoChatMessage(String roomId, String sender, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}