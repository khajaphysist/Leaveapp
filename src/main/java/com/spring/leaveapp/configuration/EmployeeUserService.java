package com.spring.leaveapp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spring.leaveapp.domain.Employee;
import com.spring.leaveapp.domain.EmployeeService;
@Component
public class EmployeeUserService implements UserDetailsService {
	@Autowired
	EmployeeService service;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = service.findByEmail(username);
		if(employee!=null) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(employee.getRole()));
			return new User(employee.getEmail(),
					employee.getPassword(),
					authorities);
		}
		throw new UsernameNotFoundException("User: " + username + " Not Found!");
	}

}
