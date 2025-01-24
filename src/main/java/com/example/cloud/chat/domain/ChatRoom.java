package com.example.cloud.chat.domain;

import com.example.cloud.oauth2.entity.SocialUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "chat_rooms")
@Getter
public class ChatRoom {
    @Id
    private String id;
    private String roomId;
    private String studyGroupName;
    private LocalDateTime createdAt;
    private List<SocialUserEntity> members;

    public ChatRoom(String roomId, String studyGroupName, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.studyGroupName = studyGroupName;
        this.createdAt = createdAt;
        this.members = new ArrayList<>();
    }

    public void addMember(SocialUserEntity member) {
        this.members.add(member);
    }
}