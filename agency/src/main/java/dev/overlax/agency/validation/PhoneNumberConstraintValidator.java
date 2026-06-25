package dev.overlax.agency.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberConstraintValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final Pattern VALID_PHONE_NUMBER = Pattern.compile("^$|^\\+?[0-9 ()\\-]{9,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VALID_PHONE_NUMBER.matcher(value).find();
    }
}
