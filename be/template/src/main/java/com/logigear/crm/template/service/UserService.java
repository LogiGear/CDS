package com.logigear.crm.template.service;

import com.logigear.crm.template.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User updateUser(User user);
}
