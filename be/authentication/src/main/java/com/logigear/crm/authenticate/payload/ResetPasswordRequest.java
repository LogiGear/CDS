package com.logigear.crm.authenticate.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;

import com.logigear.crm.authenticate.validation.Password;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
	
	@NotBlank
	@Password
	private String newPassword;
	
	@NotBlank
	private String confirmPassword;

}
