package com.example.PreProject313.controller;

import com.example.PreProject313.model.User;
import com.example.PreProject313.service.RegistrationService;
import com.example.PreProject313.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthControlller {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthControlller(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user")User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(user);

        return "redirect:/auth/login";
    }
}
