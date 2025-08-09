package com.dev.project.employee.models;

public enum LogDescription {
	
	CREATING_EMPLOYEE("Creating new employee"),
	CREATED_EMPLOYEE("Employee created successfully with ID: %d"),
	
	RETRIEVING_EMPLOYEES("Retrieving employee records"),
	RETRIEVED_EMPLOYEES("Retrieved employee records"),
	EMPLOYEE_COUNT_RETRIEVED("Total of %d employee records retrieved successfully"),
	
	RETRIEVING_EMPLOYEE("Retrieving employee record"),
	RETRIEVED_EMPLOYEE("Retrieved employee record"),
	
	UPDATING_EMPLOYEE("Updating employee record"),
	UPDATED_EMPLOYEE("Updated employee record"),
	
	DELETING_EMPLOYEE("Deleting employee record"),
	DELETED_EMPLOYEE("Employee deleted from the database"),
	
	EMPLOYEE_NOT_FOUND("Employee not found"),
	EMPLOYEE_CANNOT_BE_NULL("Employee cannot be null"),
	EXCEPTION_OCCURRED_WHILE_CREATING_EMPLOYEE("Exception occured while creating an employee"),
	EXCEPTION_OCCURRED_WHILE_RETRIEVING_EMPLOYEES("Exception occured while retrieving employees"),
	EXCEPTION_OCCURRED_WHILE_RETRIEVING_EMPLOYEE("Exception occured while retrieving employee"),
	EXCEPTION_OCCURRED_WHILE_UPDATING_EMPLOYEE("Exception occured while updating employee"),
	EXCEPTION_OCCURRED_WHILE_DELETING_EMPLOYEE("Exception occured while deleting employee"),
	
	EXCEPTION_OCCURED("Exception Ocurred. Reason : ");
	
	private final String log;
	
	private LogDescription(String log) {
		this.log = log;
	}
	
	public String getLog() {
		return log;
	}
	
	

}
