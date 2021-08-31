package com.logigear.crm.authenticate.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.logigear.crm.authenticate.exception.AppException;
import com.logigear.crm.authenticate.model.*;
import com.logigear.crm.authenticate.property.AppProperties;
import com.logigear.crm.authenticate.repository.EmployeeRepository;
import com.logigear.crm.authenticate.repository.RoleRepository;
import com.logigear.crm.authenticate.repository.UserRepository;

import java.util.Collections;

@Configuration
public class StartupConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final AppProperties properties;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StartupConfig(UserRepository userRepository, RoleRepository roleRepository,
                         AppProperties properties, PasswordEncoder passwordEncoder,
                         EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.properties = properties;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {

        if (!roleRepository.existsByName(RoleName.ADMIN)) {
            final Role adminRole = new Role();
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByName(RoleName.EMPLOYEE)) {
            final Role userRole = new Role();
            userRole.setName(RoleName.EMPLOYEE);
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByName(RoleName.CDM)) {
            final Role userRole = new Role();
            userRole.setName(RoleName.CDM);
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByName(RoleName.MANAGER)) {
            final Role userRole = new Role();
            userRole.setName(RoleName.MANAGER);
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByName(RoleName.EMPLOYEE)) {
            final Role userRole = new Role();
            userRole.setName(RoleName.EMPLOYEE);
            roleRepository.save(userRole);
        }

        if (!userRepository.existsByEmail(properties.getAdmin().getEmail())) {
            final User user = new User();
            final EmployeeDetails employee = new EmployeeDetails();
            user.setEmail(properties.getAdmin().getEmail());
            user.setName(properties.getAdmin().getName());
            user.setPassword(passwordEncoder.encode(properties.getAdmin().getPassword()));
            user.setStatus(UserStatus.ACTIVATED);
            try{
                employee.setEmployeeID(employeeRepository.findMaxEmployeeId()+1);
            }catch(Exception e){
                employee.setEmployeeID(1);
            }
            employee.setFullName(properties.getAdmin().getName());
            Role userRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new AppException("Admin Role not set."));
            user.setRoles(Collections.singleton(userRole));
            User savedUser = userRepository.save(user);
            employee.setId(savedUser.getId());
            employeeRepository.save(employee);
        }
    }
}