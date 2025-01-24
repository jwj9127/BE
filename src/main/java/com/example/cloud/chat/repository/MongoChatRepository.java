package com.example.cloud.chat.repository;

import com.example.cloud.chat.domain.MongoChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories
public interface MongoChatRepository extends MongoRepository<MongoChatMessage, String> {
    List<MongoChatMessage> findByRoomId(String roomId);
}
