package com.userchat.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class HeartbeatService {
    private final StringRedisTemplate redisTemplate;
    private static final long TTL = 30; // secondes

    public void receiveHeartbeat(Long userId) {
        // Met à jour le statut online et reset le TTL
        redisTemplate.opsForValue().set("user:" + userId + ":status", "online", TTL, TimeUnit.SECONDS);
    }
}
