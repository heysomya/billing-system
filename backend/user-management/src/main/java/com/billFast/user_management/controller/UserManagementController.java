package com.billFast.user_management.controller;

import com.billFast.user_management.entity.Customer;
import com.billFast.user_management.entity.User;
import com.billFast.user_management.service.UserManagementService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userManagementService.getAllUsers();
    }

    @GetMapping("/getUser/{name}")
    public Optional<User> getUserByUserName(@PathVariable String name) {
        return userManagementService.getUserByName(name);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return userManagementService.getAllCustomers();
    }
}

