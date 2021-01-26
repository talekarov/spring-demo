package com.tkarov.demo.service;

import com.tkarov.demo.dto.request.UserRequestBody;
import com.tkarov.demo.dto.response.UserResponseBody;
import com.tkarov.demo.exception.UserAlreadyExistsException;
import com.tkarov.demo.exception.UserNotFoundException;
import com.tkarov.demo.model.persistance.Role;
import com.tkarov.demo.model.persistance.User;
import com.tkarov.demo.model.repository.UserRepository;
import com.tkarov.demo.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserResponseBody> getUsers(){
        List<User> users = repository.findAll();
        return ConvertUtils.convertToUserResponseBodies(users);
    }

    public UserResponseBody createUser(UserRequestBody userRequestBody){
        if (repository.findByEmail(userRequestBody.getEmail()).isPresent()
        || repository.findByUsername(userRequestBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.builder().name("ROLE_USER").build());

        User newUser = User.builder()
                .password(bCryptPasswordEncoder.encode(userRequestBody.getPassword()))
                .email(userRequestBody.getEmail())
                .username(userRequestBody.getUsername())
                .roles(userRoles)
                .build();

        User user = repository.save(newUser);
        return ConvertUtils.convertToUserResponseBody(user);

    }

    public UserResponseBody findById(Long id){
        User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        return ConvertUtils.convertToUserResponseBody(user);
    }

    public UserResponseBody findByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return ConvertUtils.convertToUserResponseBody(user);
    }

    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(UserNotFoundException::new);
        repository.deleteById(id);
    }

    public UserResponseBody updateUser(UserRequestBody userRequestBody, Long id) {
        User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setEmail(userRequestBody.getEmail());
        user.setUsername(userRequestBody.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestBody.getPassword()));
        return ConvertUtils.convertToUserResponseBody(user);
    }

}