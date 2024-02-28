package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.mushow.paymybuddy.services.TransactionService;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

import java.math.BigDecimal;

@Controller
public class BalanceController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/topup")
    public String topUp(@AuthenticationPrincipal CustomUserDetails userDetails,
                        RedirectAttributes redirectAttributes,
                        @RequestParam("amount") BigDecimal amount) {
        Long userId = userDetails.getUserId();
        try {
            walletService.topUpBalance(userId, amount);
            transactionService.topUp(userId, amount, "Add from bank!");
            redirectAttributes.addFlashAttribute("message", "Balance topped up successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("amount") BigDecimal amount) {
        Long userId = userDetails.getUserId();
        try {
            walletService.withdrawFromBalance(userId, amount);
            transactionService.withdraw(userId, amount, "Withdraw to bank!");
            redirectAttributes.addFlashAttribute("message", "Balance withdrawn successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }
}
