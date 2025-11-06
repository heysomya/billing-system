package com.billingSystem.user_authentication.controller;

import com.billingSystem.user_authentication.entity.User;
import com.billingSystem.user_authentication.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

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
    void loginUser_success_returnsToken() {
        AuthenticationRequest req = new AuthenticationRequest("username", "password");
        when(userService.login("username", "password")).thenReturn("token");

        ResponseEntity<AuthenticationResponse> response = userController.loginUser(req);

        assertEquals("token", response.getBody().getToken());
        verify(userService).login("username", "password");
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
