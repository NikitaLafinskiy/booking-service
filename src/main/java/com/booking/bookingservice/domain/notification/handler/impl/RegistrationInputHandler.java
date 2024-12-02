package com.booking.bookingservice.domain.notification.handler.impl;

import com.booking.bookingservice.domain.notification.handler.NotificationInputHandler;
import com.booking.bookingservice.domain.notification.service.impl.TelegramServiceImpl;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationInputHandler implements NotificationInputHandler {
    private final UserRepository userRepository;

    @Override
    public String handle(String userInput, Long chatId) {
        User user = userRepository.findByEmail(userInput)
                .orElseThrow(() -> new EntityNotFoundException(
                "User with an email of "
                        + userInput
                        + " not found"));
        user.setTelegramChatId(chatId);
        userRepository.save(user);

        return "The user has been registered successfully: "
                + user.getFirstName()
                + user.getLastName();
    }

    @Override
    public TelegramServiceImpl.ChatState getKey() {
        return TelegramServiceImpl.ChatState.REGISTRATION;
    }
}
