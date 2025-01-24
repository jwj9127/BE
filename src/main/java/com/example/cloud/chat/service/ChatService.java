package com.example.cloud.chat.service;

import com.example.cloud.chat.config.RedisConfig;
import com.example.cloud.chat.domain.MongoChatMessage;
import com.example.cloud.chat.domain.RedisChatMessage;
import com.example.cloud.chat.dto.ChatMessageDTO;
import com.example.cloud.chat.repository.MongoChatRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoChatRepository mongoChatRepository;

    public void saveMessage(ChatMessageDTO messageDto) {
        RedisChatMessage redisMessage = new RedisChatMessage(
                messageDto.getRoomId(),
                messageDto.getSender(),
                messageDto.getMessage()
        );
        redisTemplate.opsForValue().set(messageDto.getRoomId() + ":" + messageDto.getTimestamp(), redisMessage);
    }

    public List<MongoChatMessage> getChatHistory(String roomId) {
        // Redis에서 먼저 조회
        List<Object> redisMessages = redisTemplate.opsForList().range(roomId, 0, -1);
        List<RedisChatMessage> changeRedisMessages = new ArrayList<>();
        if (!redisMessages.isEmpty()) {
            for ( Object redisMessage : redisMessages) {
                log.info("Redis message: {}", redisMessages);
                changeRedisMessages.add((RedisChatMessage) redisMessage);
            }

            return changeRedisMessages.stream()
                    .map(m -> new MongoChatMessage(m.getRoomId(), m.getSender(), m.getMessage()))
                    .toList();
        }

        // 24시간 초과 시 MongoDB에서 조회
        return mongoChatRepository.findByRoomId(roomId);
    }

    // TTL 만료 전에 MongoDB로 데이터 이전
    @Scheduled(fixedRate = 86400000) // 하루마다 실행
    public void migrateRedisToMongo() {
        // 예제 코드: Redis에 저장된 모든 메시지 처리
        Set<String> keys = redisTemplate.keys("chat:*");
        if (keys != null) {
            for (String key : keys) {
                RedisChatMessage redisMessage = (RedisChatMessage) redisTemplate.opsForValue().get(key);
                if (redisMessage != null && LocalDateTime.now().minusHours(24).isAfter(redisMessage.getTimestamp())) {
                    mongoChatRepository.save(new MongoChatMessage(redisMessage.getRoomId(), redisMessage.getSender(), redisMessage.getMessage()));
                    redisTemplate.delete(key);
                }
            }
        }
    }
}
