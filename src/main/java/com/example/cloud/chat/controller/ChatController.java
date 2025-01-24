package com.example.cloud.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    //@MessageMapping("/chat") 어노테이션은 "/chat"으로 들어오는 요청에 대해 감지하고 채팅 메시지를 가져올 수 있도록 한다.
    public void message(@Payload ChattingRequestDto message) throws IOException {

        chatService.sendMessageToPublisher(message);
    }

    @GetMapping("/history/new")
    // 유저가 마지막으로 접속했던 시간을 체크해 그 이후의 채팅 기록을 가져오는 API이다.
    // Redis의 TTL을 잘 설정해야 함.
    public ResponseEntity<List<ChattingResponseDto>> getUnreceivedMessages(
            @RequestParam Long roomId,
            @RequestParam String lastTime // 마지막으로 받은 메시지 시간
    ) {
        // 마지막으로 받은 시간 이후의 메시지를 조회하는 서비스 호출
        Timestamp parsedLastTime = chatService.getParsedLastTime(lastTime);
        List<ChattingResponseDto> unreceivedMessages = chatService.getMessagesAfterLastTime(roomId, parsedLastTime);

        return ResponseEntity.ok(unreceivedMessages);
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoomCreateResponse> getOrCreateChatRoom(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam  String roomTitle
    ) {
        ChatRoomCreateResponse chatRoomCreateResponse=

                chatService.getOrCreateChatRoom(userDetail.getUuid(),roomTitle);

        return ResponseEntity.ok(chatRoomCreateResponse);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRoomList(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        return ResponseEntity.ok(chatService.getChatRoomList(userDetail.getUuid()));
    }

    @PostMapping("/leave")
    public void leaveRoom(Long roomId) {
        chatService.leaveRoom(roomId);
    }
}