package com.dev.project.employee.exception;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dev.project.employee.models.LogDescription;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.models.Status;

@RestControllerAdvice
public class EmployeeExceptionalHandler {
	@ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        // Collect all validation error messages
		List<String> errors = ex.getBindingResult().getFieldErrors()
		        .stream()
		        .sorted(Comparator.comparingInt(err -> fieldOrder(err.getField())))
		        .map(err -> err.getField() + ": " + err.getDefaultMessage())
		        .toList();

        Response<Object> errorResponse = Response.builder()
                .status(Status.FAILURE)
                .message(LogDescription.VALIDATION_FAILED.getLog())
                .data(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	
	private int fieldOrder(String field) {
	    return switch (field) {
	        case "name" -> 1;
	        case "email" -> 2;
	        case "department" -> 3;
	        default -> Integer.MAX_VALUE;
	    };
	}
}

