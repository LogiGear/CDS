package com.logigear.crm.authenticate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD }) 
@Retention(RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = { ValidRolesValidator.class }) 
public @interface ValidRoles {

    String message() default "Invalid role detected"; 

    Class<?>[] groups() default {}; 

    Class<? extends Payload>[] payload() default {}; 
}