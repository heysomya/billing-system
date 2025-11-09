package com.billFast.user_management.controller;

import com.billFast.user_management.entity.Customer;
import com.billFast.user_management.entity.User;
import com.billFast.user_management.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementControllerTest {

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private UserManagementController userManagementController;

    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("user2");

        when(userManagementService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userManagementController.getUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        verify(userManagementService, times(1)).getAllUsers();
    }

    @Test
    void testGetCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setFirstName("Likhitha");

        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setFirstName("Priya");

        when(userManagementService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = userManagementController.getCustomers();

        assertEquals(2, customers.size());
        assertEquals("Likhitha", customers.get(0).getFirstName());
        assertEquals("Priya", customers.get(1).getFirstName());
        verify(userManagementService, times(1)).getAllCustomers();
    }

    @Test
    void getUserByUserName_found_returnsUser() {
        String name = "john";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(name);

        when(userManagementService.getUserByName(name)).thenReturn(Optional.of(user));

        Optional<User> result = userManagementController.getUserByUserName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getUsername());
        verify(userManagementService).getUserByName(name);
    }

    @Test
    void getUserByUserName_notFound_returnsEmpty() {
        String name = "nonexistent";
        when(userManagementService.getUserByName(name)).thenReturn(Optional.empty());

        Optional<User> result = userManagementController.getUserByUserName(name);

        assertFalse(result.isPresent());
        verify(userManagementService).getUserByName(name);
    }


}
