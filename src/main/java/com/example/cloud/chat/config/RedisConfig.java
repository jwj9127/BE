package com.example.cloud.chat.config;

import com.example.cloud.service.chat.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
public class RedisConfig {

    private static final String TOPIC_NAME = "chatTopic";

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    // Redis 메시지 수신 및 리스너에 전달하는 컨테이너 설정 빈 등록
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter listenerAdapter,
                                                                       ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    // RedisSubscriber 클래스를 사용해 메시지를 처리하는 리스너 설정
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    // Redis 데이터를 직렬화하고 역직렬화하는 데 사용
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //Key를 String으로 저장
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Hash Key도 String으로 저장
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Value를 JSON으로 저장
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        // Hash Value를 JSON으로 저장
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    @Bean
    // Redis 서버 연결 설정 및 관리 빈 등록
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    // 채널 토픽 설정 빈 등록
    public ChannelTopic channelTopic() {
        return new ChannelTopic(TOPIC_NAME);
    }
}
