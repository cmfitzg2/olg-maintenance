package com.example.servingwebcontent.security;

import com.example.servingwebcontent.users.OLGUser;
import com.example.servingwebcontent.users.UserManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/home", "/register", "/css/**", "/js/**", "/img/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(User.withDefaultPasswordEncoder().username("admin").password("admin")
				.roles(UserManager.ADMIN).build());
		UserManager.OLGUsers.add(new OLGUser("admin", "admin", "8283030577", "admin", UserManager.ADMIN));
		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
