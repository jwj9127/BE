package com.example.cloud.chat.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor( access = AccessLevel.PROTECTED)
public class ChatRequest {
    private Long chattingRoomId;
    private Long senderId;
    private String content;

    @Override
    public String toString() {
        return "ChatRequest{" +
                "content='" + content + '\'' +
                ", senderId=" + senderId +
                '}';
    }
}
