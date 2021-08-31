package com.logigear.crm.authenticate.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.logigear.crm.authenticate.exception.AppException;
import com.logigear.crm.authenticate.exception.UniqueEmailException;
import com.logigear.crm.authenticate.model.LdapUser;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	private final static String TOKEN_NOT_FOUND = "Token already expired or does not exist";
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final EmployeeService employeeService;

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
	public User save(User user) throws UniqueEmailException{
		return userRepository.save(user);
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
	@Transactional
	public User save(LdapUser ldapUSer) {
		Optional<User> userOptional = userRepository.findByEmail(ldapUSer.getEmail());
		User user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
			user = updateExistingUser(user, ldapUSer);
		} else {
			user = registerNewUser(ldapUSer);
		}

		employeeService.save(user, ldapUSer);
		return user;
	}

	
	private User registerNewUser(LdapUser ldapUSer) {
		User user = new User();
		user.setEmail(ldapUSer.getEmail());
		user.setName(ldapUSer.getDisplayName());
		user.setPassword(passwordEncoder.encode(ldapUSer.getPassword()));
		user.setStatus(UserStatus.ACTIVATED);
		user.setDeleted(false);
		user.setExpiredAt(Instant.now());
		Role userRole = roleRepository.findByName(RoleName.EMPLOYEE)
				.orElseThrow(() -> new AppException("User Role not set."));
		user.setRoles(Collections.singleton(userRole));

		return userRepository.save(user);
	}

	private User updateExistingUser(User existingUser, LdapUser ldapUSer) {
		existingUser.setName(ldapUSer.getDisplayName());
		existingUser.setPassword(passwordEncoder.encode(ldapUSer.getPassword()));
		return userRepository.save(existingUser);
	}
}
