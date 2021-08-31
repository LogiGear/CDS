package com.logigear.crm.authenticate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.logigear.crm.authenticate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailContains(String email);
	Optional<User> findByEmailToken(String token);
    List<User> findByIdIn(List<Long> userIds);
    Boolean existsByEmail(String email);
    
    @Query("SELECT user FROM User user WHERE user.passwordResetToken=?1")
    Optional<User> findByPasswordResetToken(String passwordResetRoken);

    @Query("SELECT MAX(c.id) FROM User c")
    int findMaxUserId();
}
