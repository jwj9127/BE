package com.example.cloud.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatMessage {
    private Type type;
    private String roomId;
    private String sender;
    private String message;
}
