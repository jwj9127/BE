package com.example.cloud.chat.util;

import com.example.cloud.chat.domain.ChatRoom;
import com.example.cloud.chat.domain.MongoChatMessage;
import com.example.cloud.chat.domain.RedisChatMessage;
import com.example.cloud.chat.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMigrationService {

    private final ChatService chatService;
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 * * * *")  // 매시간 실행
    public void migrateOldChats() {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").lt(LocalDateTime.now().minusHours(24)));
        List<ChatRoom> rooms = mongoTemplate.find(query, ChatRoom.class, "chat_rooms");

        for (ChatRoom room : rooms) {
            List<MongoChatMessage> messages = chatService.getChatHistory(room.getRoomId());
            String collectionName = "chat_messages_" + room.getRoomId() + "_" + room.getCreatedAt().toLocalDate();
            mongoTemplate.insert(messages, collectionName);
            redisTemplate.delete("chat:" + room.getRoomId());
        }
    }
}