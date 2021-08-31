package com.logigear.crm.employees.service;

import com.logigear.crm.employees.model.Role;
import com.logigear.crm.employees.model.RoleName;
import com.logigear.crm.employees.model.User;
import com.logigear.crm.employees.payload.UserUpdateRequest;

import java.util.List;

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
