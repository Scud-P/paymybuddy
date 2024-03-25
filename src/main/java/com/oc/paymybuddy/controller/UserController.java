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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @RequestParam(value = "password") String password,
            RedirectAttributes redirectAttributes) {

        try {
            userService.saveUserWithBasicInfo(firstName, lastName, email, password);
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
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
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if(firstName == null || firstName.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "First Name can't be empty");
            return "redirect:/modifyInfo";
        }

        if(lastName == null || lastName.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Last Name can't be empty");
            return "redirect:/modifyInfo";
        }

        if(email == null || email.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email can't be empty");
            return "redirect:/modifyInfo";
        }

        if(password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password can't be empty");
            return "redirect:/modifyInfo";
        }

        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                if (!firstName.equals(currentUser.getFirstName())) {
                    currentUser.setFirstName(firstName);
                }
                if (!lastName.equals(currentUser.getLastName())) {
                    currentUser.setLastName(lastName);
                }
                if (!email.equals(currentUser.getEmail())) {
                    currentUser.setEmail(email);
                }
                if (!password.equals(currentUser.getPassword())) {
                    currentUser.setPassword(password);
                }
                userService.modifyUserInfo(currentUser);
                session.setAttribute("user", currentUser);
                return "redirect:/profile";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/modifyInfo";
        }
        return "redirect:/login";
    }

    @PostMapping("/finalizePurchase")
    public String buyCredit(
            @RequestParam(value = "purchase") Double purchase,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            userService.buyCredit(currentUser, purchase);
            return "redirect:/profile";

        } catch(IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            logger.error("IllegalArgumentException occurred: {}", e.getMessage());
            return "redirect:/buyCredit";
        }
    }

    @PostMapping("/addPartnership")
    public String addConnection(
            @RequestParam(value = "email") String email,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            long currentUserId = (long) session.getAttribute("userId");
            partnershipService.addPartnership(currentUserId, email);
            return "redirect:/transfer";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            logger.error("IllegalArgumentException occurred: {}", e.getMessage());
            return "redirect:/connections";
        }
    }

}
