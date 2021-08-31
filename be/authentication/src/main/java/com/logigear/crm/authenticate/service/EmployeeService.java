package com.logigear.crm.authenticate.service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import java.util.Optional;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.logigear.crm.authenticate.model.EmployeeDetails;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.repository.EmployeeRepository;
import com.logigear.crm.authenticate.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;
    private LdapTemplate ldapTemplate;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           LdapTemplate ldapTemplate){
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.ldapTemplate = ldapTemplate;
    }


    public EmployeeDetails signup(SignUpRequest req,User user) {
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

    public EmployeeDetails findEmployeeById(long id) {
        Optional<EmployeeDetails> employeeDetails = employeeRepository.findById(id);

        if (!employeeDetails.isPresent()) {
            return null;
        }
        return employeeDetails.get();
    }

    public Boolean existEmployeeById(long id) {
        if(employeeRepository.findById(id).isPresent())return true;
        return false;
    }

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
    }
}
