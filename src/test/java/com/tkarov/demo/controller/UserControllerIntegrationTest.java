package com.tkarov.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkarov.demo.dto.request.UserRequestBody;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private static final String URL = "/api/v1/users/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        cleanDatabase();
    }

    @After
    public void tearDown() {
        cleanDatabase();
    }

    @Test
    public void shouldSuccessfullyCreateUser() throws Exception {

        mockMvc.perform(post(URL + "registration")
                .content(objectMapper.writeValueAsString(mockUserRequestBody()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("&.username").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0].name").value("ROLE_USER"));
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        userRepository.save(mockUser());
        userRepository.save(mockUser());

        mockMvc.perform(get(URL + "all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    public void shouldSuccessfullyFindUserById() throws Exception {
        User mockUser = userRepository.save(mockUser());

        mockMvc.perform(get(URL + "id=" + mockUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUser.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(mockUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0].name").value("ROLE_USER"));
    }


    @Test
    public void shouldSuccessfullyUpdateUser() throws Exception {
        User mockUser = userRepository.save(mockUser());
        UserRequestBody mockUserRequestBody = mockUserRequestBody();
        mockUserRequestBody.setEmail("user@mail.com");

        mockMvc.perform(put(URL + "id=" + mockUser.getId())
                .content(objectMapper.writeValueAsString(mockUserRequestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUserRequestBody.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(mockUserRequestBody.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0].name").value("ROLE_USER"));
    }

    @Test
    public void shouldSuccessfullyDeleteUser() throws Exception {
        User mockUser = userRepository.save(mockUser());

        mockMvc.perform(delete(URL + "id=" + mockUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, userRepository.findAll().size());
    }

    private User mockUser() {
        return User.builder()
                .email("user@mail.com")
                .username("username")
                .password("password")
                .roles(mockRoles())
                .build();
    }

    private Set<Role> mockRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name("ROLE_USER").build());
        return roles;
    }

    private UserRequestBody mockUserRequestBody() {
        return UserRequestBody.builder()
                .email("user@mail.com")
                .username("username")
                .password("password")
                .build();
    }

    private void cleanDatabase() {
        userRepository.deleteAll();
    }
}
