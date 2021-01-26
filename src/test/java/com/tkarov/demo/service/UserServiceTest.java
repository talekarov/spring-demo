package com.tkarov.demo.service;

import com.tkarov.demo.dto.request.UserRequestBody;
import com.tkarov.demo.exception.UserAlreadyExistsException;
import com.tkarov.demo.exception.UserNotFoundException;
import com.tkarov.demo.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        userService = new UserService(userRepository, bCryptPasswordEncoder);
    }

    @Test
    public void createUserThatAlreadyExists(){
        String email = "user@mail.com";
        String username = "username";
        String password = "password";
        when(userRepository.existsByEmail(email)
                || userRepository.existsByUsername(username)).thenReturn(Boolean.TRUE);
        expectedException.expect(UserAlreadyExistsException.class);
        userService.createUser(UserRequestBody.builder()
                .email(email).username(username).password(password).build());
    }

    @Test
    public void readUserThatNotExists(){
        Long id = Long.valueOf("2");
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        expectedException.expect(UserNotFoundException.class);
        userService.findById(id);
    }

    @Test
    public void updateUserThatNotExists(){
        UserRequestBody newUser = UserRequestBody.builder()
                .email("newUser@mail.com")
                .username("newUsername")
                .password("newPassword")
                .build();
        Long id = Long.valueOf("2");
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        expectedException.expect(UserNotFoundException.class);
        userService.updateUser(newUser, id);
    }

    @Test
    public void deleteUserThatNotExists(){
        Long id = Long.valueOf("2");
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        expectedException.expect(UserNotFoundException.class);
        userService.deleteById(id);
    }
}
