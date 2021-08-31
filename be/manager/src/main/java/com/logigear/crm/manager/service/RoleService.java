package com.logigear.crm.manager.service;

import com.logigear.crm.manager.model.Role;
import com.logigear.crm.manager.model.RoleName;

public interface RoleService {
    Role findRoleByName(RoleName roleName);
}
