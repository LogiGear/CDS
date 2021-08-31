package com.logigear.crm.manager.repository;

import com.logigear.crm.manager.model.Role;
import com.logigear.crm.manager.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>,
        QuerydslPredicateExecutor<Role> {
    @Override
    Optional<Role> findById(Long id);

    Optional<Role> findRoleByName(RoleName roleName);

}
