package com.logigear.crm.admins.repository;

import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleName roleName);

    boolean existsByName(RoleName roleName);

    @Query( nativeQuery = true, value = "SELECT r.id, r.name " +
            "FROM roles r, users_roles ur " +
            "WHERE ur.user_id = :userId AND ur.role_id = r.id")
    HashSet<Role> findByUserId(long userId);
    
    @Query("SELECT u FROM Role u WHERE u.id = :id")
	List<Role> findByRole(long id);
    
    @Query( nativeQuery = true, value ="SELECT r.id, r.name FROM users u join users_roles ur"
    		+ " on u.id = ur.user_id join roles r on"
    		+ " ur.role_id = r.id WHERE u.id = :id")
    List<Role> findListRoleByUser(long id);
}
