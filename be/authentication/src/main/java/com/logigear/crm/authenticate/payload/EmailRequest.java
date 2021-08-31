package com.logigear.crm.authenticate.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class EmailRequest {
    @NotBlank
    private String email;
}

