package com.logigear.crm.authenticate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class ValidRolesValidator implements ConstraintValidator<ValidRoles, Collection<String>> {

	
    @Override
    public boolean isValid(Collection<String> collection, ConstraintValidatorContext context) {
    	// TODO : implement
        return true;
    }

}