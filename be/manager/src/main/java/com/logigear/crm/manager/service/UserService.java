package com.logigear.crm.manager.service;

import com.logigear.crm.manager.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User updateUser(User user);
}
