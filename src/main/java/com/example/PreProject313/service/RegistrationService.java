package com.example.PreProject313.service;

import com.example.PreProject313.model.User;
import com.example.PreProject313.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        //System.out.println(user.getUsername());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.addAuthority(roleService.getRoleById(2L));
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void defaulRegister(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
