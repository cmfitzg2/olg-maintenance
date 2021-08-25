package com.example.servingwebcontent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HelloController {
    @Autowired
    public InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("/register")
    public void putParam(Model model) {
        model.addAttribute("name", "hello world");
    }

    @PostMapping("/register")
    public void test(Model model) {

    }

    /*
    @GetMapping("/employee/{username}")
    public String checkIfUserExists(@PathVariable("username") String username) {
        boolean flag = inMemoryUserDetailsManager.userExists(username);
        if (flag)
            return "\""+username + "\" exist in InMemoryUserDetailsManager";
        else
            return "\""+username + "\" does not exist in InMemoryUserDetailsManager";
    }

    @GetMapping("/employee/create/{username}/{password}/{role}")
    public String createUser(@PathVariable("username") String username, @PathVariable("password") String password,
                             @PathVariable("role") String role) {

        ArrayList<GrantedAuthority> grantedAuthoritiesList= new ArrayList<>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(role));

        inMemoryUserDetailsManager.createUser(new User(username, password, grantedAuthoritiesList));

        return checkIfUserExists(username);
    }

    @GetMapping("/employee/update/{username}/{password}/{role}")
    public String updateUser(@PathVariable("username") String username, @PathVariable("password") String password,
                             @PathVariable("role") String role) {
        ArrayList<GrantedAuthority> grantedAuthoritiesList= new ArrayList<>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(role));

        inMemoryUserDetailsManager.updateUser(new User(username, password, grantedAuthoritiesList));

        return checkIfUserExists(username);
    }

    @GetMapping("/employee/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {
        inMemoryUserDetailsManager.deleteUser(username);

        return checkIfUserExists(username);
    }

     */
}