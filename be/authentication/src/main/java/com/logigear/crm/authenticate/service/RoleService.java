package com.logigear.crm.authenticate.service;

import org.springframework.stereotype.Service;

import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
	
	private RoleRepository roleRepository; 
	
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
}
