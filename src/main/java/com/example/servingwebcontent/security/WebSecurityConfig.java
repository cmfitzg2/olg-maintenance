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
import java.util.UUID;

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
		userDetailsList.add(User
				.withUsername("admin")
				.password("{noop}admin")
				.roles(UserManager.ADMIN)
				.build());
		final String uid = UUID.randomUUID().toString();
		UserManager.OLGUsers.add(new OLGUser(uid, "admin", "admin", "8283030577", "admin", UserManager.ADMIN));
		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
