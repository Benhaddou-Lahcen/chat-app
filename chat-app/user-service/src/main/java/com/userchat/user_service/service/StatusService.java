package com.userchat.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StringRedisTemplate redisTemplate;
    private static final long TTL = 30; // secondes

    public void setUserOnline(Long userId) {
        redisTemplate.opsForValue().set("user:" + userId + ":status", "online", TTL, TimeUnit.SECONDS);
    }

    public void refreshUserStatus(Long userId) {
        setUserOnline(userId); // reset TTL
    }

    public void setUserOffline(Long userId) {
        redisTemplate.opsForValue().set("user:" + userId + ":status", "offline");
        redisTemplate.opsForValue().set("user:" + userId + ":lastSeen", Instant.now().toString());
    }

    public String getStatus(Long userId) {
        String status = redisTemplate.opsForValue().get("user:" + userId + ":status");
        return status != null ? status : "offline";
    }

    public String getLastSeen(Long userId) {
        return redisTemplate.opsForValue().get("user:" + userId + ":lastSeen");
    }
}
