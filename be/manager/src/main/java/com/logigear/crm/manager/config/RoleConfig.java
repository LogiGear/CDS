package com.logigear.crm.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roleConfiguration")
public class RoleConfig {
    @Value("${spring.authorization.admin.role}")
    private List<String> adminRoles;
    @Value("${spring.authorization.manager.role}")
    private List<String> managerRoles;
    @Value("${spring.authorization.cdm.role}")
    private List<String> cdmRoles;
    @Value("${spring.authorization.user.role}")
    private List<String> userRoles;

    public List<String> getAdminRoles() {
        return adminRoles;
    }

    public List<String> getManagerRoles() {
        return managerRoles;
    }

    public List<String> getCdmRoles() {
        return cdmRoles;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }
}
