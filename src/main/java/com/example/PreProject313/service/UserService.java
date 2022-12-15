package com.example.PreProject313.service;

import com.example.PreProject313.dao.UserDAO;
import com.example.PreProject313.model.Role;
import com.example.PreProject313.model.User;
import com.example.PreProject313.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, UserDAO userDAO) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDAO = userDAO;

        defaultUsers();
    }



    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new User(user.get());
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public User getUserById(long id) {
        return userDAO.getUserById(id);
    }
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public void update(User user) {
        userDAO.update(user);
    }

    public void delete(long id) {
        userDAO.delete(id);
    }

    public void defaultUsers() {
        Set<Role> listRole2 = new HashSet<>();
        listRole2.add(roleService.getRoleById(1L));
        listRole2.add(roleService.getRoleById(2L));

        User user2 = new User("admin", 20, "email2@gmail.com", "$2a$10$HvBkZ6gVTpcj54FPVTHjd.ZKIHBKQGCxvp8A2X3ALJFhtLLNDsXOe", listRole2); //admin1

        save(user2);
    }
}
