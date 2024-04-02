package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.mushow.paymybuddy.services.TransactionService;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

@Controller
public class HomeController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/home")
    public String getHomePage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("balance", walletService.getBalance(userId));
        model.addAttribute("friends", userService.getFriends(userId));
        model.addAttribute("transactions", transactionService.getIssuerTransactionsDTO(userId));
        model.addAttribute("receiverTransactions", transactionService.getReceiverTransactionsDTO(userId));
        return "home";
    }

}
