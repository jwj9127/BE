package com.example.cloud.chat.service;

import com.example.cloud.chat.domain.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RedisSubscriber implements MessageListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMesage = (String) redisTemplate.getValueSerializer().deserialize(message.getBody());
            log.info("Received message: {}", publishMesage);

            ChatMessage messageRequest = objectMapper.readValue(publishMesage, ChatMessage.class);
            log.info("Deserialized ChatMessage: {}", messageRequest);

            messagingTemplate.convertAndSend("/subscription/chattings/rooms/" + messageRequest.getRoomId(), messageRequest);
            log.info("Message [{}] send by member: {} to chatting room: {}", messageRequest.getMessage(), messageRequest.getSender(), messageRequest.getRoomId());
        } catch (Exception e) {
            log.error("Error occurred while sending message to chatting room: {}", e.getMessage());
        }
    }
}

