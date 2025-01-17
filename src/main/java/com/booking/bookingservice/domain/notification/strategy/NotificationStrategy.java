package com.booking.bookingservice.domain.notification.strategy;

import com.booking.bookingservice.domain.notification.command.NotificationCommand;
import com.booking.bookingservice.domain.notification.handler.NotificationInputHandler;
import com.booking.bookingservice.domain.notification.service.TelegramService;
import com.booking.bookingservice.exception.InvalidInputException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationStrategy {
    public static final String COMMAND_PREFIX = "/";

    private final List<NotificationCommand> commandHandlers;
    private final List<NotificationInputHandler> notificationInputHandlers;
    private final StringRedisTemplate stringRedisTemplate;

    public String handleText(Long chatId, String text) {
        if (text.startsWith(COMMAND_PREFIX)) {
            NotificationCommand commandHandler = commandHandlers.stream()
                    .filter(handler -> handler
                            .getKey()
                            .equals(text))
                    .findFirst()
                    .orElseThrow(() -> new InvalidInputException("Unknown text"));
            return commandHandler.execute(chatId);
        } else {
            TelegramService.ChatState chatState = TelegramService.ChatState
                    .valueOf(stringRedisTemplate.opsForValue()
                            .get(String.valueOf(chatId)));
            NotificationInputHandler notificationInputHandler = notificationInputHandlers.stream()
                    .filter(handler -> handler
                            .getKey()
                            .equals(chatState))
                    .findFirst()
                    .orElseThrow(() -> new InvalidInputException("Incorrect user input"));
            return notificationInputHandler.handle(text, chatId);
        }
    }
}
