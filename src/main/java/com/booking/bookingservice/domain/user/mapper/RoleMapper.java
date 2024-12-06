package com.booking.bookingservice.domain.user.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.user.model.Role;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    default Set<Role.RoleType> toRoleTypes(List<String> roles) {
        return roles.stream()
                .map(role -> Role.RoleType.valueOf(role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}
