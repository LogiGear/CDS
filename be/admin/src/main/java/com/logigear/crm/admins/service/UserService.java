package com.logigear.crm.admins.service;

import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import com.logigear.crm.admins.payload.UserUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
	
	/**
	 * Allow the register user info
	 * @return the resulting User object
	 */
	void save(User user);
	public User findUserById(Long id);
	public List<User> findAll();
	public User updateUserRoles(UserUpdateRequest req);
	List<Role> getUserByRole(RoleName roleName);
	User findUserByEmail(String email);
}
