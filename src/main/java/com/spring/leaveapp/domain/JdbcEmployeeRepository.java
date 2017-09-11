package com.spring.leaveapp.domain;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcEmployeeRepository implements EmployeeRepository {
	
	@Autowired
	JdbcOperations jdbcOperations;
	
	private final String SELECT_BY_EMAIL = "SELECT * FROM employees WHERE email = ?";
	private final String SAVE_EMPLOYEE = "INSERT INTO employees(name,email,password,role) values(?,?,?,?)";
	private final String SELECT_ALL = "SELECT * FROM employees";
	private final String DELETE_BY_ID = "DELETE FROM employees WHERE employee_id = ?";
	private final String FIND_BY_ID = "SELECT * FROM employees WHERE employee_id = ?";

	@Override
	public Employee findByEmail(String email) {
		return this.jdbcOperations.queryForObject(SELECT_BY_EMAIL, this::mapEmployee, email);
	}

	@Override
	public Employee findById(Long employeeId) {
		return this.jdbcOperations.queryForObject(FIND_BY_ID, this::mapEmployee, employeeId);
	}

	@Override
	public Iterable<Employee> findAll() {
		return this.jdbcOperations.query(SELECT_ALL,this::mapEmployee);
	}

	@Override
	public void save(Employee employee) {
		this.jdbcOperations.update(SAVE_EMPLOYEE,
				employee.getName(),
				employee.getEmail(),
				employee.getPassword(),
				employee.getRole());
	}

	@Override
	public void delete(Long id) {
		this.jdbcOperations.update(DELETE_BY_ID, id);
	}
	
	private Employee mapEmployee(ResultSet rs, int row) throws SQLException {
		return new Employee(rs.getLong("employee_id"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("password"),
				rs.getString("role"));
	}

}
