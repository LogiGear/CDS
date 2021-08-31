package com.logigear.crm.authenticate.payload;

import com.logigear.crm.authenticate.validation.Password;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
	@Password
	private String oldPassword;

	@Password
	private String password;
}
