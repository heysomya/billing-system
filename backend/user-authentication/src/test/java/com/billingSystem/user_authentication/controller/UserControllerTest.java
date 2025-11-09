package com.billingSystem.user_authentication.controller;

import com.billingSystem.user_authentication.entity.User;
import com.billingSystem.user_authentication.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void registerUser_returnsCreatedUser() {
        User user = new User();
        user.setUsername("name");
        user.setPassword("name");

        User registeredUser = new User();
        registeredUser.setUsername("name");
        registeredUser.setPassword("name");

        when(userService.registerUser(any(User.class))).thenReturn(registeredUser);

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(registeredUser, response.getBody());
        verify(userService).registerUser(user);
    }

    @Test
    void loginUserSuccess() {
        AuthenticationRequest req = new AuthenticationRequest("username", "password");
        when(userService.login("username", "password")).thenReturn("token");

        User user = new User();
        user.setUsername("username");
        user.setRole("admin");

        when(userService.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<AuthenticationResponse> response = userController.loginUser(req);

        Assertions.assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
        assertEquals("admin", response.getBody().getRole());
        assertEquals("username", response.getBody().getUserName());

        verify(userService).login("username", "password");
        verify(userService).findByUsername("username");
    }

    @Test
    void loginUser_failure_returnsErrorMessage() {
        AuthenticationRequest req = new AuthenticationRequest("user123", "password123");
        when(userService.login(anyString(), anyString()))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        ResponseEntity<AuthenticationResponse> response = userController.loginUser(req);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody().getToken());
        verify(userService).login("user123", "password123");
    }

}
