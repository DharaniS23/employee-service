package com.dev.project.employee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.dev.project.employee.exception.EmployeeNotFoundException;
import com.dev.project.employee.models.Employee;
import com.dev.project.employee.models.LogDescription;
import com.dev.project.employee.models.Response;
import com.dev.project.employee.models.Status;
import com.dev.project.employee.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Spy
	@InjectMocks
	private EmployeeServiceImpl employeeService;

	private Employee employee;

	@BeforeEach
	void setUp() {
		employee = new Employee(1L, "Sheldon", "Physics", "sheldon@gmail.com");
	}

	@Test
	void create_ShouldCallBuildSuccessResponse_AndReturnExpectedResponse() {
		// Arrange
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

		// Optional: Create a fake response if you want to override the default method
		Response<Employee> expectedResponse = new Response<>();
		expectedResponse.setData(employee);
		expectedResponse.setMessage(LogDescription.CREATED_EMPLOYEE.getLog());
		expectedResponse.setStatus(Status.SUCCESS);

		// Mock the default method via spy (optional - use real one if not overridden)
		doReturn(expectedResponse).when(employeeService).buildSuccessResponse(employee,
				LogDescription.CREATED_EMPLOYEE,employee.getId());

		// Act
		Response<Employee> actualResponse = employeeService.create(employee);

		// Assert
		assertNotNull(actualResponse);
		assertEquals(employee.getId(), actualResponse.getData().getId());
		assertEquals(employee.getName(), actualResponse.getData().getName());
		assertEquals(LogDescription.CREATED_EMPLOYEE.getLog(), actualResponse.getMessage());
		assertEquals(Status.SUCCESS, actualResponse.getStatus());

		// Verify interactions
		verify(employeeRepository, times(1)).save(employee);
		verify(employeeService, times(1)).buildSuccessResponse(employee, LogDescription.CREATED_EMPLOYEE,employee.getId());
	}

	@Test
	void create_ShouldThrowException_WhenRepositoryFails() {
		// Arrange
		RuntimeException dbException = new RuntimeException("Database write error");

		when(employeeRepository.save(any(Employee.class))).thenThrow(dbException);

		// Act & Assert
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			employeeService.create(employee);
		});

		assertEquals("Database write error", thrown.getMessage());

		// Verify repository call and NO call to buildSuccessResponse
		verify(employeeRepository, times(1)).save(employee);
		verify(employeeService, never()).buildSuccessResponse(any(), any());
	}

	@Test
	void create_ShouldThrowException_WhenEmployeeIsNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			employeeService.create(null);
		});
	}

	@Test
	void getAll_ShouldReturnListOfEmployees_WithSuccessResponse() {
		// Arrange
		List<Employee> mockEmployees = List.of(new Employee(1L, "Sheldon", "Physics", "sheldon@gmail.com"),
				new Employee(2L, "Leonard", "Physics", "leonard@gmail.com"));

		when(employeeRepository.findAll()).thenReturn(mockEmployees);

		// Build expected response using the builder
		Response<List<Employee>> expectedResponse = Response.<List<Employee>>builder().data(mockEmployees)
				.message(LogDescription.EMPLOYEE_COUNT_RETRIEVED.getLog()).status(Status.SUCCESS).build();

		// Mock buildSuccessResponse(...) from default method
		doReturn(expectedResponse).when(employeeService).buildSuccessResponse(mockEmployees,
				LogDescription.EMPLOYEE_COUNT_RETRIEVED, mockEmployees.size());

		// Act
		Response<List<Employee>> actualResponse = employeeService.getAll();

		// Assert
		assertNotNull(actualResponse);
		assertEquals(2, actualResponse.getData().size());
		assertEquals(LogDescription.EMPLOYEE_COUNT_RETRIEVED.getLog(), actualResponse.getMessage());
		assertEquals(Status.SUCCESS, actualResponse.getStatus());

		// Verify interactions
		verify(employeeRepository, times(1)).findAll();
		verify(employeeService, times(1)).buildSuccessResponse(mockEmployees, LogDescription.EMPLOYEE_COUNT_RETRIEVED,
				mockEmployees.size());
	}

	@Test
	void getAll_ShouldThrowException_WhenRepositoryFails() {
		when(employeeRepository.findAll()).thenThrow(new RuntimeException("DB error"));

		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			employeeService.getAll();
		});

		assertEquals("DB error", ex.getMessage());

		verify(employeeRepository, times(1)).findAll();
		verify(employeeService, never()).buildSuccessResponse(any(), any(), anyInt());
	}

	@Test
	void getAll_ShouldReturnEmptyList_WhenNoEmployeesExist() {
		when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

		Response<List<Employee>> expectedResponse = Response.<List<Employee>>builder().data(Collections.emptyList())
				.message(LogDescription.EMPLOYEE_COUNT_RETRIEVED.getLog()).status(Status.SUCCESS).build();

		doReturn(expectedResponse).when(employeeService).buildSuccessResponse(Collections.emptyList(),
				LogDescription.EMPLOYEE_COUNT_RETRIEVED, 0);

		Response<List<Employee>> actualResponse = employeeService.getAll();

		assertNotNull(actualResponse);
		assertTrue(actualResponse.getData().isEmpty());
		assertEquals(Status.SUCCESS, actualResponse.getStatus());

		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	void getById_ShouldReturnEmployee_WhenEmployeeExists() {
		// Arrange
		Long id = 1L;
		Employee mockEmployee = new Employee(id, "Sheldon", "Physics", "sheldon@gmail.com");

		when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

		Response<Employee> expectedResponse = Response.<Employee>builder().data(mockEmployee)
				.message(LogDescription.EMPLOYEE_COUNT_RETRIEVED.getLog()).status(Status.SUCCESS).build();

		doReturn(expectedResponse).when(employeeService).buildSuccessResponse(mockEmployee,
				LogDescription.EMPLOYEE_COUNT_RETRIEVED, 1);

		// Act
		Response<Employee> actualResponse = employeeService.getById(id);

		// Assert
		assertNotNull(actualResponse);
		assertEquals("Sheldon", actualResponse.getData().getName());
		assertEquals(Status.SUCCESS, actualResponse.getStatus());
		assertEquals(LogDescription.EMPLOYEE_COUNT_RETRIEVED.getLog(), actualResponse.getMessage());

		verify(employeeRepository, times(1)).findById(id);
	}

	@Test
	void getById_ShouldThrowException_WhenEmployeeNotFound() {
		// Arrange
		Long id = 100L;
		when(employeeRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
			employeeService.getById(id);
		});

		assertEquals(LogDescription.EMPLOYEE_NOT_FOUND.getLog(), exception.getMessage());

		verify(employeeRepository, times(1)).findById(id);
		verify(employeeService, never()).buildSuccessResponse(any(), any());
	}

	@Test
	void update_ShouldReturnUpdatedEmployee_WhenIdExists() {
		// Arrange
		Long id = 1L;

		Employee existing = Employee.builder().id(id).name("Sheldon").department("Physics").email("sheldon@gmail.com")
				.build();

		Employee updated = Employee.builder().name("Cooper").department("Math").email("cooper@gmail.com").build();

		Employee saved = Employee.builder().id(id).name("Cooper").department("Math").email("cooper@gmail.com").build();

		when(employeeRepository.findById(id)).thenReturn(Optional.of(existing));

		when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

		// Act
		Response<Employee> response = employeeService.update(id, updated);

		// Assert
		assertNotNull(response);
		assertEquals(Status.SUCCESS, response.getStatus());
		assertEquals("Cooper", response.getData().getName());
		assertEquals("Math", response.getData().getDepartment());
		assertEquals("cooper@gmail.com", response.getData().getEmail());

		verify(employeeRepository, times(1)).save(existing);
	}

	@Test
	void update_ShouldThrowException_WhenEmployeeNotFound() {
		// Arrange
		Long id = 1L;

		Employee updatedEmployee = Employee.builder().name("Fake").email("fake@none.com").department("N/A").build();

		// Mocking repo to simulate employee not found
		when(employeeRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.update(id, updatedEmployee));

		assertEquals(LogDescription.EMPLOYEE_NOT_FOUND.getLog(), exception.getMessage());
	}

	@Test
	void delete_ShouldDeleteEmployeeById_AndReturnSuccessResponse() {
		Long id = 1L;

		// Act
		Response<Void> response = employeeService.delete(id);

		// Assert
		assertNotNull(response);
		assertEquals(Status.SUCCESS, response.getStatus());
		assertEquals(LogDescription.DELETED_EMPLOYEE.getLog(), response.getMessage());

		verify(employeeRepository, times(1)).deleteById(id);
	}

	@Test
	void delete_ShouldThrowException_WhenRepositoryFails() {
		Long id = 999L;

		doThrow(new EmptyResultDataAccessException(1)).when(employeeRepository).deleteById(id);

		assertThrows(EmptyResultDataAccessException.class, () -> {
			employeeService.delete(id);
		});

		verify(employeeRepository, times(1)).deleteById(id);
	}
}
