package com.example.cloud.chat.service;

import com.example.cloud.chat.domain.MongoChatMessage;
import com.example.cloud.chat.dto.ChatMessageDTO;
import com.example.cloud.chat.repository.MongoChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoChatRepository mongoChatRepository;

    public void saveChatMessage(ChatMessageDTO chatMessage) {
        redisTemplate.opsForList().rightPush("chat:" + chatMessage.getRoomId(), chatMessage);
    }

    public List<MongoChatMessage> getChatHistory(String roomId, LocalDateTime chatDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.minusHours(24).isBefore(chatDate)) {
            List<Object> messages = redisTemplate.opsForList().range("chat:" + roomId, 0, -1);
            if (messages != null && !messages.isEmpty()) {
                return messages.stream().map(msg -> (MongoChatMessage) msg).toList();
            }
        }
        return mongoChatRepository.findByRoomId(roomId);
    }
}