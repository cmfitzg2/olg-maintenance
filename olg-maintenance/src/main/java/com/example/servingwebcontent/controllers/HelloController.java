package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.modelobjects.AddUser;
import com.example.servingwebcontent.modelobjects.FormUser;
import com.example.servingwebcontent.users.OLGUser;
import com.example.servingwebcontent.users.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@Controller
public class HelloController {

    @Autowired
    public InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("/register")
    public void registerGet(@RequestParam("uid") String uid, Model model) {
        model.addAttribute("formuser", new FormUser());
        model.addAttribute("uid", uid);
    }

    @PostMapping("/register")
    public String registerPost(Model model,
                             @RequestParam("uid") String uid,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("phone") String phone,
                             @RequestParam("password") String password) {
        model.addAttribute("formuser", new FormUser());
        OLGUser user = UserManager.getUserByUid(uid);
        if (null != user) {
            //inMemoryUserDetailsManager.updateUser();
            if (null != user.getFirstName() && !user.getFirstName().isEmpty()) {
                System.out.println("UPDATING USER");
            } else {
                System.out.println("REGISTERING USER");
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                inMemoryUserDetailsManager.createUser(User
                        .withUsername(user.getEmail())
                        .password("{noop}" + password)
                        .roles(UserManager.USER).build());
            }
        } else {
            System.out.println("USER NULL - " + uid);
        }
        return "redirect:dashboard";
    }

    @PostMapping("/dashboard")
    public String create(@RequestParam("email") String email) {
        if (UserManager.getUserByEmail(email) == null) {
            System.out.println("********CREATING USER*************");
            final String uid = UUID.randomUUID().toString();
            UserManager.OLGUsers.add(new OLGUser(uid, "", "", "", email, UserManager.USER));
            return "redirect:dashboard";
        }
        return "redirect:dashboard?error";
    }

    @GetMapping("/dashboard")
    public void getDashboard(Model model) {
        model.addAttribute("adduser", new AddUser());
        model.addAttribute("users", UserManager.OLGUsers);
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