package com.logigear.crm.authenticate.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.logigear.crm.authenticate.validation.Password;

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
