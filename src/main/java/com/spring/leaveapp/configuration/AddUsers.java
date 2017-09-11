package com.spring.leaveapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.spring.leaveapp.domain.Employee;
import com.spring.leaveapp.domain.EmployeeService;

@Component
public class AddUsers implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private EmployeeService service;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Employee manager = new Employee();
		manager.setName("admin");
		manager.setEmail("admin@localhost");
		manager.setPassword("admin");
		manager.setRole("ROLE_ADMIN");
		
		service.save(manager);
		
		Employee employee = new Employee();
		employee.setName("user");
		employee.setEmail("user@localhost");
		employee.setPassword("user");
		employee.setRole("ROLE_USER");
		
		service.save(employee);
	}

}
