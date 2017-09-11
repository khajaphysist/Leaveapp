package com.spring.leaveapp.domain;

public interface EmployeeRepository {

	Employee findByEmail(String name);

	Employee findById(Long employeeId);

	Iterable<Employee> findAll();

	void save(Employee employee);

	void delete(Long id);
	
}
