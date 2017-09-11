package com.spring.leaveapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository repository;
	
	public Employee findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public Employee findById(Long employeeId){ return repository.findById(employeeId); }
	
	public Iterable<Employee> findAllEmployees(){
		return repository.findAll();
	}
	
	public void save(Employee employee) {
		repository.save(employee);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}
}
