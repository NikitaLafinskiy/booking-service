package com.booking.bookingservice.domain.user.repository;

import com.booking.bookingservice.domain.user.model.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.role = :roleType")
    Role findByRoleType(Role.RoleType roleType);

    @Query("SELECT r FROM Role r WHERE r.role IN :roleTypes")
    Set<Role> findRolesByRoleTypes(Set<Role.RoleType> roleTypes);
}
