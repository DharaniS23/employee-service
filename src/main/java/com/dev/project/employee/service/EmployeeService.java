package com.dev.project.employee.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dev.project.employee.models.Employee;
import com.dev.project.employee.models.LogDescription;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.models.Status;
public interface EmployeeService {
	
    Response<Employee>  create(Employee employee) throws Exception;
    
    Response< List<Employee>> getAll() throws Exception;
    
    Response<Employee>  getById(Long id) throws Exception;
    
    Response<Employee> update(Long id, Employee employee) throws Exception;
    
    Response<Void> delete(Long id) throws Exception;
    
    default <T> ResponseEntity<Response<T>> buildErrorResponse(Exception e, LogDescription logDesc) {
	    Response<T> errorResponse = Response.<T>builder()
									        .status(Status.FAILURE)
									        .message(logDesc.getLog() + " " + e.getMessage())
									        .data(null)
									        .build();

	    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
    
    default  <T> Response<T> buildSuccessResponse(T data,LogDescription logDesc,Object... args) {
	   return Response.<T>builder()
			        .status(Status.SUCCESS)
			        .message(logDesc.getLog().formatted(args))
			        .data(data)
			        .build();
	}
}

