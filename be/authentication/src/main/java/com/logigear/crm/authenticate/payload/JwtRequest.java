package com.logigear.crm.authenticate.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class JwtRequest {
	private static final long serialVersionUID = 5926468583005150707L;
	
	private String email;
	private String password;
}
