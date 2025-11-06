package com.billingSystem.user_authentication.service;

import com.billingSystem.user_authentication.entity.User;
import com.billingSystem.user_authentication.repository.UserRepository;
import com.billingSystem.user_authentication.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldSaveEncodedUser() {
        User user = new User();
        user.setUsername("user123");
        user.setPassword("password123");
        user.setRoles(Set.of("USER"));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(user);

        assertNotNull(result.getId());
        assertEquals("encodedpassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByUsername_returnsUser() {
        User user = new User();
        user.setUsername("user123");
        when(userRepository.findByUsername("user123")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("user123");
        assertTrue(result.isPresent());
        assertEquals("user123", result.get().getUsername());
    }

    @Test
    void login_authenticatedAndReturnsToken() {
        String username = "user123";
        String password = "password123";
        Set<String> roles = Set.of("USER");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(username, roles)).thenReturn("token");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        String token = userService.login(username, password);
        assertEquals("token", token);
    }

    @Test
    void login_userNotFound_throwsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        assertThrows(RuntimeException.class, () -> userService.login("user", "password"));
    }
}
