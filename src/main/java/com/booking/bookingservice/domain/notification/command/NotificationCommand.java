package com.booking.bookingservice.domain.notification.command;

public interface NotificationCommand {
    String execute(Long chatId);

    String getKey();
}
