package com.example.cloud.chat.controller;

import com.example.cloud.chat.dto.ChatMessageDTO;
import com.example.cloud.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChannelTopic channelTopic;

        @MessageMapping("/chat.sendMessage")
        public void sendMessage(ChatMessageDTO message) {
            redisPublisher.publish(channelTopic, message);
        }

}