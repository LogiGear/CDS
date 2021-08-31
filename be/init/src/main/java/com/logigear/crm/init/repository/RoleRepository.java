package com.logigear.crm.init.repository;

import com.logigear.crm.init.model.Role;
import com.logigear.crm.init.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(RoleName roleName);
    Role findByName(RoleName roleName);
}
