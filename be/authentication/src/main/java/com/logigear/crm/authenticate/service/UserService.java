package com.logigear.crm.authenticate.service;

import javax.validation.Valid;

import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.payload.EmailRequest;
import com.logigear.crm.authenticate.payload.ForgotPasswordRequest;
import com.logigear.crm.authenticate.payload.LoginRequest;
import com.logigear.crm.authenticate.payload.ResetPasswordRequest;
import com.logigear.crm.authenticate.payload.SignUpRequest;

public interface UserService {
	
	/**
	 * Allow the register user info
	 * @return the resulting User object
	 */
	User signup(SignUpRequest signUpRequest);
	User generateToken(EmailRequest emailRequest);
	User findUserByEmailToken(String token);
	User updatePassword(String token, ResetPasswordRequest req);
	User generateResetPasswordToken(String token, String email);
	void save(User user);
	User findUserByEmail(String email);
	void verifyStatus(String email);
	void changePassword(LoginRequest req);
	void createUserFromLdapUser(LdapUserDetailsImpl ldapUser, String password);
	void updateUserFromLdapUser(LdapUserDetailsImpl ldapUser, String password);
}
