package com.logigear.crm.authenticate.service.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.logigear.crm.authenticate.exception.AppException;
import com.logigear.crm.authenticate.model.EmployeeDetails;
import com.logigear.crm.authenticate.model.LdapUser;
import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.model.RoleName;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.repository.EmployeeRepository;
import com.logigear.crm.authenticate.repository.UserRepository;
import com.logigear.crm.authenticate.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public EmployeeDetails signup(SignUpRequest req, User user) {
        EmployeeDetails e = new EmployeeDetails();
        e.setFullName(req.getFullName());
        e.setId(user.getId());
        try{
            e.setEmployeeID(employeeRepository.findMaxEmployeeId()+1);
        }catch(Exception ex){
            e.setEmployeeID(1);
        }
        return employeeRepository.save(e);
    }

    @Override
    public EmployeeDetails findEmployeeById(long id) {
        Optional<EmployeeDetails> employeeDetails = employeeRepository.findById(id);

        if (!employeeDetails.isPresent()) {
            return null;
        }
        return employeeDetails.get();
    }

    @Override
    public Boolean existEmployeeById(long id) {
        if(employeeRepository.findById(id).isPresent())return true;
        return false;
    }


	@Override
	public EmployeeDetails save(User user, LdapUser ldapUser) {
		Optional<EmployeeDetails> emplOptional = employeeRepository.findById(user.getId());
		EmployeeDetails empl;
		if (emplOptional.isPresent()) {
			empl = emplOptional.get();
			empl = updateExistingEmployee(empl, ldapUser);
		} else {
			empl =  registerNewEmployee(user, ldapUser);
		}
		return empl;
	}
	
	private EmployeeDetails registerNewEmployee(User user, LdapUser ldapUser) {
		EmployeeDetails empl = new EmployeeDetails();
		empl.setId(user.getId());
		empl.setFullName(ldapUser.getName());
		empl.setJobTitle(ldapUser.getTitle());
		empl.setEmployeeID(Integer. parseInt(ldapUser.getDisplayName().split(" - ")[1]));
		empl.setAddress(ldapUser.getPhysicalDeliveryOfficeName());
		empl.setDeleted(false);
	
		return employeeRepository.save(empl);
	}

	private EmployeeDetails updateExistingEmployee(EmployeeDetails existingEmpl, LdapUser ldapUser) {
		existingEmpl.setFullName(ldapUser.getName());
		existingEmpl.setJobTitle(ldapUser.getTitle());
		existingEmpl.setEmployeeID(Integer. parseInt(ldapUser.getDisplayName().split(" - ")[1]));
		existingEmpl.setAddress(ldapUser.getPhysicalDeliveryOfficeName());
		return employeeRepository.save(existingEmpl);
	}

	
/*
    public void createEmployeeFromLdapUser(LdapUserDetailsImpl ldapUser) {
        User user = userRepository.findByEmailContains(ldapUser.getUsername()).get();

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setId(user.getId());
        EmployeeDetails userFromLdif = this.getLdapUserByUid(this.getUidFromDn(ldapUser.getDn()));
        employeeDetails.setFullName(userFromLdif.getFullName());
        employeeDetails.setDeleted(false);
        employeeRepository.save(employeeDetails);
    }

    public void updateEmployeeFromLdapUser(LdapUserDetailsImpl ldapUser) {
        User user = userRepository.findByEmailContains(ldapUser.getUsername()).get();

        EmployeeDetails employeeDetails = employeeRepository.findById(user.getId()).get();
        EmployeeDetails userFromLdif = this.getLdapUserByUid(this.getUidFromDn(ldapUser.getDn()));
        employeeDetails.setFullName(userFromLdif.getFullName());

        employeeRepository.save(employeeDetails);
    }

    private class EmployeeAttributeMapper implements AttributesMapper<EmployeeDetails> {
        @Override
        public EmployeeDetails mapFromAttributes(Attributes attributes) throws NamingException {
            EmployeeDetails employeeDetails = new EmployeeDetails();
            employeeDetails.setFullName((String) attributes.get("cn").get());
            return employeeDetails;
        }
    }

    private EmployeeDetails getLdapUserByUid(String uid) {
        List<EmployeeDetails> ldapUsers = ldapTemplate.search(query().where("uid").is(uid), new EmployeeAttributeMapper());
        return ((null != ldapUsers && !ldapUsers.isEmpty()) ? ldapUsers.get(0) : null);
    }

    private String getUidFromDn(String dn) {
        String uid = dn.split(",")[0];
        return uid.substring(4);
    }*/
}
