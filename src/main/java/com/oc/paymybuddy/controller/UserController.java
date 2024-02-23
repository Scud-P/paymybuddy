package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService userService;

    @Autowired
    public PartnershipService partnershipService;


    @PostMapping("/addUser")
    public String addUser(
    @RequestParam(value = "firstName") String firstName,
    @RequestParam(value = "lastName") String lastName,
    @RequestParam(value = "email") String email,
    @RequestParam(value = "password") String password) {
        userService.saveUserWithBasicInfo(firstName, lastName, email, password);
        return "redirect:/login";
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam("user") User user) {
        userService.deleteUser(user);
        return "redirect:/index";
    }

    @PostMapping("/submitNewInfo")
    public String modifyUserInfo(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            if (firstName != null) {
                currentUser.setFirstName(firstName);
            }
            if (lastName != null) {
                currentUser.setLastName(lastName);
            }
            if (email != null) {
                currentUser.setEmail(email);
            }
            if (password != null) {
                currentUser.setPassword(password);
            }
            userService.modifyUserInfo(currentUser);
            session.setAttribute("user", currentUser);
        }
        return "redirect:/profile";
    }

    @PostMapping("/finalizePurchase")
    public String buyCredit(
            @RequestParam(value = "purchase") Double purchase,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        userService.buyCredit(currentUser, purchase);
        return "redirect:/profile";
    }

    @PostMapping("/addConnection")
    public String addConnection(
            @RequestParam(value = "email") String email,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        partnershipService.addPartnership(currentUser, email);
        return  "redirect:/transfer";
    }

}
