package com.logigear.crm.manager.service.impl;

import com.logigear.crm.manager.exception.ResourceNotFoundException;
import com.logigear.crm.manager.model.User;
import com.logigear.crm.manager.repository.UserRepository;
import com.logigear.crm.manager.service.UserService;
import com.logigear.crm.manager.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }


}
