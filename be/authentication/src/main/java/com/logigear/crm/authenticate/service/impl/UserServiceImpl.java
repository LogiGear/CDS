package com.logigear.crm.authenticate.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.logigear.crm.authenticate.exception.AppException;
import com.logigear.crm.authenticate.exception.UniqueEmailException;
import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.model.RoleName;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;
import com.logigear.crm.authenticate.payload.EmailRequest;
import com.logigear.crm.authenticate.payload.LoginRequest;
import com.logigear.crm.authenticate.payload.ResetPasswordRequest;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.repository.RoleRepository;
import com.logigear.crm.authenticate.repository.UserRepository;
import com.logigear.crm.authenticate.service.EmployeeService;
import com.logigear.crm.authenticate.service.UserService;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	private final static String TOKEN_NOT_FOUND = "Token already expired or does not exist";
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RoleRepository roleRepository;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
						   RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	@Override
	public User signup(SignUpRequest req) {
		User u = new User();
		u.setEmail(req.getEmail());
		u.setName(req.getEmail().replaceAll("(@.*)", "").trim());
		u.setPassword(passwordEncoder.encode(req.getPassword()));
		u.setStatus(UserStatus.UNACTIVATED);
		u.setExpiredAt(Instant.now());
		Role userRole = roleRepository.findByName(RoleName.EMPLOYEE)
				.orElseThrow(() -> new AppException("User Role not set."));
		u.setRoles(Collections.singleton(userRole));

		if (userRepository.existsByEmail(req.getEmail())) {
			throw new AppException("EXIST_EMAIL");
		}

		User savedUser = userRepository.save(u);
		employeeService.signup(req,savedUser);
		return savedUser;
	}

	@Override
	public User generateToken(EmailRequest req){
		User user = userRepository.findByEmailContains(req.getEmail()).orElseThrow(
				() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, req.getEmail())));
		if((user.getExpiredAt().isBefore(Instant.now())||user.getEmailToken() == null)
				&& user.getStatus().equals(UserStatus.UNACTIVATED)) {
			user.setEmailToken(UUID.randomUUID().toString());
			user.setExpiredAt(Instant.now().plus(7, ChronoUnit.DAYS));
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Token have not expired yet or already activated!");
		}
		return userRepository.save(user);
	}

	@Override
	public User findUserByEmailToken(String token){
		User user = userRepository.findByEmailToken(token).orElseThrow(
				() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,token)));
		return user;
	}
	
	@Override
	public User generateResetPasswordToken(String token, String email) {
		User user = userRepository.findByEmailContains(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
		user.setTokenExpiredAt(Instant.now().plus(1, ChronoUnit.DAYS));
		user.setPasswordResetToken(token);
		return userRepository.save(user);                    
	}
	
	@Override
	public User updatePassword(String token, ResetPasswordRequest req) {
		User user = userRepository.findByPasswordResetToken(token)
				.orElseThrow(() -> 
                new UsernameNotFoundException(TOKEN_NOT_FOUND));
		if(user.getTokenExpiredAt().isAfter(Instant.now())) {
				user.setPassword(passwordEncoder.encode(req.getNewPassword()));
				user.setPasswordResetToken(null);
			return userRepository.save(user);
		}
		else {
			throw new AppException(TOKEN_NOT_FOUND);
		}
		
	}

	@Override
	public void save(User user) throws UniqueEmailException{
		userRepository.save(user);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User findUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmailContains(email);

		if (!user.isPresent()) {
			return null;
		}
		return user.get();
	}

	@Override
	public void verifyStatus(String email) {
		User u = findUserByEmail(email);
        if(u.getStatus().equals(UserStatus.UNACTIVATED)){
        	throw new AppException("User account is not activated");
        }
	}

	@Override
	public void changePassword(LoginRequest req) {
		User user = userRepository.findByEmailContains(req.getEmail()).orElseThrow(
				() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, req.getEmail())));
		user.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));
		userRepository.save(user);
		
	}

	@Override
	public void createUserFromLdapUser(LdapUserDetailsImpl ldapUser, String password) {
		User user = new User();
		user.setEmail(ldapUser.getUsername());
		User userFromLdif = this.getLdapUserByUid(this.getUidFromDn(ldapUser.getDn()));
		if (userFromLdif != null) {
			user.setName(userFromLdif.getName());
		} else {
			user.setName(ldapUser.getUsername().replaceAll("(@.*)", "").trim());
		}
		user.setPassword(passwordEncoder.encode(password));
		user.setStatus(UserStatus.ACTIVATED);
		user.setDeleted(false);
		user.setExpiredAt(Instant.now());
		Role userRole = roleRepository.findByName(RoleName.EMPLOYEE)
				.orElseThrow(() -> new AppException("User Role not set."));
		user.setRoles(Collections.singleton(userRole));

		this.save(user);
	}

	@Override
	public void updateUserFromLdapUser(LdapUserDetailsImpl ldapUser, String password) {
		User user = this.findUserByEmail(ldapUser.getUsername());

		user.setPassword(passwordEncoder.encode(password));
		User userFromLdif = this.getLdapUserByUid(this.getUidFromDn(ldapUser.getDn()));
		if (userFromLdif != null) {
			user.setName(userFromLdif.getName());
		} else {
			user.setName(ldapUser.getUsername().replaceAll("(@.*)", "").trim());
		}

		this.save(user);
	}

	private class UserAttributeMapper implements AttributesMapper<User> {
		@Override
		public User mapFromAttributes(Attributes attributes) throws NamingException {
			User user = new User();
			user.setName((String) attributes.get("cn").get());
			return user;
		}
	}

	private User getLdapUserByUid(String uid) {
		List<User> ldapUsers = ldapTemplate.search(query().where("uid").is(uid), new UserAttributeMapper());
		return ((null != ldapUsers && !ldapUsers.isEmpty()) ? ldapUsers.get(0) : null);
	}

	private String getUidFromDn(String dn) {
		String uid = dn.split(",")[0];
		return uid.substring(4);
	}
}
