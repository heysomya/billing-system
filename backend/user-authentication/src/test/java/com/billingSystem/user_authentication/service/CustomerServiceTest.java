package com.billingSystem.user_authentication.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.billingSystem.user_authentication.entity.Customer;
import com.billingSystem.user_authentication.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testRegisterCustomerSuccess() {
        Customer inputCustomer = new Customer();
        inputCustomer.setFirstName("Likhitha");
        inputCustomer.setLastName("P");
        inputCustomer.setEmail("test@test.com");
        inputCustomer.setPhone("1234567890");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer registeredCustomer = customerService.register(inputCustomer);

        assertEquals("Likhitha", registeredCustomer.getFirstName());
        assertEquals("P", registeredCustomer.getLastName());
        assertEquals("test@test.com", registeredCustomer.getEmail());
        assertEquals("1234567890", registeredCustomer.getPhone());
    }
}

