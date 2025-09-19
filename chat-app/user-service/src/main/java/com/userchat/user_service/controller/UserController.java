package com.userchat.user_service.controller;

import com.userchat.user_service.model.User;
import com.userchat.user_service.security.JwtUtil;
import com.userchat.user_service.service.StatusService;
import com.userchat.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StatusService statusService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        User u = userService.login(user.getEmail(), user.getPassword());
        if (u != null) {
            statusService.setUserOnline(u.getId());
            String token = JwtUtil.generateToken(u.getEmail());
            return Map.of("token", token, "message", "Login successful");
        }
        return Map.of("error", "Invalid credentials");
    }

    @PostMapping("/logout/{id}")
    public String logout(@PathVariable Long id) {
        statusService.setUserOffline(id);
        return "User logged out";
    }

    @GetMapping("/{id}/status")
    public Map<String, String> getStatus(@PathVariable Long id) {
        return Map.of(
                "status", statusService.getStatus(id),
                "lastSeen", Optional.ofNullable(statusService.getLastSeen(id)).orElse("N/A")
        );
    }

    @PostMapping("/{id}/heartbeat")
    public String heartbeat(@PathVariable Long id) {
        statusService.refreshUserStatus(id);
        return "Heartbeat received";
    }
}
