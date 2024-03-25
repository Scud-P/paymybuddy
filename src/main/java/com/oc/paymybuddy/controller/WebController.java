package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.model.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);


    @GetMapping("/signup")
    public String getSignup() {
        return "signup";
    }

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/connections")
    public String getConnections() {
        return "connections";
    }

    @GetMapping("/profile")
    public String getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "login";
        }
        return "profile";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/modifyInfo")
    public String modifyInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "login";
        }
        return "modifyinfo";
    }

    @GetMapping("/buyCredit")
    public String buyCredit(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "login";
        }
        return "buycredit";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
