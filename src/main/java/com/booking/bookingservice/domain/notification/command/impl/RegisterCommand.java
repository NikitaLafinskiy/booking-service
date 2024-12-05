package com.booking.bookingservice.domain.notification.command.impl;

import com.booking.bookingservice.domain.notification.command.NotificationCommand;
import com.booking.bookingservice.domain.notification.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterCommand implements NotificationCommand {
    public static final String REGISTER_KEY = "/register";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String execute(Long chatId) {
        stringRedisTemplate.opsForValue()
                .set(String.valueOf(chatId),
                TelegramService.ChatState.REGISTRATION.name());

        return "What is your email?";
    }

    @Override
    public String getKey() {
        return REGISTER_KEY;
    }
}
