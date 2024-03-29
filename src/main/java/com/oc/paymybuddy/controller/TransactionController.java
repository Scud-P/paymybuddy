package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView transfer(HttpSession session, Pageable pageable) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        long senderUserId = currentUser.getUserId();

        Page<Transaction> transactionPage = transactionService.getRecentTransactionsBySenderUserID(senderUserId, pageable);

        List<TransactionDTO> transactionDTOs = transactionPage.getContent().stream()
                .map(transaction -> {
                    User receiver = userService.findById(transaction.getReceiverUserId());
                    return new TransactionDTO(transaction, receiver);
                })
                .toList();

        List<String> emails = partnershipService.getEmailsFromPartners(senderUserId);

        ModelAndView modelAndView = new ModelAndView("transfer");
        modelAndView.addObject("emails", emails);
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("transactions", transactionDTOs);

        return modelAndView;
    }

    @PostMapping("/submitTransaction")
    public String submitTransaction(
            HttpSession session,
            @RequestParam(value = "selectedEmail") String email,
            @RequestParam(value = "amount") Double amount,
            @RequestParam(value = "description") String description) {

        transactionService.submitTransaction(session, email, amount, description);

        return "redirect:/transfer";
    }

}
