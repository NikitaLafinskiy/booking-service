package com.booking.bookingservice.config;

import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtSecurityContextFactory implements
        WithSecurityContextFactory<WithJwtMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithJwtMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDto userDto = annotation.isAdmin() ? new UserDto()
                .setId(1L)
                .setEmail("admin@booking.com")
                .setFirstName("Admin")
                .setLastName("User") : new UserDto()
                .setId(2L)
                .setEmail("user@gmail.com")
                .setFirstName("John")
                .setLastName("Doe");
        Collection<? extends GrantedAuthority> authorities = annotation.isAdmin()
                ? List.of(() -> "ROLE_ADMIN")
                : List.of(() -> "ROLE_CUSTOMER");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDto,
                null,
                authorities
        );

        context.setAuthentication(authentication);
        return context;
    }
}
