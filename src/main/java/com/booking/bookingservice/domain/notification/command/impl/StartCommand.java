package com.booking.bookingservice.domain.notification.command.impl;

import com.booking.bookingservice.domain.notification.command.NotificationCommand;
import com.booking.bookingservice.domain.notification.service.impl.TelegramServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements NotificationCommand {
    public static final String START_KEY = "/start";

    private final RedisTemplate<Long, TelegramServiceImpl.ChatState> redisTemplate;

    @Override
    public String execute(Long chatId) {
        redisTemplate.opsForValue().set(chatId, TelegramServiceImpl.ChatState.INIT);
        return """
                Welcome to the Booking Service! List of commands:
                /start - start the bot
                /register - register with an email
                """;
    }

    @Override
    public String getKey() {
        return START_KEY;
    }
}
