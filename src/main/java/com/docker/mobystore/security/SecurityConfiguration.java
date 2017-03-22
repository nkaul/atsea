package com.docker.mobystore.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		
    @Autowired
    DataSource dataSource;
 
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM customer WHERE username = ?")
               .authoritiesByUsernameQuery("SELECT username, role FROM customer WHERE username=?");
    }
	
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		      .antMatchers(HttpMethod.POST, "/order/**").authenticated()
		      .antMatchers(HttpMethod.DELETE, "/order/**").authenticated()
		      .antMatchers(HttpMethod.GET, "/order/**").authenticated()
		      .antMatchers(HttpMethod.GET, "/customer/**").authenticated()
			  .anyRequest().permitAll()
			.and().httpBasic()
			.and().csrf().disable();
	}	

}
