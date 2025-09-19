package com.userchat.user_service.websocket;

import com.userchat.user_service.security.JwtUtil;
import com.userchat.user_service.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresenceChannelInterceptor implements ChannelInterceptor {
    private final StatusService statusService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != null && accessor.getCommand().getMessageType().name().equals("CONNECT")) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                String email = JwtUtil.extractEmail(token.substring(7));
                accessor.getSessionAttributes().put("userId", email);
            }
        }
        return message;
    }
}
