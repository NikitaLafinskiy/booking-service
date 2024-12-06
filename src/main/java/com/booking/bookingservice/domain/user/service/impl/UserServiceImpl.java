package com.booking.bookingservice.domain.user.service.impl;

import com.booking.bookingservice.domain.user.dto.UpdateUserRequestDto;
import com.booking.bookingservice.domain.user.dto.UpdateUserRolesRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.mapper.RoleMapper;
import com.booking.bookingservice.domain.user.mapper.UserMapper;
import com.booking.bookingservice.domain.user.model.Role;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.RoleRepository;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.domain.user.service.UserService;
import com.booking.bookingservice.exception.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public UserDto me(UserDto userDto) {
        return userDto;
    }

    @Override
    public UserDto updateMe(UserDto userDto, UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id "
                                + userDto.getId()
                                + " not found"));
        userMapper.updateUser(user, updateUserRequestDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUserRoles(
            Long userId,
            UpdateUserRolesRequestDto updateUserRolesRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id "
                                + userId
                                + " not found"));
        Set<Role.RoleType> roleTypes = roleMapper.toRoleTypes(
                updateUserRolesRequestDto.getRoles());
        Set<Role> roles = roleRepository.findRolesByRoleTypes(roleTypes);
        user.setRoles(roles);
        return userMapper.toDto(userRepository.save(user));
    }
}
