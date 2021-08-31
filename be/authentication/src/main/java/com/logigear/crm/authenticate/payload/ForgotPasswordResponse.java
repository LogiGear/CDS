package com.logigear.crm.authenticate.payload;


import com.logigear.crm.authenticate.model.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ForgotPasswordResponse {
	private String passwordResetToken;
	
	public ForgotPasswordResponse(User user) {
		passwordResetToken = user.getPasswordResetToken();
	}
}
