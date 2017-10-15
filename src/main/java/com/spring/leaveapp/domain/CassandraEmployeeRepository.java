package com.spring.leaveapp.domain;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CassandraEmployeeRepository implements EmployeeRepository {
	
	@Autowired
    CqlOperations cqlOperations;

	
	private final String SELECT_BY_EMAIL = "SELECT * FROM employees WHERE email = ? ALLOW FILTERING";
	private final String SAVE_EMPLOYEE = "INSERT INTO employees(employee_id,name,email,password,role) values(?,?,?,?,?)";
	private final String SELECT_ALL = "SELECT * FROM employees";
	private final String DELETE_BY_ID = "DELETE FROM employees WHERE employee_id = ?";
	private final String FIND_BY_ID = "SELECT * FROM employees WHERE employee_id = ? ALLOW FILTERING";

	@Override
	public Employee findByEmail(String email) {
		return this.cqlOperations.queryForObject(SELECT_BY_EMAIL, rowMapper, email);
	}

	@Override
	public Employee findById(Long employeeId) {
		return this.cqlOperations.queryForObject(FIND_BY_ID, rowMapper, employeeId);
	}

	@Override
	public Iterable<Employee> findAll() {
		return this.cqlOperations.query(SELECT_ALL,rowMapper);
	}

	@Override
	public void save(Employee employee) {
		this.cqlOperations.execute(SAVE_EMPLOYEE,
				System.currentTimeMillis(),
				employee.getName(),
				employee.getEmail(),
				employee.getPassword(),
				employee.getRole());
	}

	@Override
	public void delete(Long id) {
		this.cqlOperations.execute(DELETE_BY_ID, id);
	}
	
	private Employee mapEmployee(ResultSet rs, int row) throws SQLException {
		return new Employee(rs.getLong("employee_id"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("password"),
				rs.getString("role"));
	}

	private RowMapper<Employee> rowMapper = (rs,row)-> new Employee(
	        rs.getLong("employee_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"));

}
