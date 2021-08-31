package com.logigear.crm.init.repository;

import com.logigear.crm.init.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNameAndEmail(String name,String email);

    Boolean existsByNameAndEmail(String name,String email);
}
