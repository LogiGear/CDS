package com.logigear.crm.authenticate.service;

import com.logigear.crm.authenticate.model.EmployeeDetails;
import com.logigear.crm.authenticate.model.LdapUser;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.payload.SignUpRequest;

public interface EmployeeService {

    EmployeeDetails signup(SignUpRequest req, User user);

    EmployeeDetails findEmployeeById(long id);

    Boolean existEmployeeById(long id);
    
    EmployeeDetails save(User user, LdapUser ldapUser);
    
}
