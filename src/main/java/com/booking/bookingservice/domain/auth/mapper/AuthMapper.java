package com.booking.bookingservice.domain.auth.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    User toUserFromRegisterUserRequestDto(RegisterUserRequestDto registerUserRequestDto);
}
