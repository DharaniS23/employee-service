package com.dev.project.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.project.employee.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
