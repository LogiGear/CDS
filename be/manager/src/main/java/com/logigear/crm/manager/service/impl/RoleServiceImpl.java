package com.logigear.crm.manager.service.impl;

import com.logigear.crm.manager.exception.ResourceNotFoundException;
import com.logigear.crm.manager.model.Role;
import com.logigear.crm.manager.model.RoleName;
import com.logigear.crm.manager.repository.RoleRepository;
import com.logigear.crm.manager.service.RoleService;
import com.logigear.crm.manager.util.MessageUtil;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private  final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(RoleName roleName) {
        return roleRepository.findRoleByName(roleName).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
        );
    }
}
