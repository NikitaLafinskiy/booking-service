package com.booking.bookingservice.domain.notification.command.impl;

import com.booking.bookingservice.domain.notification.command.NotificationCommand;
import com.booking.bookingservice.domain.notification.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements NotificationCommand {
    public static final String START_KEY = "/start";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String execute(Long chatId) {
        stringRedisTemplate.opsForValue()
                .set(String.valueOf(chatId),
                        TelegramService.ChatState.INIT.name());
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
