package com.dev.project.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.project.employee.exception.EmployeeNotFoundException;
import com.dev.project.employee.models.Employee;
import com.dev.project.employee.models.LogDescription;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repo;

	@Override
	public Response<Employee> create(Employee employee) {
		if (employee != null) {
			Employee response = repo.save(employee);
			return buildSuccessResponse(response, LogDescription.CREATED_EMPLOYEE,response.getId());
		}else {
			throw new IllegalArgumentException(LogDescription.EMPLOYEE_CANNOT_BE_NULL.getLog());
		}
	}

	@Override
	public Response<List<Employee>> getAll() {
		List<Employee> response = repo.findAll();
		return buildSuccessResponse(response, LogDescription.EMPLOYEE_COUNT_RETRIEVED, response.size());
	}

	@Override
	public Response<Employee> getById(Long id) {

		Optional<Employee> employee = repo.findById(id);

		if (employee.isPresent())
			return buildSuccessResponse(employee.get(), LogDescription.EMPLOYEE_COUNT_RETRIEVED,1);
		else
			throw new EmployeeNotFoundException(LogDescription.EMPLOYEE_NOT_FOUND.getLog());
	}

	@Override
	public Response<Employee> update(Long id, Employee employee) {
		Response<Employee> existingEmployee = getById(id);

		Employee emp = existingEmployee.getData();
		emp.setName(employee.getName());
		emp.setDepartment(employee.getDepartment());
		emp.setEmail(employee.getEmail());
		Employee savedEmployee = repo.save(emp);
		return buildSuccessResponse(savedEmployee, LogDescription.UPDATED_EMPLOYEE);
	}

	@Override
	public Response<Void> delete(Long id) {
		repo.deleteById(id);
		return buildSuccessResponse(null, LogDescription.DELETED_EMPLOYEE);
	}
}
