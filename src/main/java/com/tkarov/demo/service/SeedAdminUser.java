package com.tkarov.demo.service;

import com.tkarov.demo.model.persistance.Role;
import com.tkarov.demo.model.persistance.User;
import com.tkarov.demo.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SeedAdminUser implements ApplicationRunner {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SeedAdminUser(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name("ROLE_ADMIN").build());

        User newUser = User.builder()
                .password(bCryptPasswordEncoder.encode("admin"))
                .email("admin@mail.com")
                .username("admin")
                .roles(roles)
                .build();

        userRepository.save(newUser);
    }
}
