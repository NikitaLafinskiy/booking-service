package com.booking.bookingservice.domain.user.repository;

import com.booking.bookingservice.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.role = :roleType")
    Role findByRoleType(Role.RoleType roleType);
}
