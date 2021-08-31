package com.logigear.crm.authenticate.controller;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;
import com.logigear.crm.authenticate.payload.EmailRequest;
import com.logigear.crm.authenticate.payload.ForgotPasswordRequest;
import com.logigear.crm.authenticate.payload.ForgotPasswordResponse;
import com.logigear.crm.authenticate.payload.LoginRequest;
import com.logigear.crm.authenticate.payload.ResetPasswordRequest;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.payload.UserResponse;
import com.logigear.crm.authenticate.security.JwtProvider;
import com.logigear.crm.authenticate.service.MailService;
import com.logigear.crm.authenticate.service.UserService;
import com.logigear.crm.authenticate.util.AppConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("authentication/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Value("${spring.server.address.gateway}")
	private String GATEWAY_ADDRESS;
	@Value("${spring.server.address.client}")
	private String CLIENT_ADDRESS;

    private final AuthenticationManager authenticationManager;
    private final JwtProvider tokenProvider;
    private final UserService userService;
    private final MailService mailService;
  

	@PostMapping("signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest req) {
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		req.getEmail(),
                		req.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User)authentication.getPrincipal();
		
		String token = tokenProvider.generateToken(user);
		UserResponse userRes = new UserResponse(user);
		userRes.setToken(token);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + token);
		return ResponseEntity.ok().headers(responseHeaders).body(userRes);
	}

	@Deprecated
	@PostMapping("signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest req) {
		User user = userService.signup(req);

		String token = tokenProvider.generateToken(user);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + token);
		return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(new UserResponse(user));
	}

	@Deprecated
	@PostMapping("forgotpwd")
	public ResponseEntity<ForgotPasswordResponse> generateToken(@RequestBody ForgotPasswordRequest req) {
		String token = UUID.randomUUID().toString();
		User user = userService.generateResetPasswordToken(token, req.getEmail());
		String link = "http://localhost:3000/auth/reset-password/" + user.getPasswordResetToken();
		mailService.send(user.getEmail(), mailService.buildEmailForgotPassword(user.getEmail(), user.getName(), link),
				"Reset your password");
		HttpHeaders responseHeaders = new HttpHeaders();
		return ResponseEntity.ok().headers(responseHeaders).body(new ForgotPasswordResponse(user));
	}

	@Deprecated
	@PatchMapping("resetpwd")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest req,
			@RequestParam(name = "passwordResetToken", required = true) String token) {
		userService.updatePassword(token, req);
		return ResponseEntity.status(HttpStatus.OK).body("Password sucessfully reset");
	}

	@Deprecated
	@PostMapping("confirm")
	public ResponseEntity<?> confirmEmail(@Valid @RequestBody EmailRequest req) {
		User user = userService.generateToken(req);
		String link = GATEWAY_ADDRESS + "/api/auth/confirm?token=" + user.getEmailToken();
		mailService.send(user.getEmail(), mailService.buildEmail(user.getEmail(), user.getName(), link),
				"Confirm your email");
		return ResponseEntity.status(HttpStatus.CREATED).body("Token generated");
	}

	@Deprecated
	@GetMapping("confirm")
	public ResponseEntity<Void> confirmToken(@RequestParam(name = "token", required = true) String token) {
		User user = userService.findUserByEmailToken(token);
		if (user.getStatus().equals(UserStatus.UNACTIVATED)) {
			user.setStatus(UserStatus.ACTIVATED);
			userService.save(user);
		} else {
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(CLIENT_ADDRESS)).build();
		}
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(CLIENT_ADDRESS)).build();
	}

	@Deprecated
	@PostMapping("changepwd")
	public ResponseEntity<?> changePassword(@Valid @RequestBody LoginRequest req) {
		userService.changePassword(req);
		return ResponseEntity.status(HttpStatus.OK).body("User " + req.getEmail() + " updated new password");
	}

}
