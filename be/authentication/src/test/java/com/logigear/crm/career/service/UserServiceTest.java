package com.logigear.crm.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.model.RoleName;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.repository.RoleRepository;
import com.logigear.crm.authenticate.repository.UserRepository;
import com.logigear.crm.authenticate.service.UserService;
import com.logigear.crm.authenticate.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	private static final String NAME = "user test";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "test@gmail.com";
    private static String PASSWORD_ENCODER = "$2a$10$OQxlf6vK5z1d.3AgWxKG4eAw35/D1.s7GD9ewLK2EA5HdJ8IMaw/W";
    
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
	private PasswordEncoder passwordEncoder;

    @MockBean
	private RoleRepository roleRepository;
    
    @BeforeEach
    public void setUp() {
        passwordEncoder = createPasswordEncoder();
        roleRepository = createRoleRepository();
        userService = new UserServiceImpl(userRepository, passwordEncoder, roleRepository);
    }

    @Test
    public void checkCorrectSignUpTest() {
    	
    	// given
        given(userRepository.save(any())).will(returnsFirstArg());
        
        // given
    	SignUpRequest req = new SignUpRequest();
    	req.setFullName(NAME);
    	req.setEmail(EMAIL);
    	req.setPassword(PASSWORD);
    	
    	// when
    	User user = userService.signup(req);
    	
    	// Expected
    	Role userRole = new Role(1L, RoleName.EMPLOYEE);
        Set<Role> userRoles = Collections.singleton(userRole);
    	User expectedUser = new User();
    	expectedUser.setName(NAME);
		expectedUser.setEmail(EMAIL);
		expectedUser.setPassword(PASSWORD_ENCODER);
		expectedUser.setStatus(UserStatus.UNACTIVATED);
		expectedUser.setRoles(userRoles);
		
		// then
		assertEquals(user.toString(), expectedUser.toString());
    }

    private PasswordEncoder createPasswordEncoder() {
        PasswordEncoder mock = mock(PasswordEncoder.class);
        when(mock.encode(anyString())).thenReturn(PASSWORD_ENCODER);
        return mock;
    }
    
    private RoleRepository createRoleRepository() {
    	RoleRepository mock = mock(RoleRepository.class);
        Role userRole = new Role(1L, RoleName.EMPLOYEE);
        Optional<Role> optUserRole = Optional.of(userRole);
        when(mock.findByName(userRole.getName())).thenReturn(optUserRole);
        return mock;
    }

    
}