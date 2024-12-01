package com.booking.bookingservice.domain.user.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.user.model.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Named("userById")
    default User userById(Long id) {
        return Optional.ofNullable(id)
                .map(User::new)
                .orElse(null);
    }
}
