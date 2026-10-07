package com.mflores.telecomapp.validation;

import com.mflores.telecomapp.dto.CreateCustomerCredentialRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, CreateCustomerCredentialRequest> {


    @Override
    public boolean isValid(CreateCustomerCredentialRequest dto, ConstraintValidatorContext context) {

        //If either password or confirm password are null we return null because the passwordMatch validator was
        //given a separated task, it doesn't have to check if the inputs are blank, that task is delegated to
        //a different validator
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            return true;
        }

        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
