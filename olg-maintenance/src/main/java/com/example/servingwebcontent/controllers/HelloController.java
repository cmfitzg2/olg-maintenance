package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.modelobjects.AddUser;
import com.example.servingwebcontent.modelobjects.ChangePassword;
import com.example.servingwebcontent.modelobjects.FormUser;
import com.example.servingwebcontent.tickets.Ticket;
import com.example.servingwebcontent.tickets.TicketManager;
import com.example.servingwebcontent.users.OLGUser;
import com.example.servingwebcontent.users.UserManager;
import com.example.servingwebcontent.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class HelloController {

    @Autowired
    public InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("/register")
    public void getRegister(
            Model model,
            @RequestParam(value = "uid", required = false) String uid) {
        model.addAttribute("formuser", new FormUser());
        model.addAttribute("uid", uid);
    }

    @PostMapping("/register")
    public String postRegister(
            Model model,
            @RequestParam("uid") String uid,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        model.addAttribute("formuser", new FormUser());
        OLGUser user = UserManager.getUserByUid(uid);
        if (null != user && GeneralUtils.isNotEmpty(firstName) && GeneralUtils.isNotEmpty(lastName)
                && GeneralUtils.isNotEmpty(phone) && GeneralUtils.isNotEmpty(password)
                && GeneralUtils.isNotEmpty(confirmPassword) && confirmPassword.equals(password)
                && password.length() >= 5) {
            //inMemoryUserDetailsManager.updateUser();
            if (null == user.getFirstName() || user.getFirstName().isEmpty()) {
                System.out.println("REGISTERING USER");
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                inMemoryUserDetailsManager.createUser(User
                        .withUsername(user.getEmail())
                        .password("{noop}" + password)
                        .roles(UserManager.USER)
                        .build());
                return "redirect:dashboard";
            }
        } else {
            System.out.println("ERROR IN FORM");
        }
        return "redirect:register?error";
    }

    @PostMapping("/dashboard")
    public String postDashboard(@RequestParam("email") String email) {
        if (UserManager.getUserByEmail(email) == null) {
            System.out.println("********CREATING USER*************");
            final String uid = UUID.randomUUID().toString();
            UserManager.OLGUsers.add(new OLGUser(uid, "", "", "", email, UserManager.USER));
            return "redirect:dashboard";
        }
        return "redirect:dashboard?error";
    }

    @GetMapping("/dashboard")
    public void getDashboard(Model model, HttpServletRequest request) {
        model.addAttribute("adduser", new AddUser());
        model.addAttribute("users", UserManager.OLGUsers);
        model.addAttribute("tickets", TicketManager.tickets);
        OLGUser user = UserManager.getUserByEmail(request.getRemoteUser());
        if (null != user) {
            model.addAttribute("userUid", user.getUid());
        }
    }

    @GetMapping("/ticket-submit")
    public void getTicketSubmit(Model model, HttpServletRequest request) {
        model.addAttribute("newTicket", new Ticket());
        model.addAttribute("users", UserManager.OLGUsers);
        OLGUser user = UserManager.getUserByEmail(request.getRemoteUser());
        if (null != user) {
            model.addAttribute("userUid", user.getUid());
        }
    }

    @PostMapping("/ticket-submit")
    public String postTicketSubmit(
            Model model,
            @RequestParam(value = "assignedTo", required = false) String assignedTo,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam("submittedBy") String submittedBy,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        model.addAttribute("newTicket", new Ticket());
        Ticket ticket = new Ticket();
        ticket.setUid(UUID.randomUUID().toString());
        ticket.setAssignedTo(assignedTo);
        ticket.setSubmittedBy(submittedBy);
        ticket.setStatus(TicketManager.STATUS_READY);
        ticket.setPriority(priority);
        ticket.setTitle(title);
        ticket.setDescription(description);
        TicketManager.tickets.add(ticket);

        return "redirect:dashboard?ticketSuccess";
    }

    @GetMapping("/view-ticket")
    public void getViewTicket(
            Model model,
            HttpServletRequest request,
            @RequestParam(value = "uid", required = false) String uid) {
        OLGUser user = UserManager.getUserByEmail(request.getRemoteUser());
        if (null != user) {
            model.addAttribute("userUid", user.getUid());
        }
        model.addAttribute("ticketUid", uid);
        model.addAttribute("users", UserManager.OLGUsers);
        model.addAttribute("newTicket", new Ticket());
        Ticket ticket = TicketManager.getTicketByUid(uid);
        if (null != ticket) {
            model.addAttribute("ticketTitle", ticket.getTitle());
            model.addAttribute("ticketDescription", ticket.getDescription());
            model.addAttribute("ticketStatus", ticket.getStatus());
            model.addAttribute("ticketPriority", ticket.getPriority());
            model.addAttribute("ticketSubmittedBy", ticket.getSubmittedBy());
            model.addAttribute("ticketAssignedTo", ticket.getAssignedTo());
        } else {
            System.out.println("TICKET NULL FOR UID " + uid);
        }
    }

    @PostMapping("/view-ticket")
    public String postViewTicket(
            Model model,
            @RequestParam(value = "assignedTo", required = false) String assignedTo,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam("status") String status,
            @RequestParam("uid") String uid,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        model.addAttribute("newTicket", new Ticket());
        Ticket ticket = TicketManager.getTicketByUid(uid);
        if (null != ticket) {
            if (GeneralUtils.isNotEmpty(assignedTo)) {
                ticket.setAssignedTo(assignedTo);
            }
            ticket.setStatus(status);
            if (GeneralUtils.isNotEmpty(priority)) {
                ticket.setPriority(priority);
            }
            ticket.setTitle(title);
            ticket.setDescription(description);
            TicketManager.setTicketByUid(uid, ticket);
        } else {
            System.out.println("TICKET NULL");
            return "redirect:dashboard?ticketError";
        }

        return "redirect:dashboard?ticketSuccess";
    }

    @GetMapping("/view-user")
    public void getViewUser(
            Model model,
            @RequestParam(value = "uid", required = false) String uid) {
        model.addAttribute("formuser", new FormUser());
        model.addAttribute("uid", uid);
        model.addAttribute("userUid", uid);
        OLGUser user = UserManager.getUserByUid(uid);
        if (null != user) {
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userFirstName", user.getFirstName());
            model.addAttribute("userLastName", user.getLastName());
            model.addAttribute("userPhone", user.getPhone());
        } else {
            System.out.println("USER NULL FOR UID " + uid);
        }
    }

    @PostMapping("/view-user")
    public String postViewUser(
            Model model,
            @RequestParam("uid") String uid,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        System.out.println("POSTED TO VIEW USER");
        model.addAttribute("formuser", new FormUser());
        OLGUser user = UserManager.getUserByUid(uid);
        if (null != user && GeneralUtils.isNotEmpty(email) && GeneralUtils.isNotEmpty(firstName)
                && GeneralUtils.isNotEmpty(lastName) && GeneralUtils.isNotEmpty(phone)
                && GeneralUtils.isNotEmpty(password) && GeneralUtils.isNotEmpty(confirmPassword)
                && confirmPassword.equals(password) && password.length() >= 5) {
            String oldEmail = user.getEmail();
            if (inMemoryUserDetailsManager.userExists(oldEmail)) {
                String oldPass = inMemoryUserDetailsManager.loadUserByUsername(oldEmail).getPassword();
                String adminPass = inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword();
                //now check if the user trying to update is the same as this user or the admin
                if (oldPass.equals(password) || adminPass.equals(password)
                        || oldPass.equals("{noop}" + password)
                        || adminPass.equals("{noop}" + password)) {
                    //authorized to update
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPhone(phone);
                    if (!inMemoryUserDetailsManager.userExists(email)) {
                        //the email changed and the update workflow doesn't support changing username
                        //so just delete and re-add, since all the tickets and stuff are tied to uid anyway
                        inMemoryUserDetailsManager.deleteUser(oldEmail);
                        System.out.println("DELETED OLD USER FROM MEMORY");
                        user.setEmail(email);
                        inMemoryUserDetailsManager.createUser(User
                                .withUsername(email)
                                .password(oldPass)
                                .roles(UserManager.USER)
                                .build());
                        System.out.println(inMemoryUserDetailsManager.loadUserByUsername(email).getPassword());
                    }
                    for (Ticket ticket : TicketManager.tickets) {
                        if (ticket.getAssignedTo().equals(oldEmail)) {
                            ticket.setAssignedTo(email);
                        }
                        if (ticket.getSubmittedBy().equals(oldEmail)) {
                            ticket.setSubmittedBy(email);
                        }
                    }
                    System.out.println("USER UPDATED");
                    return "redirect:dashboard";
                }
            }
        }
        System.out.println("ERROR UPDATING USER");
        return "redirect:dashboard?error";
    }

    @GetMapping("/delete-user")
    public void getDeleteUser(Model model,
                                @RequestParam("uid") String uid) {
        model.addAttribute("uid", uid);
        model.addAttribute("userUid", uid);
        model.addAttribute("formuser", new FormUser());
    }

    @PostMapping("/delete-user")
    public String postDeleteUser(Model model,
                                 @RequestParam("uid") String uid,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword) {
        model.addAttribute("formuser", new FormUser());
        if (password.equals(confirmPassword)
                && (password.equals(inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword())
                || ("{noop}" + password).equals(inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword()))) {
            OLGUser user = UserManager.getUserByUid(uid);
            if (null != user) {
                for (Ticket ticket : TicketManager.tickets) {
                    if (ticket.getAssignedTo().equals(user.getEmail())) {
                        ticket.setAssignedTo("admin");
                    }
                }
                inMemoryUserDetailsManager.deleteUser(user.getEmail());
                UserManager.OLGUsers.remove(user);
                return "redirect:dashboard?userSuccess";
            } else {
                System.out.println("USER NULL FOR UID " + uid);
            }
        } else {
            System.out.println(password);
            System.out.println("{noop}" + inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword());
            System.out.println(inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword());
            System.out.println("PASSWORD ERROR");
        }
        return "redirect:dashboard?error";
    }

    @GetMapping("/change-password")
    public void getChangePassword(Model model,
                              @RequestParam("uid") String uid) {
        model.addAttribute("uid", uid);
        model.addAttribute("userUid", uid);
        OLGUser user = UserManager.getUserByUid(uid);
        if (null != user) {
            model.addAttribute("userEmail", user.getEmail());
        }
        model.addAttribute("changepassword", new ChangePassword());
    }

    @PostMapping("/change-password")
    public String postChangePassword(Model model,
                                 @RequestParam("uid") String uid,
                                 @RequestParam("password") String password,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword) {
        model.addAttribute("changepassword", new ChangePassword());
        if (newPassword.equals(confirmPassword)
                && (password.equals(inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword())
                || ("{noop}" + password).equals(inMemoryUserDetailsManager.loadUserByUsername("admin").getPassword()))) {
            OLGUser user = UserManager.getUserByUid(uid);
            if (null != user && inMemoryUserDetailsManager.userExists(user.getEmail())) {
                inMemoryUserDetailsManager.updatePassword(
                        inMemoryUserDetailsManager.loadUserByUsername(user.getEmail()), "{noop}" + newPassword);
                return "redirect:dashboard?userSuccess";
            }
        }
        return "redirect:dashboard?error";
    }
}