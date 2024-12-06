package com.booking.bookingservice.domain.user.dto;

import com.booking.bookingservice.domain.user.model.Role;
import com.booking.bookingservice.validator.enumlistmatch.EnumListMatch;
import java.util.List;
import lombok.Data;

@Data
public class UpdateUserRolesRequestDto {
    @EnumListMatch(enumClass = Role.RoleType.class)
    private List<String> roles;
}
