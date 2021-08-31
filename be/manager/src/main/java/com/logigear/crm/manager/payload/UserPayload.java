package com.logigear.crm.manager.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor // Template - Most payload need this
public class UserPayload {

    private long id;
    @NotBlank
    private String email;
    @NotBlank
    private String name;

}
