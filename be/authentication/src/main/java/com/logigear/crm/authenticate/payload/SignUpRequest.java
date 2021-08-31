package com.logigear.crm.authenticate.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.logigear.crm.authenticate.validation.Password;
import com.logigear.crm.authenticate.validation.UniqueEmail;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	@NotBlank
	@Size(min = 4, max = 40)
	private String fullName;

	@UniqueEmail
	private String email;

	@Password
	private String password;
	
}
