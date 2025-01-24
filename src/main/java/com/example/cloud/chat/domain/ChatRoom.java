package com.example.cloud.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sender;

    private Long receiver;

    private String title;

    private Timestamp lastTime;

    private String senderImage;

    private String receiverImage;

    public static ChatRoom to(Long receiver, Long sender, String findSenderProfileImage, String roomTitle, String findReceiverProfileImage) {
        return ChatRoom.builder()
                .receiver(receiver)
                .sender(sender)
                .senderImage(findSenderProfileImage)
                .title(roomTitle)
                .receiverImage(findReceiverProfileImage)
                .lastTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();

    }
}