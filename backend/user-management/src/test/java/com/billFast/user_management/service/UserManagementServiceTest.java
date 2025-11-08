package com.billFast.user_management.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.billFast.user_management.entity.Customer;
import com.billFast.user_management.entity.User;
import com.billFast.user_management.repository.CustomerRepository;
import com.billFast.user_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private UserManagementService service;

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("user1");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("user2");
        user2.setPassword("hash2");

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = service.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setFirstName("Likhitha");

        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setFirstName("Priya");

        when(customerRepo.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = service.getAllCustomers();

        assertEquals(2, customers.size());
        assertEquals("Likhitha", customers.get(0).getFirstName());
        assertEquals("Priya", customers.get(1).getFirstName());
    }
}

