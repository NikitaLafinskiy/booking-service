package com.booking.bookingservice.domain.notification.command.impl;

import com.booking.bookingservice.domain.notification.command.NotificationCommand;
import com.booking.bookingservice.domain.notification.service.impl.TelegramServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterCommand implements NotificationCommand {
    public static final String REGISTER_KEY = "/register";

    private final RedisTemplate<Long, TelegramServiceImpl.ChatState> redisTemplate;

    @Override
    public String execute(Long chatId) {
        redisTemplate.opsForValue().set(chatId,
                TelegramServiceImpl.ChatState.REGISTRATION);

        return "What is your email?";
    }

    @Override
    public String getKey() {
        return REGISTER_KEY;
    }
}
