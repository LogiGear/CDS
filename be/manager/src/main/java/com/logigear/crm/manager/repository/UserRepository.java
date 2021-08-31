package com.logigear.crm.manager.repository;

import com.logigear.crm.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>
{
    Optional<User> findById(long id);
    List<User> findAll();
}
