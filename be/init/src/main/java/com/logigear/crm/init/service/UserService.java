package com.logigear.crm.init.service;

import com.logigear.crm.init.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User updateUser(User user);
}
