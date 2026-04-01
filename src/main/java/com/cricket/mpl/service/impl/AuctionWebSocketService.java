package com.cricket.mpl.service.impl;

import com.cricket.mpl.dto.response.PlayerBidResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuctionWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public AuctionWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendBidUpdate(PlayerBidResponse response) {

        String destination = "/topic/auction/" + response.getAuctionId();

        // 🔍 Log 1: Method entry
        System.out.println("🚀 [WS] sendBidUpdate() called");

        // 🔍 Log 2: Payload
        System.out.println("📦 [WS] Payload: " + response);

        // 🔍 Log 3: Destination
        System.out.println("📡 [WS] Sending to: " + destination);

        messagingTemplate.convertAndSend(destination, response);

        // 🔍 Log 4: After send
        System.out.println("✅ [WS] Message sent successfully");
    }
}
