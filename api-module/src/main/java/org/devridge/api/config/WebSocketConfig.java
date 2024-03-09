package org.devridge.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    /**
     * 해당 엔드포인트로 요청 시 소켓 연결
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/ws-stomp")
                .setAllowedOriginPatterns("*");
    }

    /**
     * 메시지 브로커 설정을 위한 메소드
     * /api/subscribe -> 메세지 구독(수신)
     * /api/publish -> 메세지 발행(송신)
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/api/coffee-chat/sub");
        /* 메시지 앞에 해당 prefix로 해당 경로를 처리하고 있는 핸들러로 전달된다. */
        registry.setApplicationDestinationPrefixes("/api/coffee-chat/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
