package dev.overlax.agency.dto;

import dev.overlax.agency.validation.ValidPassword;
import dev.overlax.agency.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank(message = "{register.firstName.notBlank}")
        String firstName,

        @NotBlank(message = "{register.lastName.notBlank}")
        String lastName,

        @NotBlank(message = "{register.email.notBlank}")
        @Email(message = "{register.email.invalid}")
        String email,

        @NotBlank(message = "{register.password.notBlank}")
        @ValidPassword(message = "{register.password.invalid}")
        String password,

        @ValidPhoneNumber(message = "{register.phoneNumber.invalid}")
        String phoneNumber
) {
}
