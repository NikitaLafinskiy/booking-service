package com.booking.bookingservice.domain.notification.handler.impl;

import com.booking.bookingservice.domain.notification.handler.NotificationInputHandler;
import com.booking.bookingservice.domain.notification.service.TelegramService;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationInputHandler implements NotificationInputHandler {
    private final UserRepository userRepository;

    @Override
    public String handle(String userInput, Long chatId) {
        Optional<User> optionalUser = userRepository.findByEmail(userInput);

        if (optionalUser.isEmpty()) {
            return "An account with this email does not exist. Please provide a different email";
        }

        User user = optionalUser.get();
        user.setTelegramChatId(chatId);
        userRepository.save(user);

        return "The user has been registered successfully: "
                + user.getFirstName()
                + user.getLastName();
    }

    @Override
    public TelegramService.ChatState getKey() {
        return TelegramService.ChatState.REGISTRATION;
    }
}
