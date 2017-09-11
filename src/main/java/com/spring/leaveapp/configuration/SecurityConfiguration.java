package com.spring.leaveapp.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.formLogin().loginPage("/").permitAll().successHandler(authenticationSuccessHandler)
		.and()
		.rememberMe().tokenValiditySeconds(2419200).key("rememberMeKey")
		.and()
		.logout().logoutSuccessUrl("/")
		.and()
		.authorizeRequests()
		.antMatchers("/manager","/manager/**").hasRole("ADMIN")
		.antMatchers("/employee","/employee/**").hasRole("USER")
		.antMatchers("/h2-console","/h2-console/**").permitAll()
		.and()
		.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
	}
/*	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("user").roles("USER")
		.and()
		.withUser("admin").password("admin").roles("USER","ADMIN");
	}*/
}
