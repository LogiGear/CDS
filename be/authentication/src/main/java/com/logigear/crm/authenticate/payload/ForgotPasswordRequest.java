package com.logigear.crm.authenticate.payload;

import javax.validation.constraints.Size;

import com.logigear.crm.authenticate.validation.Password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
	

	@Password
	private String newPassword;
	
	private String confirmPassword;
	
	@Size(max = 40)
	private String email;
	
	private String passwordResetToken;
}
