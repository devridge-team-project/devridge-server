package org.devridge.api.common.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.member.exception.AccessTokenInvalidException;
import org.devridge.api.common.util.AccessTokenUtil;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            if (!this.validateAccessToken(accessToken)) {
                throw new AccessTokenInvalidException(401, "access token이 필요합니다.");
            }

            String email = this.getEmail(accessToken);
            accessor.addNativeHeader("senderEmail", email);
        }

        return message;
    }

    private String getEmail(String accessToken) {
        String bearerToken = accessToken.trim();

        if (!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                Claims claims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
                String email = claims.get("memberEmail", String.class);
                return email;
            } catch (ExpiredJwtException | MalformedJwtException e) {
                throw new AccessTokenInvalidException(401, "");
            }
        }

        return null;
    }

    private boolean validateAccessToken(String accessToken) {
        if (accessToken == null) {
            return false;
        }

        String bearerToken = accessToken.trim();

        if (!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                Claims claims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
                return true;
            } catch (ExpiredJwtException | MalformedJwtException e) {
                return false;
            }
        }

        return false;
    }

    @EventListener(SessionConnectEvent.class)
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        if (this.validateAccessToken(accessToken)) {
            String email = this.getEmail(accessToken);
            // Set the memberId as a session attribute
            accessor.getSessionAttributes().put("senderEmail", email);
        }
    }
}