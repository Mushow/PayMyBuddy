package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

import java.math.BigDecimal;

@Controller
public class TransferController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/transfer")
    public String showTransfer(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        BigDecimal availableFunds = walletService.getBalance(customUserDetails.getUserId());
        model.addAttribute("availableFunds", availableFunds);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Email does not exist in the database.");
        return "redirect:/transfer";
    }

}