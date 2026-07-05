package org.northernarc.unittestingemployee.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.northernarc.unittestingemployee.exceptions.EmployeeNotFoundException;
import org.northernarc.unittestingemployee.model.Employee;
import org.northernarc.unittestingemployee.service.EmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@MockMvcTest
@Import(EmployeeController.class)
@SpringBootTest
public class EmployeeControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    //@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    EmployeeServiceDao employeeServiceDao;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetEmployeeSuccess() throws Exception {
        Employee employee=new Employee();
        employee.setId(1L);
        employee.setName("Kalyan");
        employee.setSalary(10001);
        when(employeeServiceDao.getEmployeeById(1L)).thenReturn(employee);
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Kalyan"))
                .andExpect(jsonPath("$.salary").value(10001));
    }
    @Test
    void testEmployeeNotFoundException() throws Exception {
        when(employeeServiceDao.getEmployeeById(2L)).thenThrow(new EmployeeNotFoundException("Employee not found with id: 2"));
        mockMvc.perform(get("/api/employees/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 2"));

    }

    @Test
    void testDeleteEmployeeSuccess() throws Exception {
//        Employee employee=new Employee();
//        employee.setId(1L);
//        employee.setName("Kalyan");
//        employee.setSalary(10001);
//        doNothing().when(employeeServiceDao).deleteEmployee(1L);
        mockMvc.perform(delete("/api/employees/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEmployeeSuccess() throws Exception {
        Employee employee=new Employee();
        employee.setId(1L);
        employee.setName("Kalyan");
        employee.setSalary(10001);
        when(employeeServiceDao.createEmployee(any(Employee.class))).thenReturn(employee);
        mockMvc.perform(post("/api/employees/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Kalyan"))
                .andExpect(jsonPath("$.salary").value(10001));
    }
}
