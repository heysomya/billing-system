package com.billFast.user_management.service;

import com.billFast.user_management.entity.Customer;
import com.billFast.user_management.entity.User;
import com.billFast.user_management.repository.CustomerRepository;
import com.billFast.user_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagementService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public UserManagementService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}
