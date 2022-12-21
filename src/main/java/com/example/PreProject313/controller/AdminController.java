package com.example.PreProject313.controller;


import com.example.PreProject313.model.User;
import com.example.PreProject313.service.RegistrationService;
import com.example.PreProject313.service.RoleService;
import com.example.PreProject313.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, RegistrationService registrationService) {

        this.userService = userService;
        this.roleService = roleService;
        this.registrationService = registrationService;
    }

    @GetMapping("/index")
    public String index(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", principal);
        return "admin/index";
    }

    @GetMapping("/{id}")
    public String showUserPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {

        model.addAttribute("listRoles",roleService.getAllRoles());
        return "admin/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam("listRoles") ArrayList<Long> roles) {
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        if (userService.getUserById(user.getId()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("User with name \"%s\" is already exist!", user.getUsername())));
        }
        user.setAuthorities(roleService.findByIdRoles(roles));
        registrationService.defaulRegister(user);
        return "redirect:/admin/index";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("listRoles", roleService.getAllRoles());
        return "admin/edit";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam("listRoles") ArrayList<Long>roles) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        System.out.println(user.getFirstName() + user.getLastName());
        user.setAuthorities(roleService.findByIdRoles(roles));
        registrationService.codePassword(user);
        userService.update(user);
        return "redirect:/admin/index";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin/index";
    }

}
