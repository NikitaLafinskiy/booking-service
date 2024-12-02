package com.booking.bookingservice.domain.notification.handler;

import com.booking.bookingservice.domain.notification.service.impl.TelegramServiceImpl;

public interface NotificationInputHandler {
    String handle(String userInput, Long chatId);

    TelegramServiceImpl.ChatState getKey();
}
