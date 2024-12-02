package com.booking.bookingservice.domain.notification.service;

public interface TelegramService {
    void sendMessage(String message, Long chatId);

    void getUpdates();
}
