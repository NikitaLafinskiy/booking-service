package com.booking.bookingservice.domain.notification.service.impl;

import com.booking.bookingservice.domain.notification.service.TelegramService;
import com.booking.bookingservice.domain.notification.strategy.NotificationStrategy;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final TelegramBot telegramBot;
    private final NotificationStrategy notificationStrategy;

    @PostConstruct
    public void init() {
        getUpdates();
    }

    @Override
    public void sendMessage(String message, Long chatId) {
        telegramBot.execute(new SendMessage(chatId, message));
    }

    @Override
    public void getUpdates() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                Message message = update.message();
                if (message != null) {
                    sendMessage(handleArrivingMessage(message),
                            message.chat().id());
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @PreDestroy
    public void destroy() {
        telegramBot.removeGetUpdatesListener();
    }

    private String handleArrivingMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        return notificationStrategy.handleText(chatId, text);
    }
}
