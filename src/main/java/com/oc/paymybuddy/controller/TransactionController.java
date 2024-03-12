package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    PartnershipService partnershipService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;


    @GetMapping("/transfer")
    public ModelAndView transfer(HttpSession session,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "3") int size) {

        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        long senderUserId = currentUser.getUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<TransactionDTO> transactionPage = transactionService.getRecentTransactionsDTOBySenderUserID(senderUserId, pageable);

        List<String> emails = partnershipService.getEmailsFromPartners(senderUserId);

        ModelAndView modelAndView = new ModelAndView("transfer");
        modelAndView.addObject("emails", emails);
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("transactions", transactionPage.getContent());
        modelAndView.addObject("currentPage", transactionPage.getNumber());
        modelAndView.addObject("totalPages", transactionPage.getTotalPages());

        return modelAndView;
    }

    @PostMapping("/submitTransaction")
    public String submitTransaction(
            HttpSession session,
            @RequestParam(value = "selectedEmail") String email,
            @RequestParam(value = "amount") Double amount,
            @RequestParam(value = "description") String description,
            RedirectAttributes redirectAttributes) {

        try {
            long userId = (long) session.getAttribute("userId");
            transactionService.submitTransaction(userId, email, amount, description);

            User updatedUser = userService.findById(userId);
            session.setAttribute("user", updatedUser);

            return "redirect:/transfer";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            logger.error("IllegalArgumentException occurred: {}", e.getMessage());
            return "redirect:/transfer";
        }
    }
}
