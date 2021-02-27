package com.tkarov.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkarov.demo.dto.request.JwtRequest;
import com.tkarov.demo.model.persistance.Role;
import com.tkarov.demo.model.persistance.User;
import com.tkarov.demo.model.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    private static final String URL = "/authenticate";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setup() {
        cleanDatabase();
    }

    @After
    public void tearDown() {
        cleanDatabase();
    }

    @Test
    public void shouldSuccessfullyAuthenticate() throws Exception {

        userRepository.save(mockUser());

        mockMvc.perform(post(URL)
                .content(objectMapper.writeValueAsString(mockJwtRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

    }

    @Test
    public void shouldFailAuthenticate() throws Exception {
        mockMvc.perform(post(URL)
                .content(objectMapper.writeValueAsString(mockJwtRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }



    private JwtRequest mockJwtRequest() {
        return JwtRequest.builder()
                .username("username")
                .password("password")
                .build();
    }

    private User mockUser() {
        return User.builder()
                .email("user@mail.com")
                .password(bCryptPasswordEncoder.encode("password"))
                .username("username")
                .roles(mockRoles())
                .build();
    }

    private Set<Role> mockRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name("ROLE_USER").build());
        return roles;
    }
    private void cleanDatabase() {
        userRepository.deleteAll();
    }
}
