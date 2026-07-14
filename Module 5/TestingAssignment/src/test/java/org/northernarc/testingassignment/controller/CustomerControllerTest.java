package org.northernarc.testingassignment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.dto.CustomerRequest;
import org.northernarc.testingassignment.dto.CustomerResponse;
import org.northernarc.testingassignment.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class CustomerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private CustomerService customerService;

    private CustomerRequest validRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        validRequest = new CustomerRequest();
        validRequest.setName("John Doe");
        validRequest.setEmail("john@example.com");
        validRequest.setPhone("9876543210");
        validRequest.setPassword("password123");
    }

    @Test
    public void createCustomer_withValidData_returns201Created() throws Exception {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setName("John Doe");
        response.setEmail("john@example.com");

        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class));
    }

    @Test
    public void createCustomer_withMissingEmail_returns400BadRequest() throws Exception {
        validRequest.setEmail(null);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    public void createCustomer_withInvalidEmail_returns400BadRequest() throws Exception {
        validRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    public void createCustomer_withInvalidPhoneLength_returns400BadRequest() throws Exception {
        validRequest.setPhone("123456");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    public void getAllCustomers_returnsOkWithList() throws Exception {
        CustomerResponse customer1 = new CustomerResponse();
        customer1.setId(1L);
        customer1.setName("Customer 1");

        CustomerResponse customer2 = new CustomerResponse();
        customer2.setId(2L);
        customer2.setName("Customer 2");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void getCustomerById_whenCustomerExists_returnsOk() throws Exception {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setName("John Doe");
        response.setEmail("john@example.com");

        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    public void updateCustomer_withValidData_returnsOk() throws Exception {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setName("Jane Doe");
        response.setEmail("jane@example.com");

        when(customerService.updateCustomer(eq(1L), any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(CustomerRequest.class));
    }

    @Test
    public void deleteCustomer_whenCustomerExists_returns204NoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
