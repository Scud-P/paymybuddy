package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PartnershipController {

    private static final Logger logger = LoggerFactory.getLogger(PartnershipController.class);

    @Autowired
    public UserService userService;

    @Autowired
    public PartnershipService partnershipService;

    @GetMapping("/getPartnerships")
    public String getPartnerships(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        List<String> emails = partnershipService.getEmailsFromPartners(userId);
        return "emails";
    }

}
