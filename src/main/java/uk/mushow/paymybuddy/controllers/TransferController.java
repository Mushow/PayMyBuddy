package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.services.TransactionService;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

import java.math.BigDecimal;
import java.util.Set;

@Controller
public class TransferController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transfer")
    public String showTransfer(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        BigDecimal availableFunds = walletService.getBalance(customUserDetails.getUserId());
        Set<User> friends = userService.getFriends(customUserDetails.getUserId());
        model.addAttribute("user", userService.getUserById(customUserDetails.getUserId()));
        model.addAttribute("friends", friends);
        model.addAttribute("availableFunds", availableFunds);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("friendEmail") String friendEmail,
                           @RequestParam("amount") BigDecimal amount,
                           @RequestParam("description") String description,
                           RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            transactionService.transfer(customUserDetails.getUserId(), friendEmail, amount, description);
            redirectAttributes.addFlashAttribute("message", "Transfer completed!.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transfer";
    }

}
