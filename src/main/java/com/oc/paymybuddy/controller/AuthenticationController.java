package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(email);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "No user was found for that email address");
            return "redirect:/login";
        }

        if (user.getPassword().equals(password)) {
            logger.info("User {} is now logged in", user);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("formattedBalance", user.getFormattedBalance());
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("error", "Incorrect password");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("Logging out user {}", user.getEmail());
        }
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/index";
    }
}
