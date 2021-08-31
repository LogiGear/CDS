package com.logigear.crm.authenticate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.repository.RoleRepository;
import com.logigear.crm.authenticate.repository.UserRepository;

import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService  {

	private UserRepository userRepository;
	private RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
                                    RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByEmailContains(usernameOrEmail)
                .orElseThrow(() -> 
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );
        user.setRoles((Set) roleRepository.findByUserId(user.getId()));

        return new UserPrincipal(user);
    }
	
	
	// This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        user.setRoles((Set) roleRepository.findByUserId(id));

        return new UserPrincipal(user);
    }
}
