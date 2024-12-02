package com.booking.bookingservice.config;

import com.booking.bookingservice.domain.notification.service.impl.TelegramServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Long, TelegramServiceImpl.ChatState> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, TelegramServiceImpl.ChatState> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
