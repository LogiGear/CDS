package com.logigear.crm.employees.repository;

import com.logigear.crm.employees.model.Role;
import com.logigear.crm.employees.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

	Optional<User> findByEmailToken(String token);
    List<User> findByIdIn(List<Long> userIds);
    Boolean existsByEmail(String email);
    
    @Query("SELECT user FROM User user WHERE user.passwordResetToken=?1")
    Optional<User> findByPasswordResetToken(String passwordResetRoken);

    @Query("SELECT MAX(c.id) FROM User c")
    int findMaxUserId();

    Optional<User> findById(long id);
    
    @Query("SELECT u FROM User u join Role r WHERE r.id = 2")
    List<User> findUserbyRole(long roleId);
    
    @Query( nativeQuery = true, value = "SELECT r.id, r.name " +
            "FROM roles r, users_roles ur " +
            "WHERE ur.user_id = :userId AND ur.role_id = r.id")
    HashSet<Role> findByUserId(long userId);
}
