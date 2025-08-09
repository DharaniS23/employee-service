package com.dev.project.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.employee.models.Employee;
import com.dev.project.employee.models.LogDescription;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
	
    private final EmployeeService service;

    @PostMapping
    public ResponseEntity<Response<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
        try {
        	
            log.info(LogDescription.CREATING_EMPLOYEE.getLog(), employee);
            
            Response<Employee> employeeResponse = service.create(employee);
            
            log.info(LogDescription.CREATED_EMPLOYEE.getLog(), employeeResponse.getData().getId());
            
            return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
        } catch (Exception e) {
        	
            log.error(LogDescription.EXCEPTION_OCCURRED_WHILE_CREATING_EMPLOYEE.getLog(), e.getMessage(), e);
            
            return service.buildErrorResponse(e, LogDescription.EXCEPTION_OCCURED);
        }
        
    }
    
    @GetMapping
    public ResponseEntity<Response<List<Employee>>> getAllEmployee() {
        try {
        	
            log.info(LogDescription.RETRIEVING_EMPLOYEES.getLog());
            
            Response<List<Employee>> employeeResponse = service.getAll();
            
            log.info(LogDescription.RETRIEVED_EMPLOYEES.getLog());
            
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } catch (Exception e) {
        	
            log.error(LogDescription.EXCEPTION_OCCURRED_WHILE_RETRIEVING_EMPLOYEES.getLog(), e.getMessage(), e);
            
            return service.buildErrorResponse(e, LogDescription.EXCEPTION_OCCURED);
        }
        
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Response<Employee>> getEmployeeById(@PathVariable Long id) {
        try {
        	
            log.info(LogDescription.RETRIEVING_EMPLOYEE.getLog());
            
            Response<Employee> employeeResponse = service.getById(id);
            
            log.info(LogDescription.RETRIEVED_EMPLOYEE.getLog());
            
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } catch (Exception e) {
        	
            log.error(LogDescription.EXCEPTION_OCCURRED_WHILE_RETRIEVING_EMPLOYEE.getLog(), e.getMessage(), e);
            
            return service.buildErrorResponse(e, LogDescription.EXCEPTION_OCCURED);
        }
        
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Response<Employee>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        try {
        	
            log.info(LogDescription.UPDATING_EMPLOYEE.getLog());
            
            Response<Employee> employeeResponse =  service.update(id, employee);
            
            log.info(LogDescription.UPDATED_EMPLOYEE.getLog());
            
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } catch (Exception e) {
        	
            log.error(LogDescription.EXCEPTION_OCCURRED_WHILE_UPDATING_EMPLOYEE.getLog(), e.getMessage(), e);
            
            return service.buildErrorResponse(e, LogDescription.EXCEPTION_OCCURED);
        }
        
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteEmployee(@PathVariable Long id) {
        try {
        	
            log.info(LogDescription.DELETING_EMPLOYEE.getLog());
            
            Response<Void> employeeResponse =  service.delete(id);
            
            log.info(LogDescription.DELETED_EMPLOYEE.getLog());
            
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } catch (Exception e) {
        	
            log.error(LogDescription.EXCEPTION_OCCURRED_WHILE_DELETING_EMPLOYEE.getLog(), e.getMessage(), e);
            
            return service.buildErrorResponse(e, LogDescription.EXCEPTION_OCCURED);
        }
        
    }
}
