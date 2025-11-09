package com.billingSystem.user_authentication.controller;

import com.billingSystem.user_authentication.entity.Customer;
import com.billingSystem.user_authentication.entity.User;
import com.billingSystem.user_authentication.service.CustomerService;
import com.billingSystem.user_authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authRequest) {
        try {
            String token = userService.login(authRequest.getUsername(), authRequest.getPassword());
            User user = userService.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String role = user.getRole() != null
                    ? user.getRole()
                    : null;

            return ResponseEntity.ok(new AuthenticationResponse(token, role, user.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid username or password", null, null));
        }
    }

    @PostMapping("/registerCustomer")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) {
        Customer customers = customerService.register(customer);
        return ResponseEntity.ok(customers);
    }
}

