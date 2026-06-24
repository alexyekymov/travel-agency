package dev.overlax.agency.controller;

import dev.overlax.agency.dto.UserRequest;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/sign-in")
    public String signIn() {
        return "auth/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("userRequest") UserRequest request) {
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid @ModelAttribute("userRequest") UserRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-up";
        }

        try {
            userService.register(request);
        } catch (EmailAlreadyExistsException e) {
            bindingResult.rejectValue("email", "email.exists", "Email is already taken");
            return "auth/sign-up";
        }

        return "redirect:/auth/sign-in?registered";
    }
}
