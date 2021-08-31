package com.logigear.crm.authenticate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.model.RoleName;

import java.util.HashSet;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

    boolean existsByName(RoleName roleName);

    @Query( nativeQuery = true, value = "SELECT r.id, r.name " +
            "FROM roles r, users_roles ur " +
            "WHERE ur.user_id = :userId AND ur.role_id = r.id")
    HashSet<Role> findByUserId(long userId);
}
