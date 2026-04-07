package com.cricket.mpl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker


public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${web-socket.config.allowed.origins}")
    private String wsAllowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");   // where clients subscribe
        config.setApplicationDestinationPrefixes("/app"); // where clients send
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-auction")
                .setAllowedOrigins(this.wsAllowedOrigins)
                .withSockJS(); // for fallback
    }
}
