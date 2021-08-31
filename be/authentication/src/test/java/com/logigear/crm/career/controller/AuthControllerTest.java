package com.logigear.crm.career.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;
import com.logigear.crm.authenticate.payload.EmailRequest;
import com.logigear.crm.authenticate.payload.LoginRequest;
import com.logigear.crm.authenticate.payload.SignUpRequest;
import com.logigear.crm.authenticate.security.JwtProvider;
import com.logigear.crm.authenticate.service.UserService;
import com.logigear.crm.authenticate.util.AppConstants;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthControllerTest {
	
	private static final String SIGNUP_URL = "/api/auth/signup";
	private static final String CONFIRM_URL = "/api/auth/confirm";
	private static final String NAME = "test";
	private static final String EMAIL = "usertest@gmail.com";
	private static final String PASSWORD = "password";
	private static final String TOKEN = "tokenteststring";
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private AuthenticationManager authenticationManager;
	
	@MockBean
    private JwtProvider tokenProvider;
	
	@MockBean
	private HttpHeaders responseHeaders;
	
	@MockBean
	Authentication auth;

	@MockBean	
	private UserService userService;
	
	@Test
	public void authenticateUser_Success() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setEmail("phong.ha@gmail.com");
		req.setPassword("12345678");
		
		User user = new User();
		user.setEmail("phong.ha@gmail.com");
		user.setPassword("12345678");
		
		String token = "abc";	
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(user);
		when(tokenProvider.generateToken(user)).thenReturn(token);

        responseHeaders.set(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + token);
		
		mvc.perform(post("/api/auth/signin")
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isOk())
        .andExpect(header().string(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + token))
        .andExpect(content().string(containsString(user.getEmail())));
	}
	
	@Test
	public void authenticateUser_EmailNotFound_returnUnauthorized() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setEmail("phong.ha@gmail.com");
		req.setPassword("12345678");
		
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        	.thenThrow(new UsernameNotFoundException("User not found with username or email : " + req.getEmail()));
   	
		mvc.perform(post("/api/auth/signin")
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isUnauthorized())
        .andExpect(status().reason(containsString("User not found with username or email : " + req.getEmail())));
	}
	
	@Test
	public void authenticateUser_EmailEmpty_returnBadRequest() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setEmail("");
		req.setPassword("12345678");
		  	
		mvc.perform(post("/api/auth/signin")
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void authenticateUser_PasswordNull_returnBadRequest() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setEmail("phong.ha@gmail.com");
		req.setPassword(null);
		  	
		mvc.perform(post("/api/auth/signin")
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isBadRequest());
	}
	
	
	/**
	 * Unit test for SignUp
	 * */
	@Test
	public void signup_Success() throws Exception {
		// given
		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		SignUpRequest req = new SignUpRequest(NAME, EMAIL, PASSWORD);
		
		// when
		when(userService.signup(any(SignUpRequest.class))).thenReturn(user);
		when(tokenProvider.generateToken(user)).thenReturn(TOKEN);
		
        responseHeaders.set(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + TOKEN);
        
		mvc.perform(post(SIGNUP_URL)
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isCreated())
        .andExpect(header().string(AppConstants.TOKEN_RESPONSE_HEADER_NAME, AppConstants.TOKEN_PREFIX + TOKEN))
		.andExpect(content().string(containsString(user.getEmail())));
	}
	
	@Test
	public void signup_EmailNull_returnBadRequest() throws Exception {
		SignUpRequest req = new SignUpRequest(NAME, null, PASSWORD);
		
		mvc.perform(post(SIGNUP_URL)
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void signup_NameIsBlank_returnBadRequest() throws Exception {
		SignUpRequest req = new SignUpRequest("", EMAIL, PASSWORD);
		
		mvc.perform(post(SIGNUP_URL)
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void signup_PassNull_returnBadRequest() throws Exception {
		SignUpRequest req = new SignUpRequest(NAME, EMAIL, "");
		
		mvc.perform(post(SIGNUP_URL)
		.contentType(TestUtil.APPLICATION_JSON_UTF8)
		.content(TestUtil.convertObjectToJsonBytes(req)))
        .andExpect(status().isBadRequest());
	}

	@Test
	public void confirm_EmailNull_returnBadRequest() throws Exception {
		EmailRequest req = new EmailRequest();
		req.setEmail(null);

		mvc.perform(post(CONFIRM_URL)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(req)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void confirm_Send_Success() throws Exception {
		// given
		User user = new User();
		user.setEmail(EMAIL);
		EmailRequest req = new EmailRequest();
		req.setEmail(EMAIL);


		// when
		when(userService.generateToken(any(EmailRequest.class))).thenReturn(user);

		mvc.perform(post(CONFIRM_URL)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(req)))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("Token generated")));
	}

	@Test
	public void confirm_Confirm_Success() throws Exception {
		// given
		User user = new User();
		user.setEmail(EMAIL);
		user.setStatus(UserStatus.UNACTIVATED);
		user.setEmailToken(UUID.randomUUID().toString());
		EmailRequest req = new EmailRequest();
		req.setEmail(EMAIL);


		// when
		when(userService.findUserByEmailToken(any(String.class))).thenReturn(user);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CONFIRM_URL+"?token="+user.getEmailToken())
				.accept(MediaType.APPLICATION_JSON);
		mvc.perform(requestBuilder);
		assertEquals(user.getStatus(),UserStatus.ACTIVATED);
	}

}
