package com.logigear.crm.authenticate.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import com.logigear.crm.authenticate.validation.Password;

@Getter @Setter @ToString
public class LoginRequest {

	@NotBlank
	private String email;
	
	@Password
    private String password;
}
