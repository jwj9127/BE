package com.example.cloud.chat.util;

import com.example.cloud.chat.dto.ChatRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonParser {

    private final ObjectMapper objectMapper;

    public String toJson(ChatRequest chatRequest) {
        try {
            return objectMapper.writeValueAsString(chatRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatRequest toMessageRequest(String chattingMessage) {
        try {
            return objectMapper.readValue(chattingMessage, ChatRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

