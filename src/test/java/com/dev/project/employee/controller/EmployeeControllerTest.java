package com.dev.project.employee.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dev.project.employee.models.Employee;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;




@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EmployeeService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EmployeeService employeeService() {
            return Mockito.mock(EmployeeService.class);
        }
    }

    private Employee employee;
    private Response<Employee> employeeResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setDepartment("HR");
        employee.setEmail("john@example.com");

        employeeResponse = new Response<>();
        employeeResponse.setData(employee);

        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(service.create(any(Employee.class))).thenReturn(employeeResponse);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.name").value("John Doe"));
    }
    
    
    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employeeList = List.of(employee);
        Response<List<Employee>> listResponse = new Response<>();
        listResponse.setData(employeeList);

        when(service.getAll()).thenReturn(listResponse);

        mockMvc.perform(get("/employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(service.getById(1L)).thenReturn(employeeResponse);

        mockMvc.perform(get("/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(service.update(eq(1L), any(Employee.class))).thenReturn(employeeResponse);

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Response<Void> voidResponse = new Response<>();
        when(service.delete(1L)).thenReturn(voidResponse);

        mockMvc.perform(delete("/employees/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void givenInvalidEmployee_whenPost_thenReturnsValidationError() throws Exception {
        
        Employee invalidEmployee = new Employee();
        invalidEmployee.setName(""); 
        invalidEmployee.setEmail("invalid-email"); 

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0]", containsString("Name is required")))
                .andExpect(jsonPath("$.data[1]", containsString("Email should be valid")))
                .andExpect(jsonPath("$.data[2]", containsString("Department is required")))
                ;
    }
}
